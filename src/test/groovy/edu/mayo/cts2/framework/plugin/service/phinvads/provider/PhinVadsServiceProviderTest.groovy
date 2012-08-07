package edu.mayo.cts2.framework.plugin.service.phinvads.provider

import javax.annotation.Resource

import static org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration("/test-phinvads-context.xml")
class PhinVadsServiceProviderTest {

	@Resource
	def PhinVadsServiceProvider provider

	@Test
	void TestSetUp() {
		assertNotNull provider
	}

	@Test
	void TestGetEntityRead() {
		assertNotNull provider.getService(EntityDescriptionReadService.class)
	}

}
