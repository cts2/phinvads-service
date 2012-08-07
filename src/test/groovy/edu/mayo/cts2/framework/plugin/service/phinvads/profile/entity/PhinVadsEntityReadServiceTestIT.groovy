package edu.mayo.cts2.framework.plugin.service.phinvads.profile.entity

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Before
import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.phinvads.namespace.NamespaceResolutionService
import edu.mayo.cts2.framework.plugin.service.phinvads.test.AbstractTestBase
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId


class PhinVadsEntityReadServiceTestIT extends AbstractTestBase {

	@Resource
	def PhinVadsEntityReadService service

	def marshaller = new DelegatingMarshaller()

	@Before
	void setUpNs() {
		service.namespaceResolutionService = {
			prefixToUri: {ns -> ns}
		} as NamespaceResolutionService
	}
	
	@Test
	void TestSetUp() {
		assertNotNull service
	}
}
