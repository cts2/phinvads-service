package edu.mayo.cts2.framework.plugin.service.phinvads.profile.valueset

import scala.collection.JavaConversions._
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.plugin.service.phinvads.dao.PhinVadsDao
import edu.mayo.cts2.framework.plugin.service.phinvads.profile.AbstractService
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService
import gov.cdc.vocab.service.bean.ValueSet
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import scala.collection.immutable.Map
import javax.annotation.Resource
import edu.mayo.cts2.framework.plugin.service.phinvads.transform.ValueSetTransform

@Component
class PhinVadsValueSetReadService
  extends AbstractService
  with ValueSetReadService {

  @Resource
  var phinVadsDao: PhinVadsDao =  _
  
  @Resource
  var valueSetTransform: ValueSetTransform = _

  @Override
  def read(id: NameOrURI,
    readContext: ResolvedReadContext): ValueSetCatalogEntry = {

    val valueSet:ValueSet = 
    if(id.getName() != null){
      phinVadsDao.valueSetMaps.valueSetByName.getOrElse(id.getName(), return null)
    } else {
      phinVadsDao.valueSetMaps.valueSetByUri.getOrElse(id.getUri(), return null)
    }
    
    valueSetTransform.transformPhinVadsValueSetToEntry(valueSet)
  }

  @Override
  def exists(identifier: NameOrURI, readContext: ResolvedReadContext): Boolean = {
    throw new UnsupportedOperationException()
  }
}