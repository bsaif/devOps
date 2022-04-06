package tn.esprit.spring;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Employe;

import tn.esprit.spring.entities.Role;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;
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
	
	@Autowired 
	ContratRepository ContratRepo;
	
	@Autowired
	EntrepriseRepository  enterpriseRepositor;
	
	@Test
	@Order(1)
	public void testAjoutEmploye() {
		Employe employe = new Employe("ghaith","khiari","ghaith.khiari@esprit.tn",true,Role.INGENIEUR);
		int id = employeService.ajouterEmploye(employe);
		Assert.assertNotNull(employeService.getEmployePrenomById(id));
	}
	@Test
	@Order(2)
	public void testDeleteEmployeById()
	{
		Employe employe = new Employe("ghaith","khiari","ghaith.khiari@esprit.tn",true,Role.INGENIEUR);
		int id = employeService.ajouterEmploye(employe);
		employeService.deleteEmployeById(id);
		Assert.assertNull(employeService.getEmployePrenomById(id));
	}
	
	@Test
	@Order(3)
	public void testMettreAjourEmailByEmployeId()
	{
		String newMail = "updated_mail@mail.com";
        employeService.mettreAjourEmailByEmployeIdJPQL(newMail, employeService.getAllEmployes().get(0).getId());
        String updatedMail = employeService.getAllEmployes().get(0).getEmail();
        Assert.assertEquals("check updated mail ", newMail, updatedMail);}
	
	@Test
	@Order(4)
   public void deleteAllContratJPQL() {
        employeService.deleteAllContratJPQL();
        long count = ContratRepo.count();
        Assert.assertEquals("deleteAllContractJPQL... ", 0, count);
    }
	/*
	@Test
	@Order(5)
   public void getSalaireByEmployeIdJPQL() throws ParseException {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy/MM/dd");
        Date date = DateFor.parse("2021/11/25");
        Contrat contract_1 = new Contrat(111, date, "CIVP", 1200);
        contract_1.setEmploye(employeService.getAllEmployes().get(0));
        ContratRepo.save(contract_1);
        float salary = employeService.getSalaireByEmployeIdJPQL(employeService.getAllEmployes().get(0).getId());
        MatcherAssert.assertThat("getSalaireByEmployeIdJPQL... ", salary, Matchers.equalTo(contract_1.getSalaire()));
    }
    */
	
	@Test
	@Order(6)
   public void getNombreEmployeJPQL() {
		Employe employee_1 = new Employe("samir", "soupap", "smayer@mail.com", true, Role.INGENIEUR);
        int count = employeService.getNombreEmployeJPQL();
        if (count == 0)
            employeService.ajouterEmploye(employee_1);
        Assert.assertNotEquals(0, count);
    }
	
	
	
}
