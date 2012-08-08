package edu.mayo.cts2.framework.plugin.service.phinvads.dao

import java.lang.Override
import scala.collection.JavaConversions._
import org.springframework.stereotype.Component
import com.caucho.hessian.client.HessianProxyFactory
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService
import gov.cdc.vocab.service.VocabService
import gov.cdc.vocab.service.bean.ValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetSummary
import gov.cdc.vocab.service.bean.ValueSetVersion
import org.springframework.beans.factory.InitializingBean
import com.caucho.hessian.io.SerializerFactory
import javax.annotation.Resource
import gov.cdc.vocab.service.bean.CodeSystem

@Component
class PhinVadsDao extends InitializingBean {

  val phinvadsServiceUrl = "http://phinvads.cdc.gov/vocabService/v2"

  @Resource
  var serializerFactory: SerializerFactory = _

  var vocabService: VocabService = _

  var valueSets: Seq[ValueSet] = _

  var codeSystems: Seq[CodeSystem] = _

  var valueSetVersions: Seq[ValueSetVersion] = _

  var valueSetNameToOidMap: Map[String, String] = _

  var codeSystemNameToOidMap: Map[String, String] = _

  var codeSystemIdMaps: CodeSystemIdMaps = _

  var valueSetMaps: ValueSetMaps = _

  class CodeSystemIdMaps(
    val codeSystemIdByName: Map[String, CodeSystemId] = Map(),
    val codeSystemIdByUri: Map[String, CodeSystemId] = Map(),
    val codeSystemIdByVersionName: Map[String, CodeSystemId] = Map(),
    val codeSystemIdByNameAndVersion: Map[(String, String), CodeSystemId] = Map())

  class CodeSystemMaps(
    val codeSystemByName: Map[String, CodeSystem] = Map(),
    val codeSystemByUri: Map[String, CodeSystem] = Map())

  class ValueSetMaps(
    val valueSetByName: Map[String, ValueSet] = Map(),
    val valueSetByUri: Map[String, ValueSet] = Map())

  def mapValueSetIds = (map: ValueSetMaps, valueSet: ValueSet) => {
    new ValueSetMaps(
      map.valueSetByName ++ Map(valueSet.getCode() -> valueSet),
      map.valueSetByUri ++ Map(valueSet.getOid() -> valueSet))
  }: ValueSetMaps

  def mapCodeSystemIds = (map: CodeSystemMaps, cs: CodeSystem) => {
    new CodeSystemMaps(
      map.codeSystemByName ++ Map(cs.getCodeSystemCode() -> cs),
      map.codeSystemByUri ++ Map(cs.getOid() -> cs))
  }: CodeSystemMaps

  private def cacheValueSetMaps(valueSets:Seq[ValueSet]) = {
    valueSets.
      foldLeft(new ValueSetMaps())(mapValueSetIds)
  }: ValueSetMaps

  def afterPropertiesSet() {
    vocabService = initPhinVadsClient()
    valueSets = cacheValueSets()
    valueSetVersions = cacheValueSetVersions()
    valueSetNameToOidMap = cacheValueSetOids(valueSets)
    valueSetMaps = cacheValueSetMaps(valueSets)
    
    codeSystems = vocabService.getAllCodeSystems().getCodeSystems()
    codeSystemIdMaps = cacheCodeSystemIdMaps(codeSystems)
  }

  private def initPhinVadsClient() = {
    val factory = new HessianProxyFactory();
    factory.setSerializerFactory(serializerFactory)
    factory.create(classOf[VocabService], phinvadsServiceUrl).asInstanceOf[VocabService]
  }

  def getValueSetOid(valueSetName: String): String = {
    valueSetNameToOidMap.getOrElse(valueSetName, return null)
  }

  def getValueSets[T](transform: (ValueSet => T), slice: Option[Slice] = None): Seq[T] = {
    val list =
      if (slice.isDefined) {
        valueSets.slice(slice.get.start, slice.get.end)
      } else {
        valueSets
      }

    list.map(transform)
  }

  def getValueSetVersions[T](transform: (ValueSetVersion => T), slice: Option[Slice] = None): Seq[T] = {
    val list =
      if (slice.isDefined) {
        valueSetVersions.slice(slice.get.start, slice.get.end)
      } else {
        valueSetVersions
      }

    list.map(transform)
  }

  private def cacheValueSets() = {
    vocabService.getAllValueSets().getValueSets()
  }

  private def cacheValueSetVersions() = {
    vocabService.getAllValueSetVersions().getValueSetVersions()
  }

  private def cacheValueSetOids(valueSets:Seq[ValueSet]) = {
    valueSets.foldLeft(Map[String, String]())(
      (map, valueSet) => {
        map ++ Map(valueSet.getCode() -> valueSet.getOid())
      })
  }

  private def cacheCodeSystemIdMaps(codeSystems:Seq[CodeSystem]) = {
    codeSystems.foldLeft(new CodeSystemIdMaps())(
      (map, codeSystem) => {
        val versionName = getCodeSystemVersionName(codeSystem)

        val csId = new CodeSystemId(
            codeSystem.getCodeSystemCode(),
            versionName,
            codeSystem.getVersion,
            codeSystem.getOid)
        new CodeSystemIdMaps(
          map.codeSystemIdByName ++ Map(codeSystem.getCodeSystemCode() -> csId),
          map.codeSystemIdByUri ++ Map(codeSystem.getOid() -> csId),
          map.codeSystemIdByVersionName ++ Map(versionName -> csId),
          map.codeSystemIdByNameAndVersion ++ Map((codeSystem.getCodeSystemCode(), codeSystem.getVersion()) -> csId))
      })
  }

  def getCodeSystemVersionName(codeSystem: CodeSystem): String = {
    val versionName =  codeSystem.getCodeSystemCode() + "-" + codeSystem.getVersion()
    
    versionName
  }
}