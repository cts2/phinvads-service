package edu.mayo.cts2.framework.plugin.service.phinvads.profile.valueset

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test
import static org.junit.Assert.*

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.plugin.service.phinvads.test.AbstractTestBase
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery

class PhinVadsValueSetQueryServiceTestIT extends AbstractTestBase {

	@Resource
	def PhinVadsValueSetQueryService service

	def marshaller = new DelegatingMarshaller()


	@Test
	void TestSetUp() {
		assertNotNull service
	}

	@Test
	void TestGetSummariesNotNull() {
		assertNotNull service.getResourceSummaries(null as ValueSetQuery, null, new Page())
	}

    @Test
    void TestGetSummariesSize() {
        assertEquals 50, service.getResourceSummaries(null as ValueSetQuery, null, new Page()).entries.size()
    }

	@Test
	void TestGetSummariesEntriesValidXml() {
		def entries = service.getResourceSummaries(null as ValueSetQuery, null, new Page()).entries
		
		assertTrue entries.size() > 1
		
		entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
}
