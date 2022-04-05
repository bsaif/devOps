package tn.esprit.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.services.IEntrepriseService;
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

}
