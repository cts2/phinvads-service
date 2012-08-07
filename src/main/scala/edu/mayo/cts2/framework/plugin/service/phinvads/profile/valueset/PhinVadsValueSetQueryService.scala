package edu.mayo.cts2.framework.plugin.service.phinvads.profile.valueset

import scala.collection.JavaConversions._
import scala.collection.JavaConversions._
import org.springframework.stereotype.Component
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.ResourceDescription
import edu.mayo.cts2.framework.model.core.SortCriteria
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.framework.plugin.service.phinvads.dao.PhinVadsDao
import edu.mayo.cts2.framework.plugin.service.phinvads.profile.AbstractService
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService
import gov.cdc.vocab.service.bean.ValueSet
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.core.EntryDescription
import java.nio.charset.Charset
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.StringEscapeUtils
import edu.mayo.cts2.framework.plugin.service.phinvads.util.EncodingUtils
import edu.mayo.cts2.framework.core.url.UrlConstructor
import edu.mayo.cts2.framework.plugin.service.phinvads.transform.ValueSetTransform
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import org.apache.commons.collections.CollectionUtils

@Component
class PhinVadsValueSetQueryService
  extends AbstractService
  with ValueSetQueryService {

  val UTF8_CHARSET = Charset.forName("UTF-8");

  @Resource
  var phinVadsDao: PhinVadsDao = _

  @Resource
  var valueSetTransform: ValueSetTransform = _

  def filterMap = Map(
    StandardModelAttributeReference.RESOURCE_SYNOPSIS.
      getPropertyReference().
      getReferenceTarget().
      getName() -> resourceSynompsisFilter _)

  def getSupportedMatchAlgorithms: java.util.Set[_ <: MatchAlgorithmReference] = {
    Set(StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference())
  }

  def getSupportedSearchReferences: java.util.Set[_ <: PropertyReference] = {
    Set(StandardModelAttributeReference.RESOURCE_SYNOPSIS.getPropertyReference())
  }

  def getSupportedSortReferences: java.util.Set[_ <: PropertyReference] = null

  def getKnownProperties: java.util.Set[PredicateReference] = null

  def getResourceSummaries(query: ValueSetQuery, sort: SortCriteria, page: Page = new Page()): DirectoryResult[ValueSetCatalogEntrySummary] = {
    new DirectoryResult[ValueSetCatalogEntrySummary](
      phinVadsDao.valueSets.slice(page.getStart, page.getEnd).
        filter(queryToFilter(query)).
        map(valueSetTransform.transformPhinVadsValueSetToSummary), true)
  }

  def queryToFilter(query: ValueSetQuery) = {
    if (query == null || CollectionUtils.isEmpty(query.getFilterComponent)) {
      matchAllFilter
    } else {
      val filters = query.getFilterComponent().foldLeft(Seq[(ValueSet => Boolean)]())(
        (list, filterComponent) => {
          list ++ Seq[(ValueSet => Boolean)](resourceSynompsisFilter(filterComponent.getMatchValue()))
        })

      filters.reduce((f1, f2) => (
        (valueSet: ValueSet) => {
          f1(valueSet) || f2(valueSet)
        }: Boolean))
    }
  }: (ValueSet => Boolean)

  def matchAllFilter = (valueSet: ValueSet) => {
    true
  }: Boolean

  def resourceSynompsisFilter(matchText: String) = (valueSet: ValueSet) => {
    StringUtils.contains(valueSet.getDefinitionText(), matchText)
  }: Boolean

  def getResourceList(p1: ValueSetQuery, p2: SortCriteria, p3: Page): DirectoryResult[ValueSetCatalogEntry] = null

  def count(p1: ValueSetQuery): Int = 0

}