package edu.mayo.cts2.framework.plugin.service.phinvads.profile.resolvedvalueset

import edu.mayo.cts2.framework.service.profile.resolvedvalueset.{ResolvedValueSetQuery, ResolvedValueSetQueryService}
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference
import java.util.{Set, List}
import edu.mayo.cts2.framework.model.core._
import javax.annotation.Resource
import org.springframework.stereotype.Component
import gov.cdc.vocab.service.bean.{ValueSetVersion, ValueSet}
import edu.mayo.cts2.framework.model.valuesetdefinition.{ResolvedValueSetHeader, ResolvedValueSetDirectoryEntry}
import edu.mayo.cts2.framework.plugin.service.phinvads.dao.PhinVadsDao
import edu.mayo.cts2.framework.plugin.service.phinvads.profile.AbstractService

@Component
class PhinVadsResolvedValueSetQueryService extends AbstractService with ResolvedValueSetQueryService {

  @Resource
  var phinVadsDao: PhinVadsDao = _

  def getSupportedMatchAlgorithms: Set[_ <: MatchAlgorithmReference] = null

  def getSupportedSearchReferences: Set[_ <: PropertyReference] = null

  def getSupportedSortReferences: Set[_ <: PropertyReference] = null

  def getKnownProperties: Set[PredicateReference] = null

  def getResourceSummaries(p1: ResolvedValueSetQuery, p2: SortCriteria, p3: Page): DirectoryResult[ResolvedValueSetDirectoryEntry] = {
    throw new UnsupportedOperationException()
  }

  def count(p1: ResolvedValueSetQuery): Int = 0

}
