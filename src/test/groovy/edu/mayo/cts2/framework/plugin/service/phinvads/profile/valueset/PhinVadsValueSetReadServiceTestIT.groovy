package edu.mayo.cts2.framework.plugin.service.phinvads.profile.valueset

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.phinvads.test.AbstractTestBase


class PhinVadsValueSetReadServiceTestIT extends AbstractTestBase {

	@Resource
	def PhinVadsValueSetReadService service

	def marshaller = new DelegatingMarshaller()


	@Test
	void TestSetUp() {
		assertNotNull service
	}

	@Test
	void TestReadByName() {
		assertNotNull service.read(ModelUtils.nameOrUriFromName("PHVS_AddressType_CDC"), null)
	}

	
}
