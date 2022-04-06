package tn.esprit.spring.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IEntrepriseService;
import tn.esprit.spring.services.ITimesheetService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ITimesheetServiceTest {
	@Autowired
	private ITimesheetService sTimesheet;
	@Autowired
	private IEmployeService sEmploye;
	@Autowired
	private IEntrepriseService sEntreprise;
	

	@Test
	public void testAjouterMission() {
		Mission mission = new Mission("Inspection", "sur terrain");
		int id = sTimesheet.ajouterMission((mission));

		assertNotNull(sTimesheet.getMissionById(id));
	}

	@Test
	public void testGetMissionById() {

		Mission mission1 = new Mission("Inspection", "sur terrain");
		int id = sTimesheet.ajouterMission(mission1);

		Mission mission2 = sTimesheet.getMissionById(id);
		assertNotNull(mission2);

		Mission mission3 = sTimesheet.getMissionById(213232);
		assertNull(mission3);
	};

	@Test
	public void testAffecterMissionADepartement() {
		Mission mission = new Mission("Inspection", "sur terrain");
		int idMission = sTimesheet.ajouterMission(mission);

		Departement departement = new Departement("Info");
		int idDepartement = sEntreprise.ajouterDepartement(departement);

		int idDepAffecte = sTimesheet.affecterMissionADepartement(idMission, idDepartement);
		assertEquals(idDepAffecte, idDepartement);

	}

	@Test
	public void testValiderTimesheet() {
		Mission mission = new Mission("Inspection", "sur terrain");
		sTimesheet.ajouterMission(mission);

		Employe ingenieur = new Employe("Saidi", "ahmed", "Ahmed.Saidi@esprit.tn", true, Role.INGENIEUR);
		Employe chef = new Employe("Saidi", "ahmed", "Ahmed.Saidi@esprit.tn", true, Role.CHEF_DEPARTEMENT);

		Date dateDebut = new Date(System.currentTimeMillis());
		Date dateFin = new Date(System.currentTimeMillis());

		sEmploye.ajouterEmploye(ingenieur);
		sEmploye.ajouterEmploye(chef);

		assertEquals(0,				sTimesheet.validerTimesheet(mission.getId(), ingenieur.getId(), dateDebut, dateFin, chef.getId()));
		assertEquals(-1,				sTimesheet.validerTimesheet(mission.getId(), ingenieur.getId(), dateDebut, dateFin, ingenieur.getId()));

	}
}
