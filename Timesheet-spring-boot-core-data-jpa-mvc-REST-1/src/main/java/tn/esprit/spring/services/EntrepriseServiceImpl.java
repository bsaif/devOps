package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Service
public class EntrepriseServiceImpl implements IEntrepriseService {
	public static final Logger logger = Logger.getLogger(EntrepriseServiceImpl.class);
	@Autowired
    EntrepriseRepository entrepriseRepoistory;
	@Autowired
	DepartementRepository deptRepoistory;
	String errorText = "Erreur:";
	public int ajouterEntreprise(Entreprise entreprise) {
		logger.info("START ajouterEntreprise ");

		try {
			logger.debug(entreprise.getId());

			logger.trace("debut d'ajout de l'entreprise: " + entreprise.getName());
			entrepriseRepoistory.save(entreprise);
			logger.trace("fin ajout");

			logger.debug("l'entreprise: " + entreprise.getName() + " de l'id: " + entreprise.getId()
					+ " ajoutée avec succé");

		} catch (Exception e) {
			logger.error(errorText + e);
		}
		logger.info("END ajouterEntreprise ");

		return entreprise.getId();
	}

	public int ajouterDepartement(Departement dep) {
		logger.info("START ajouterDepartement ");

		try {
			logger.debug(dep.getId());

			logger.trace("debut d'ajout du departement: " + dep.getName());
			deptRepoistory.save(dep);
			logger.trace("fin Ajout");

			logger.debug("l'dep: " + dep.getName() + " de l'id: " + dep.getId() + " ajoutée avec succé");

		} catch (Exception e) {
			logger.error(errorText + e);
		}
		logger.info("END ajouterdep ");

		return dep.getId();
	}
	
	public int  affecterDepartementAEntreprise(int depId, int entrepriseId) {
		//Le bout Master de cette relation N:1 est departement  
				//donc il faut rajouter l'entreprise a departement 
				// ==> c'est l'objet departement(le master) qui va mettre a jour l'association
				//Rappel : la classe qui contient mappedBy represente le bout Slave
				//Rappel : Dans une relation oneToMany le mappedBy doit etre du cote one.
		logger.info("START affecterDepartementAEntreprise ");
		Departement dep = new Departement();
		try {
			logger.debug("init dep" + dep);

			Optional<Entreprise> e = entrepriseRepoistory.findById(entrepriseId);
			Optional<Departement> d = deptRepoistory.findById(depId);
			logger.trace("Début Test : verifier l'existence du l'entreprise et du Departement");

			if (e.isPresent() && d.isPresent()) {

				logger.trace("début: récuperer entrep by ID");
				Entreprise entrepriseManagedEntity = e.get();
				logger.trace("fin: récuperer entrep by ID");

				logger.trace("début: récuperer dep by ID");
				Departement depManagedEntity = d.get();
				logger.trace("début: récuperer dep by ID");

				logger.trace("début: affectation d'entrep -> departement");
				depManagedEntity.setEntreprise(entrepriseManagedEntity);
				deptRepoistory.save(depManagedEntity);
				logger.trace("fin: affectation d'entrep -> departement");

				dep = depManagedEntity;

				logger.debug("new Dep" + dep);

				logger.debug("Entrep: " + depManagedEntity.getEntreprise().getId() + "-"
						+ depManagedEntity.getEntreprise().getName() + "affecté au department:"
						+ depManagedEntity.getName() + "-" + depManagedEntity.getId());
				logger.trace("FIN Test : verifier l'existence du l'entreprise et du Departement");

			}
		 else {
			logger.trace("Entrep ou Dep n'exitse pas");
			logger.trace("FIN Test : verifier l'existence du l'entreprise et du Departement");
		}

		} catch (Exception e) {
			logger.error(errorText + e);
		}

		logger.info("END affecterDepartementAEntreprise ");
		return dep.getEntreprise().getId();
		
	}
	
	public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) {
		logger.info("START getAllDepartementsNamesByEntreprise ");
		List<String> depNames = new ArrayList<>();
		Optional<Entreprise> e = entrepriseRepoistory.findById(entrepriseId);
	
		for(String elem: depNames)
	       {
			logger.debug("Init List"+elem);
	       }
		
		logger.trace("Début Test : verifier l'existence du l'entrepri");

		if (e.isPresent()) {
			logger.trace("Début Get : Entreprise");
			Entreprise entrepriseManagedEntity = e.get();
			logger.trace("FIN Get : Entreprise");

			logger.trace("Début parcour : de liste des dep d'entrep");
			for (Departement dep : entrepriseManagedEntity.getDepartements()) {
				depNames.add(dep.getName());
			}
			logger.trace("FIN parcour de liste des dep d'entrep");

			
			for(String elem: depNames)
		       {
				logger.debug("Final List"+elem);
		       }

			return depNames;
		}
		
		logger.trace("FIN Test : verifier l'existence du l'entrepri");

		
		
		
		logger.info("END getAllDepartementsNamesByEntreprise ");

		return depNames;
	}

	@Transactional
	public int deleteEntrepriseById(int entrepriseId) {
		logger.info("START deleteEntrepriseById ");
		Optional<Entreprise> e = entrepriseRepoistory.findById(entrepriseId);

		try {

			logger.trace("Début Test : verifier l'existence du l'entreprise");
			if (e.isPresent()) {
				
				logger.debug("Entrep exitse:" + e.get().getId());

				logger.trace("débbut suppression");
				entrepriseRepoistory.delete(e.get());
				logger.trace("fin suppression");
				logger.trace("FIN Test : verifier l'existence du l'entreprise");
				
				return 1;
			} else {
				logger.trace("Entrep n'exitse pas");
				logger.trace("FIN Test : verifier l'existence du l'entrep");
				return -1;
			}

		} catch (Exception err) {
			logger.error(errorText + err);

		}
		if(e.isPresent()) {
			logger.debug("Entrep supprimée:" + e.get().getId());
		}
	
		logger.info("END deleteEntrepriseById ");

		return 0;

	}

	@Transactional
	public int deleteDepartementById(int depId) {
		logger.info("START deleteDepartementById ");
		Optional<Departement> d = deptRepoistory.findById(depId);

		try {
			logger.trace("Début Test : verifier l'existence du lDepartement");

			if (d.isPresent()) {
				logger.debug("Departement exitse:" + d.get().getId());

				logger.trace("débbut suppression");
				deptRepoistory.delete(d.get());
				logger.trace("fin suppression");
				logger.trace("FIN Test : verifier l'existence du Departement");
				return 1;
			} else {

				logger.trace("Departement n'exitse pas");
				logger.trace("FIN Test : verifier l'existence du Departement");
				return -1;
			}

		} catch (Exception e) {
			logger.error(errorText+ e);

		}
		if(d.isPresent()) {
			logger.debug("Departement suprimée:" + d.get().getId());
		}
		logger.info("END deleteDepartementById ");
		return 0;

	}

	public Entreprise getEntrepriseById(int entrepriseId) {
		logger.info("START getEntrepriseById ");
		try {
			Optional<Entreprise> e = entrepriseRepoistory.findById(entrepriseId);
			logger.trace("Début Test : verifier l'existence du l'entrep");
			if (e.isPresent()) {

				logger.debug("Entreprise exitse:" + e.get().getId());

				logger.trace("débbut Get");
				return e.get();
			}
			logger.trace("fin Get");
			logger.trace("FIN Test : verifier l'existence du l'entrep");
		} catch (Exception e) {
			logger.error("Erreur: " + e);

		}
		logger.info("END getEntrepriseById ");

		return null;
	}

	public Departement getDepartementById(int departementId) {
		logger.info("START getDepartementById ");
		try {
			Optional<Departement> d = deptRepoistory.findById(departementId);

			logger.trace("Début Test : verifier l'existence du departement");
			if (d.isPresent()) {

				logger.debug("Entreprise exitse:" + d.get().getId());

				logger.trace("débbut Get");
				return d.get();
			}
			logger.trace("fin Get");
			logger.trace("FIN Test : verifier l'existence du departement");
		} catch (Exception e) {
			logger.error(errorText+ e);

		}
		logger.info("END getDepartementById ");

		return null;
	}

}
