package tn.esprit.spring;




import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.services.EmployeServiceImpl;
import tn.esprit.spring.services.EntrepriseServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class EmployeTest {
	
	@Autowired
	EmployeServiceImpl employeService;
	
	@Autowired
	EntrepriseServiceImpl entrepriseService;
	
	@Autowired
	DepartementRepository departementRepository;
	
	
	@Test
	@Order(1)
	public void testAjoutEmploye() {
		Employe employe = new Employe("ghaith","khiari","ghaith.khiari@esprit.tn",true,Role.INGENIEUR);
		int id = employeService.ajouterEmploye(employe);
		Assert.assertNotNull(employeService.getEmployePrenomById(id));
	}
	/*@Test
	@Order(2)
	public void testDeleteEmployeById()
	{
		
		Employe employe = new Employe("ghaith","khiari","ghaith.khiari@esprit.tn",true,Role.INGENIEUR);
		int id = employeService.ajouterEmploye(employe);
		employeService.deleteEmployeById(id);
		Assert.assertNull(employeService.getEmployePrenomById(id));
	}*/
	
	/*@Test
	@Order(2)
	public void testMettreAjourEmailByEmployeId()
	{
		Employe employe = new Employe("eee","aaaaaaaa","chadi.znina@esprit.tn",true,Role.INGENIEUR);
		int id = employeService.ajouterEmploye(employe);
		employeService.mettreAjourEmailByEmployeId("olay@esprit.tn", id);
		Assert.assertTrue("not equal", employeService.getEmployePrenomById(id).getEmail().equals("olay@esprit.tn"));
	}
	
	@Transactional
	@Test
	@Order(2)
	public void testaffecterEmployeADepartement()
	{
		Employe employe = new Employe("bbbbb","bbbbbbbb","chadi.znina@esprit.tn",true,Role.INGENIEUR);
		int idEmploye = employeService.ajouterEmploye(employe);
		Departement departement = new Departement("chadi's Departement");
		int idDepartement = entrepriseService.ajouterDepartement(departement);
		employeService.affecterEmployeADepartement(idEmploye, idDepartement);
		Assert.assertTrue(employeService.getdeptById(idDepartement).getEmployes().indexOf(employe)!= -1);
	}
	
	@Transactional
	@Test
	@Order(4)
	public void testdesaffecterEmployeDuDepartemen()
	{
		Employe employe = new Employe("aziz","sahnoun","sahnoun.aziz@esprit.tn",true,Role.INGENIEUR);
		int idEmploye = employeService.ajouterEmploye(employe);
		Departement departement = new Departement("chadi's Departement");
		int idDepartement = entrepriseService.ajouterDepartement(departement);
		employeService.affecterEmployeADepartement(idEmploye, idDepartement);
		employeService.desaffecterEmployeDuDepartement(idEmploye, idDepartement);
		Assert.assertTrue(employeService.getdeptById(idDepartement).getEmployes().indexOf(employe) == -1);
	}
	
	@Test
	@Order(5)
	public void testAjouterContrat()
	{
		Date date = new Date(System.currentTimeMillis());
		Contrat contrat = new Contrat(date,"CDI",2000);
		int referenceContrat = employeService.ajouterContrat(contrat);
		Assert.assertNotNull(employeService.getContratById(referenceContrat));
	}
	
	@Test
	@Order(6)
	public void testAffecterContratAEmploye()
	{
		Date date = new Date(System.currentTimeMillis());
		Contrat contrat = new Contrat(date,"CDI",2000);
		int referenceContrat = employeService.ajouterContrat(contrat);
		Employe employe = new Employe("Oneil","Shaqil","Shaq.OG@esprit.tn",true,Role.INGENIEUR);
		int idEmploye = employeService.ajouterEmploye(employe);
		employeService.affecterContratAEmploye(referenceContrat, idEmploye);
		Assert.assertNotNull(employeService.getContratById(referenceContrat).getEmploye());
	}
	*/
	
	
}
