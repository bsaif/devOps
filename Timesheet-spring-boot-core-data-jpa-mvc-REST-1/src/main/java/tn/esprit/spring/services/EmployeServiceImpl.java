package tn.esprit.spring.services;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeServiceImpl implements IEmployeService {

   // private static final Logger log = Logger.getLogger(EmployeServiceImpl.class);

    EmployeRepository employeRepository;
    DepartementRepository deptRepoistory;
    ContratRepository contratRepoistory;
    TimesheetRepository timesheetRepository;

    @Autowired
    public EmployeServiceImpl(EmployeRepository employeRepository, DepartementRepository deptRepoistory, ContratRepository contratRepoistory, TimesheetRepository timesheetRepository) {
        this.employeRepository = employeRepository;
        this.deptRepoistory = deptRepoistory;
        this.contratRepoistory = contratRepoistory;
        this.timesheetRepository = timesheetRepository;
    }

    
    public int ajouterEmploye(Employe employe) {
    	log.info("Dans ajouterEmploye() : ");
		log.debug("Ajout de l'emplyé " + employe);
		try {
			employeRepository.save(employe);
			log.debug("Ajout Employé fait !!!");
			log.info("Sortie de ajouterEmployé sans erreurs");
			
		} catch (Exception e) {
			log.error("Erreure dans ajouterEmploye() : " + e);
    }

		return employe.getId();
    }

    public void mettreAjourEmailByEmployeId(String email, int employeId) {
    	   log.info("** start  mettreAjourEmailByEmployeId : ");
	        try {
	        	 Optional<Employe> employe = employeRepository.findById(employeId);
	             if(employe.isPresent())
	             {
	                 employe.get().setEmail(email);
	                 employeRepository.save(employe.get());
	             }
	            log.debug(" -- N° 2 : the employee :" + employeId +" updated successfully " );
	            log.info("** end  mettreAjourEmailByEmployeId without error ");
	        }catch (Exception e ){
	            log.info("** end  mettreAjourEmailByEmployeId with error : "+e);
	        }
    }

    public void affecterEmployeADepartement(int employeId, int depId) {
		log.info("Dans affecterEmployeADepartement() : ");
    	log.debug("Affectation du Departement " + depId + " a l'Employe " + employeId);
    	Optional<Departement> depManagedEntity = deptRepoistory.findById(depId);
        Optional<Employe> employeManagedEntity = employeRepository.findById(employeId);
        if (depManagedEntity.isPresent() && employeManagedEntity.isPresent()) {
        	
        	   if (depManagedEntity.get().getEmployes() == null) {
                   List<Employe> employes = new ArrayList<>();
                   try {
                	   employes.add(employeManagedEntity.get());
                       depManagedEntity.get().setEmployes(employes);
	       				log.debug("Affectation terminée !!!");  
	       				log.info("Sortie de affecterEmployeADepartement sans erreurs");
       				
       			} catch (Exception e) {
       				log.error("erreur Dans affecterEmployeADepartement() : "+ e);
       			}
                  
               } else {
            		try {
        				depManagedEntity.get().getEmployes().add(employeManagedEntity.get());
        				
        			}catch(Exception exp){
        				log.error("erreur Dans affecterEmployeADepartement() : "+ exp);
        			}
        			
               }
        	
        }
    }

    @Transactional
    public void desaffecterEmployeDuDepartement(int employeId, int depId) {
    	log.info("Dans desaffecterEmployeDuDepartement() : ");
   	 	Optional<Departement> dep = deptRepoistory.findById(depId);
   	 	if(dep.isPresent()) {
		  	int employeNb = dep.get().getEmployes().size();
		  	 for (int index = 0; index < employeNb; index++) {
		  		if (dep.get().getEmployes().get(index).getId() == employeId) {
					try {
						 dep.get().getEmployes().remove(index);
		                    break;
					}catch(Exception exp){
						log.error("Dans desaffecterEmployeDuDepartement() : "+ exp);
					}
				}
			}
   	 	}
    }

    public int ajouterContrat(Contrat contrat) {
    	log.info("Dans ajouterContrat() : ");
		log.debug("Ajout du contrat " + contrat);
		
		try {
			contratRepoistory.save(contrat);
			
			log.debug("Ajout Contrat fait !!!");
			log.info("Sortie de ajouterContrat sans erreurs");
			
		} catch (Exception e) {
			log.error("Erreure dans ajouterContrat() : " + e);
		}
		
		return contrat.getReference();
    }

    public void affecterContratAEmploye(int contratId, int employeId) {
    	log.info("Dans affecterContratAEmploye() : ");
		log.debug("affecter du contrat " + contratId + "à l'employé" + employeId );
        Optional<Contrat> contratManagedEntity = contratRepoistory.findById(contratId);
        Optional<Employe> employeManagedEntity = employeRepository.findById(employeId);
        
        if(contratManagedEntity.isPresent() && employeManagedEntity.isPresent())
        {
        	try {
    			contratManagedEntity.get().setEmploye(employeManagedEntity.get());
                contratRepoistory.save(contratManagedEntity.get());
    			log.debug("affectation du Contrat fait !!!");
    			log.info("Sortie de affecterContratAEmploye sans erreurs");
    			
    		} catch (Exception e) {
    			log.error("Erreure dans affecterContratAEmploye() : " + e);
    		}
        }
    }

    public String getEmployePrenomById(int employeId) {
        Optional<Employe> employeManagedEntity = employeRepository.findById(employeId);
        return employeManagedEntity.map(Employe::getPrenom).orElse(null);
    }

    public void deleteEmployeById(int employeId) {
        Optional<Employe> employe = employeRepository.findById(employeId);
        if(employe.isPresent()){
            //Desaffecter l'employe de tous les departements
            //c'est le bout master qui permet de mettre a jour
            //la table d'association
            for (Departement dep : employe.get().getDepartements()) {
                dep.getEmployes().remove(employe.get());
            }

            employeRepository.delete(employe.get());
        }

    }

    public void deleteContratById(int contratId) {
    	log.info("Dans deleteEmployeById() : ");
		log.debug("supperssion de l employé " + contratId);
        Optional<Contrat> contratManagedEntity = contratRepoistory.findById(contratId);
		try {
			contratManagedEntity.ifPresent(contrat -> contratRepoistory.delete(contrat));
			log.debug("suppression faite !!!");
			log.info("Sortie de deleteContratById sans erreurs");
		} catch (Exception e) {
			log.error("Erreure dans deleteContratById() : " + e);
		}
    }

    
    public int getNombreEmployeJPQL() {
        log.info("** start  getNumberEmployeeJPQL : ");
        int count = 0;
        try {
            count = employeRepository.countemp();
            log.debug(" -- the numbers of employee is :"+count);
            log.info("** end  getNumberEmployeeJPQL without error ");
        }catch (Exception e ){
            log.info("** end  getNumberEmployeeJPQL with error : "+e);
        }
        return count;
    }

    public List<String> getAllEmployeNamesJPQL() {
        log.info("** start  getAllEmployeeNamesJPQL : ");
        List<String> names = null;
        try {
            names = employeRepository.employeNames();
            log.debug(" -- N° 1 : Names of employee : "+names);
            log.info("** end  getAllEmployeeNamesJPQL without error ");
        }catch(Exception e ){
            log.info("** end  getAllEmployeeNamesJPQL with error : "+e);
        }
        return names;
    }

    public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
        log.info("** start  getAllEmployeeByEnterprise : ");
        List<Employe> employees = null;
        try {
            employees = employeRepository.getAllEmployeByEntreprisec(entreprise);
            log.debug(" -- N° 1 : the number of employees of the enterprise :" + entreprise.getName() +" .. is :" + employees.size() );
            List <String> empNames = employees.stream().map(Employe::getNom).collect(Collectors.toList());
            log.debug(" -- N° 2 : their names are :" + empNames );
            log.info("** end  getAllEmployeeByEnterprise without error");
        }catch(Exception e )
        {
            log.info("** end  getAllEmployeeByEnterprise with error : "+e);
        }
        return employees;
    }

    public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
        log.info("** start  UpdateEmailByEmployeeIdJPQL : ");
        try {
            log.debug(" -- N° 1 : the employee :" + employeId +" will have a new mail :" + email );
            employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);
            log.debug(" -- N° 2 : the employee :" + employeId +" updated successfully " );
            log.info("** end  UpdateEmailByEmployeeIdJPQL without error ");
        }catch (Exception e ){
            log.info("** end  UpdateEmailByEmployeeIdJPQL with error : "+e);
        }

    }

    public void deleteAllContratJPQL() {
        log.info("** start  deleteAllContactJPQL : ");
        try {
            employeRepository.deleteAllContratJPQL();
            log.info("** end  deleteAllContactJPQL without error");
        }catch(Exception e ){
            log.error("** end  deleteAllContactJPQL with error "+e);
        }
    }

    public float getSalaireByEmployeIdJPQL(int employeId) {

        log.info("** start  getSalaryByEmployeeIdJPQL : ");
        log.debug(" -- N° 1 : the employee :"+employeId  );
        float salaire = 0;
        try {
            salaire = employeRepository.getSalaireByEmployeIdJPQL(employeId);
            log.debug(" -- N° 2 : his salary is :"+salaire  );
            log.info("** end  getSalaryByEmployeeIdJPQL  without error ");
        } catch (Exception e) {
            log.error("** end  getSalaryByEmployeeIdJPQL  with error "+e);
        }

        return salaire;
    }

    public Double getSalaireMoyenByDepartementId(int departementId) {
        log.info("** start  getAverageSalaryByDepartmentId : ");
        log.debug(" -- N° 1 : the department ID :"+departementId );
        double averageSalary=0;
        try {
            averageSalary = employeRepository.getSalaireMoyenByDepartementId(departementId);
            log.debug(" -- N° 2 : average salary :"+averageSalary );
            log.info("** end  getAverageSalaryByDepartmentId  without error ");
        }catch (Exception e )
        {
            log.info("** end  getAverageSalaryByDepartmentId  with error : "+e);
        }
        return averageSalary;
    }

    public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
                                                         Date dateFin) {
        log.info("** start  getTimeSheetsByMissionAndDate : ");
        List<Timesheet> timesheetList = null;
        try {
            log.debug(" -- N° 1 : the information of timeSheet are :\nemployee : " +employe.getId()+"\nmission : "+mission.getName()+"\ndate : "+dateDebut+" -> "+dateFin );
            timesheetList = timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
            log.debug(" -- N° 2 : time sheet found successfully .. count  : " +timesheetList.size() );
            log.info("** end  getTimeSheetsByMissionAndDate without error ");
        }catch (Exception e ){
            log.info("** end  getTimeSheetsByMissionAndDate with error : "+e);
        }
        return timesheetList;
    }
    public List<Employe> getAllEmployes() {
		return (List<Employe>) employeRepository.findAll();
}

}
