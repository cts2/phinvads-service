package edu.mayo.cts2.framework.plugin.service.phinvads.profile.entity

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.phinvads.test.AbstractTestBase
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId


class PhinVadsEntityReadServiceTestIT extends AbstractTestBase {

	@Resource
	def PhinVadsEntityReadService service

	def marshaller = new DelegatingMarshaller()

	@Test
	void TestSetUp() {
		assertNotNull service
	}
	
	@Test
	void TestRead() {
		def id = new EntityDescriptionReadId(
			"AGG057",
			 null, 
			 ModelUtils.nameOrUriFromName(
				 "PH_PHINQuestions_CDC-20110907")
			
		)
		assertNotNull service.read(id, null)
	}
	
	@Test
	void TestReadValidXml() {
		def id = new EntityDescriptionReadId(
			"AGG057",
			 null,
			 ModelUtils.nameOrUriFromName(
				 "PH_PHINQuestions_CDC-20110907")
			
		)
		def entity = service.read(id, null)
		
		marshaller.marshal(entity, new StreamResult(new StringWriter()))
	}
}
