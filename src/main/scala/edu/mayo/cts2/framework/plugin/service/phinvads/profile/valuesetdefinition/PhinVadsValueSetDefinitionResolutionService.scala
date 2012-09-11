package edu.mayo.cts2.framework.plugin.service.phinvads.profile.valuesetdefinition

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import org.springframework.stereotype.Component
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core._
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader
import edu.mayo.cts2.framework.plugin.service.phinvads.dao.PhinVadsDao
import edu.mayo.cts2.framework.plugin.service.phinvads.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.phinvads.transform.ValueSetTransform
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import gov.cdc.vocab.service.bean.ValueSetConcept
import javax.annotation.Resource
import gov.cdc.vocab.service.bean.ValueSetVersion
import gov.cdc.vocab.service.bean.ValueSet
import gov.cdc.vocab.service.dto.input.CodeSystemSearchCriteriaDto

@Component
class PhinVadsValueSetDefinitionResolutionService extends AbstractService with ValueSetDefinitionResolutionService {

  @Resource
  var phinVadsDao: PhinVadsDao = _

  @Resource
  var valueSetTransform: ValueSetTransform = _

  def getSupportedMatchAlgorithms: java.util.Set[_ <: MatchAlgorithmReference] = null

  def getSupportedSearchReferences: java.util.Set[_ <: PropertyReference] = null

  def getSupportedSortReferences: java.util.Set[_ <: PropertyReference] = null

  def getKnownProperties: java.util.Set[PredicateReference] = null

  def resolveDefinition(
    id: ValueSetDefinitionReadId,
    codeSystemVersions: java.util.Set[NameOrURI],
    codeSystemVersionTag: NameOrURI,
    query: ResolvedValueSetResolutionEntityQuery,
    sort: SortCriteria,
    readContext: ResolvedReadContext,
    page: Page): ResolvedValueSetResult[EntitySynopsis] = {

    val valueSetName = id.getValueSet().getName()

    val valueSet = phinVadsDao.valueSetMaps.valueSetByName(valueSetName);

    val valueSetOid = valueSet.getOid

    val valueSetVersion =
      phinVadsDao.vocabService.getValueSetVersionByValueSetOidAndVersionNumber(valueSetOid, null)

    val versionId = valueSetVersion.getValueSetVersion().getId()

    val valueSetConcepts = phinVadsDao.vocabService.
      getValueSetConceptsByValueSetVersionId(versionId, page.getStart + 1, page.getEnd + 1).
      getValueSetConcepts().asScala

    val synopsis: Seq[EntitySynopsis] = valueSetConcepts.map(valueSetTransform.transformValueSetConcept)

    new ResolvedValueSetResult(buildHeader(valueSet, valueSetVersion.getValueSetVersion, valueSetConcepts), synopsis, true)
  }

  private def buildHeader(valueSet:ValueSet, valueSetVersion:ValueSetVersion, concepts:Seq[ValueSetConcept]): ResolvedValueSetHeader = {
    
    val csvRefs = concepts.foldLeft(Set[CodeSystemVersionReference]())( (x,y) => x ++ Set(valueSetConceptToCsvReference(y)) )
    
    val header = new ResolvedValueSetHeader()
    csvRefs.foreach( header.addResolvedUsingCodeSystem(_))
 
    val valueDefSetRef = new ValueSetDefinitionReference()
    val vsr = new ValueSetReference(valueSet.getCode);
    vsr.setUri("urn:oid:" + valueSet.getOid)
    valueDefSetRef.setValueSet(vsr)
    
    val vsdr = new NameAndMeaningReference(valueSet.getCode + "-v" +valueSetVersion.getVersionNumber)
    valueDefSetRef.setValueSetDefinition(vsdr)
    header.setResolutionOf(valueDefSetRef)

    header
  }
  
  private def valueSetConceptToCsvReference(concept:ValueSetConcept) = {
    val ref = new CodeSystemVersionReference()
    val csOid = concept.getCodeSystemOid
    
    val csId = phinVadsDao.codeSystemIdMaps.codeSystemIdByUri(csOid)
    val csName = csId.name
    val csvName = csId.codeSystemVersionName
    
    val csr = new CodeSystemReference(csName)
    csr.setUri("urn:oid:" + csOid)
    
    val csvr = new NameAndMeaningReference(csvName)
    ref.setCodeSystem(csr)
    ref.setVersion(csvr)

    ref
    
  }:CodeSystemVersionReference

  def resolveDefinitionAsEntityDirectory(p1: ValueSetDefinitionReadId, p2: java.util.Set[NameOrURI], p3: NameOrURI, p4: ResolvedValueSetResolutionEntityQuery, p5: SortCriteria, p6: ResolvedReadContext, p7: Page): ResolvedValueSetResult[EntityDirectoryEntry] = null

  def resolveDefinitionAsCompleteSet(p1: ValueSetDefinitionReadId, p2: java.util.Set[NameOrURI], p3: NameOrURI, p4: ResolvedReadContext): ResolvedValueSet = null
}