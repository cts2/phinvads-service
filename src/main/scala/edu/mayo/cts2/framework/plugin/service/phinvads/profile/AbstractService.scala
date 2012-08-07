package edu.mayo.cts2.framework.plugin.service.phinvads.profile

import scala.collection.JavaConversions._
import edu.mayo.cts2.framework.core.url.UrlConstructor
import edu.mayo.cts2.framework.model.core.OpaqueData
import edu.mayo.cts2.framework.model.core.SourceReference
import edu.mayo.cts2.framework.model.core.VersionTagReference
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.service.profile.BaseService
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.plugin.service.phinvads.dao.Slice

abstract class AbstractService extends BaseService {

  val MAYO = "Mayo Clinic"
  val DEFAULT_VERSION = "1.0"
  val DESCRIPTION = "CTS2 Service Implementation using the NLM UMLS."
  val CURRENT_TAG = {
    new VersionTagReference("CURRENT")
  }

  @Resource
  var urlConstructor: UrlConstructor = _
  
  def pageToSlice(page: Page = new Page()):Slice = {
    new Slice(page.getStart, page.getEnd)
  }
  
  override def getServiceVersion(): String = {
    DEFAULT_VERSION
  }

  override def getServiceProvider(): SourceReference = {
    var ref = new SourceReference()
    ref.setContent(MAYO)

    ref
  }

  override def getServiceDescription(): OpaqueData = {
    return ModelUtils.createOpaqueData(DESCRIPTION)
  }

  override def getServiceName(): String = {
    return this.getClass().getCanonicalName()
  }

  def getKnownNamespaceList: java.util.List[DocumentedNamespaceReference] = List[DocumentedNamespaceReference]()

}

