package edu.mayo.cts2.framework.plugin.service.phinvads.profile.codesystem

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.phinvads.test.AbstractTestBase


class PhinVadsCodeSystemReadServiceTestIT extends AbstractTestBase {

	@Resource
	def PhinVadsCodeSystemReadService service

	def marshaller = new DelegatingMarshaller()


	@Test
	void TestSetUp() {
		assertNotNull service
	}
}
