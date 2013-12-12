package fi.vm.sade.osoitepalvelu.reitit;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioDto;
import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioOid;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/test-application-context.xml")
public class OrganisaatioReittiTest {
	
	@Autowired
	private OrganisaatioReitti organisaatioReitti;
	
	@Test
	public void testHaeOrganisaatioOidilla() {
		Locale locale = new Locale("fi", "FI");
		String organisaationNimi = "Mainingin koulu";
		String oid = "1.2.246.562.10.895185029210";		// Testataan Mainingin koululla
		OrganisaatioDto organisaatio = organisaatioReitti.haeOrganisaatioOidilla(new OrganisaatioOid(oid));		
		Assert.assertNotNull(organisaatio);
		Assert.assertTrue(organisaatio.getOid().getValue().equals(oid));	// OID on oltava sama
		
		String haetunOrganisaationNimi = organisaatio.getNimiLokaalille(locale);
		Assert.assertNotNull(haetunOrganisaationNimi);
		Assert.assertTrue(haetunOrganisaationNimi.equals(organisaationNimi));
	}
}
