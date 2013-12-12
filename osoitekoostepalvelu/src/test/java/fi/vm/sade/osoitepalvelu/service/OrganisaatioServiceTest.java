package fi.vm.sade.osoitepalvelu.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.vm.sade.osoitepalvelu.service.dto.OrganisaatioOid;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/test-application-context.xml")
public class OrganisaatioServiceTest {

	@Autowired
	private OrganisaatioService organisaatioService;
	
	@Test
	public void testHaeKaikkiOrganisaatioOidid() {
		List<OrganisaatioOid> organisaatiot = organisaatioService.findAllOrganizationIds();
		Assert.assertNotNull(organisaatiot);
		Assert.assertTrue(organisaatiot.size() > 0);
	}
}
