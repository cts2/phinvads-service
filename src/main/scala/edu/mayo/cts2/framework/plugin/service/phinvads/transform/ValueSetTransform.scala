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

@Component
class ValueSetTransform {

  @Resource
  var urlConstructor: UrlConstructor = _

  @Resource
  var phinVadsDao: PhinVadsDao = _

  def transformPhinVadsValueSetToSummary = (phinvadsvs: ValueSet) => {
    transformValueSetToSummary(transformPhinVadsValueSetToEntry(phinvadsvs))
  }: ValueSetCatalogEntrySummary

  def transformPhinVadsValueSetToEntry = (phinvadsvs: ValueSet) => {
    val entry: ValueSetCatalogEntry = new ValueSetCatalogEntry()

    val txt = phinvadsvs.getDefinitionText()

    entry.setAbout("urn:oid:" + phinvadsvs.getOid);
    entry.setValueSetName(phinvadsvs.getCode())
    entry.setFormalName(phinvadsvs.getName())

    if (StringUtils.isNotBlank(txt)) {
      val description = new EntryDescription()
      description.setValue(ModelUtils.toTsAnyType(EncodingUtils.stripNonValidXMLCharacters(txt)))
      entry.setResourceSynopsis(description)
    }

    entry
  }: ValueSetCatalogEntry

  def transformValueSetToSummary = (entry: ValueSetCatalogEntry) => {
    val summary: ValueSetCatalogEntrySummary = new ValueSetCatalogEntrySummary()

    val name = entry.getValueSetName()

    summary.setAbout(entry.getAbout())
    summary.setValueSetName(name)
    summary.setFormalName(entry.getFormalName())
    summary.setResourceSynopsis(entry.getResourceSynopsis())
    summary.setHref(urlConstructor.createValueSetUrl(name))

    summary
  }: ValueSetCatalogEntrySummary

  def transformValueSetConcept = (entry: ValueSetConcept) => {
    val synopsis: EntitySynopsis = new EntitySynopsis()
    synopsis.setDesignation(entry.getCdcPreferredDesignation())
    synopsis.setName(entry.getConceptCode())
    val cs = phinVadsDao.codeSystemMaps.codeSystemByUri(entry.getCodeSystemOid())
    synopsis.setNamespace(cs.getCodeSystemCode())
    synopsis.setUri("uri:urn:" + entry.getId);

    synopsis
  }: EntitySynopsis
}