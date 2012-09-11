package edu.mayo.cts2.framework.plugin.service.phinvads.transform

import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.framework.model.core.EntryDescription
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import gov.cdc.vocab.service.bean.ValueSet
import org.apache.commons.lang.StringUtils
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.core.url.UrlConstructor
import edu.mayo.cts2.framework.plugin.service.phinvads.util.EncodingUtils
import javax.annotation.Resource
import org.springframework.stereotype.Component
import gov.cdc.vocab.service.bean.ValueSetConcept
import edu.mayo.cts2.framework.model.core.EntitySynopsis
import edu.mayo.cts2.framework.plugin.service.phinvads.dao.PhinVadsDao
import gov.cdc.vocab.service.bean.CodeSystemConcept
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import gov.cdc.vocab.service.bean.CodeSystem
import edu.mayo.cts2.framework.plugin.service.phinvads.dao.CodeSystemId
import edu.mayo.cts2.framework.plugin.service.phinvads.namespace.NamespaceResolutionService

@Component
class EntityTransform {

  @Resource
  var urlConstructor: UrlConstructor = _
  
  @Resource
  var namespaceResolutionService: NamespaceResolutionService = _

  @Resource
  var phinVadsDao: PhinVadsDao = _

  def transformPhinVadsCodeSystemConceptToEntity = (phinvadcsc: CodeSystemConcept) => {
    val entity: NamedEntityDescription = new NamedEntityDescription()

    val name = new ScopedEntityName()
    name.setName(phinvadcsc.getConceptCode());
    val cs = phinVadsDao.codeSystemIdMaps.codeSystemIdByUri(phinvadcsc.getCodeSystemOid())
    name.setNamespace(cs.name)

    val nsUri = namespaceResolutionService.prefixToUri(StringUtils.removeStart(cs.name, "PH_"))
    if(nsUri != null){
      entity.setAbout(nsUri + phinvadcsc.getConceptCode )
    } else {
      entity.setAbout("uri:urn:" + phinvadcsc.getId())
    }

    entity.setEntityID(name)

    val entityType = new URIAndEntityName()
    entityType.setName("Class")
    entityType.setNamespace("owl")
    entityType.setUri("http://www.w3.org/2002/07/owl#Class")

    entity.addEntityType(entityType)

    entity.setDescribingCodeSystemVersion(
      buildCodeSystemVersionReference(cs))

    val description = new EntityDescription()
    description.setNamedEntity(entity)

    description
  }: EntityDescription

  def buildCodeSystemVersionReference(codeSystem: CodeSystemId) = {
    val ref = new CodeSystemVersionReference
    val versionRef = new NameAndMeaningReference
    val codeSystemRef = new CodeSystemReference

    versionRef.setContent(codeSystem.codeSystemVersionName)
    versionRef.setUri(codeSystem.oid)

    codeSystemRef.setContent(codeSystem.name)
    codeSystemRef.setUri(codeSystem.oid)
    
    ref.setCodeSystem(codeSystemRef)
    ref.setVersion(versionRef)

    ref
  }

}