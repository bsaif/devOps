package tn.esprit.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.services.IEntrepriseService;

import java.util.List;

import org.junit.Assert;
import tn.esprit.spring.entities.Departement;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntrepriseTest {
	@Autowired
	private IEntrepriseService serviceEntreprise;


	@Test
	@Order(1)
	public void testAjouterEntreprise() {
		Entreprise ent = new Entreprise("SaidiJam Mat", "Agriculture");
		int id = serviceEntreprise.ajouterEntreprise(ent);
		Assert.assertNotNull(serviceEntreprise.getEntrepriseById(id));
	}

	@Test
	@Order(2)
	public void testAjouterDepartment() {
		Departement dep = new Departement("Agriculture");
		int id = serviceEntreprise.ajouterDepartement(dep);
		Assert.assertNotNull(serviceEntreprise.getAllDepartementsNamesByEntreprise(id));
	}
	
	@Test
	@Order(3)
	public void testAffecterDepartementAEntreprise() {
		Entreprise ent = new Entreprise("Espritt", "Education");
		int idEntrep = serviceEntreprise.ajouterEntreprise(ent);

		Departement dep = new Departement("Web");
		int idDep = serviceEntreprise.ajouterDepartement(dep);

		Assert.assertNotEquals(idDep,idEntrep);
	}
	
	@Test
	@Order(4)
	public void testGetAllDepartementsNamesByEntreprise() {
		Entreprise ent = new Entreprise("Espritt", "Education");
		List<String> list = serviceEntreprise.getAllDepartementsNamesByEntreprise(132);
		Assert.assertNotNull(list);
	}
	
	@Test
	@Order(5)
	public void testgetEntrepriseById() {
		Entreprise ent = new Entreprise("Soc", "DEV");
		int id = serviceEntreprise.ajouterEntreprise(ent);
		
		Entreprise e1 = serviceEntreprise.getEntrepriseById(id);
		Assert.assertNotNull(e1);

		Entreprise e2 = serviceEntreprise.getEntrepriseById(213232);
		Assert.assertNull(e2);
	}
	
	
	

}
