package edu.mayo.cts2.framework.plugin.service.phinvads.profile.codesystem

import java.lang.Override

import scala.collection.JavaConversions._

import org.springframework.stereotype.Component

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.VersionTagReference
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.plugin.service.phinvads.profile.AbstractService
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService

@Component
class PhinVadsCodeSystemReadService
  extends AbstractService
  with CodeSystemReadService {

  def currentFilter(map: Map[String, String]) = {
    map.get("CURVER").get.equals("Y")
  }

  @Override
  def read(identifier: NameOrURI,
    readContext: ResolvedReadContext): CodeSystemCatalogEntry = {

   null
  }

  @Override
  def exists(identifier: NameOrURI, readContext: ResolvedReadContext): Boolean = {
    throw new UnsupportedOperationException()
  }

  def getSupportedTags: java.util.List[VersionTagReference] =
    List[VersionTagReference](CURRENT_TAG)

}