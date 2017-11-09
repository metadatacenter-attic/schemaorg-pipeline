package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeTranslator;

public class XsltTranslatorTest {

  @Test
  public void shouldConvertToXsltTemplates() {
    final String mapping =
        "@type:                       MedicalTrial\n" + 
        "additionalType:              clinicaltrials\n" +
        "name:                        /clinical_study/official_title\n" + 
        "alternateName:               /clinical_study/brief_title\n" + 
        "alternateName:               /clinical_study/acronym\n" +
        "identifier:                  /clinical_study/id_info/org_study_id\n" + 
        "identifier:                  /clinical_study/id_info/nct_id\n" + 
        "identifier:                  /clinical_study/id_info/secondary_id\n" + 
        "status:                      /clinical_study/overall_status\n" +
        "description:                 /clinical_study/detailed_description/textblock\n" +
        "disambiguatingDescription:   /clinical_study/brief_summary/textblock\n" +
        "studySubject:                /clinical_study/condition\n" +
        "code:                        /clinical_study/keyword\n" +
        "phase:                       /clinical_study/phase\n" +
        "trialDesign:                 /clinical_study/study_design_info/intervention_model\n" +
        "population:                  /clinical_study/eligibility/criteria/textblock\n" +
        "sponsor:                     /clinical_study/sponsors/lead_sponsor\n" + 
        "    @type:                   Organization\n" + 
        "    name:                    /agency\n" + 
        "    additionalType:          Lead Sponsor\n" + 
        "sponsor:                     /clinical_study/sponsors/collaborator\n" + 
        "    @type:                   Organization\n" + 
        "    name:                    /agency\n" + 
        "    additionalType:          Collaborator\n" + 
        "studyLocation:               /clinical_study/location/facility\n" + 
        "    @type:                   AdministrativeArea\n" + 
        "    name:                    /name\n" +
        "    additionalType:          Facility\n" +
        "    address:                 /address\n" +
        "        @type:               PostalAddress\n" +
        "        addressLocality:     /city\n" + 
        "        addressRegion:       /state\n" + 
        "        postalCode:          /zip\n" + 
        "        addressCountry:      /country\n" +
        "studyLocation:               /clinical_study/location_countries\n" +
        "    @type:                   AdministrativeArea\n" +
        "    name:                    /country\n" +
        "    additionalType:          Country Location\n" +
        "subjectOf:                   /clinical_study/references\n" +
        "    @type:                   CreativeWork\n" +
        "    additionalType:          pubmed\n" +
        "    identifier:              /PMID\n" +
        "    alternateName:           /citation\n" +
        "subjectOf:                   /clinical_study/results_reference\n" +
        "    @type:                   CreativeWork\n" +
        "    additionalType:          pubmed\n" +
        "    identifier:              /PMID\n" +
        "    alternateName:           /citation\n" +
        "subjectOf:                   /clinical_study/link\n" +
        "    @type:                   WebPage\n" +
        "    url:                     /url\n" +
        "    description:             /description";

    // Assertions
    System.out.println(MapNodeTranslator.translate(new XsltTranslatorHandler(), mapping));
    assertThat(MapNodeTranslator.translate(new XsltTranslatorHandler(), mapping), equalTo(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
        "   <xsl:template match=\"/*\">\n" + 
        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
        "         <xsl:apply-templates />\n" + 
        "         <additionalType>clinicaltrials</additionalType>\n" + 
        "      </instance>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/official_title\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/brief_title\">\n" + 
        "      <alternateName><xsl:value-of select=\".\"/></alternateName>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/acronym\">\n" + 
        "      <alternateName><xsl:value-of select=\".\"/></alternateName>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/id_info/org_study_id\">\n" + 
        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/id_info/nct_id\">\n" + 
        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/id_info/secondary_id\">\n" + 
        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/overall_status\">\n" + 
        "      <status><xsl:value-of select=\".\"/></status>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/detailed_description/textblock\">\n" + 
        "      <description><xsl:value-of select=\".\"/></description>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/brief_summary/textblock\">\n" + 
        "      <disambiguatingDescription><xsl:value-of select=\".\"/></disambiguatingDescription>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/condition\">\n" + 
        "      <studySubject><xsl:value-of select=\".\"/></studySubject>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/keyword\">\n" + 
        "      <code><xsl:value-of select=\".\"/></code>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/phase\">\n" + 
        "      <phase><xsl:value-of select=\".\"/></phase>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/study_design_info/intervention_model\">\n" + 
        "      <trialDesign><xsl:value-of select=\".\"/></trialDesign>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/eligibility/criteria/textblock\">\n" + 
        "      <population><xsl:value-of select=\".\"/></population>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/sponsors/lead_sponsor\">\n" + 
        "      <sponsor _type=\"Organization\">\n" + 
        "         <xsl:apply-templates select=\"agency\"/>\n" + 
        "         <additionalType>Lead Sponsor</additionalType>\n" + 
        "      </sponsor>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/sponsors/lead_sponsor/agency\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/sponsors/collaborator\">\n" + 
        "      <sponsor _type=\"Organization\">\n" + 
        "         <xsl:apply-templates select=\"agency\"/>\n" + 
        "         <additionalType>Collaborator</additionalType>\n" + 
        "      </sponsor>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/sponsors/collaborator/agency\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location/facility\">\n" + 
        "      <studyLocation _type=\"AdministrativeArea\">\n" + 
        "         <xsl:apply-templates select=\"name\"/>\n" + 
        "         <additionalType>Facility</additionalType>\n" + 
        "         <xsl:apply-templates select=\"address\"/>\n" + 
        "      </studyLocation>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location/facility/name\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location/facility/address\">\n" + 
        "      <address _type=\"PostalAddress\">\n" + 
        "         <xsl:apply-templates select=\"city\"/>\n" + 
        "         <xsl:apply-templates select=\"state\"/>\n" + 
        "         <xsl:apply-templates select=\"zip\"/>\n" + 
        "         <xsl:apply-templates select=\"country\"/>\n" + 
        "      </address>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location/facility/address/city\">\n" + 
        "      <addressLocality><xsl:value-of select=\".\"/></addressLocality>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location/facility/address/state\">\n" + 
        "      <addressRegion><xsl:value-of select=\".\"/></addressRegion>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location/facility/address/zip\">\n" + 
        "      <postalCode><xsl:value-of select=\".\"/></postalCode>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location/facility/address/country\">\n" + 
        "      <addressCountry><xsl:value-of select=\".\"/></addressCountry>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location_countries\">\n" + 
        "      <studyLocation _type=\"AdministrativeArea\">\n" + 
        "         <xsl:apply-templates select=\"country\"/>\n" + 
        "         <additionalType>Country Location</additionalType>\n" + 
        "      </studyLocation>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location_countries/country\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/references\">\n" + 
        "      <subjectOf _type=\"CreativeWork\">\n" + 
        "         <additionalType>pubmed</additionalType>\n" + 
        "         <xsl:apply-templates select=\"PMID\"/>\n" + 
        "         <xsl:apply-templates select=\"citation\"/>\n" + 
        "      </subjectOf>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/references/PMID\">\n" + 
        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/references/citation\">\n" + 
        "      <alternateName><xsl:value-of select=\".\"/></alternateName>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/results_reference\">\n" + 
        "      <subjectOf _type=\"CreativeWork\">\n" + 
        "         <additionalType>pubmed</additionalType>\n" + 
        "         <xsl:apply-templates select=\"PMID\"/>\n" + 
        "         <xsl:apply-templates select=\"citation\"/>\n" + 
        "      </subjectOf>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/results_reference/PMID\">\n" + 
        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/results_reference/citation\">\n" + 
        "      <alternateName><xsl:value-of select=\".\"/></alternateName>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/link\">\n" + 
        "      <subjectOf _type=\"WebPage\">\n" + 
        "         <xsl:apply-templates select=\"url\"/>\n" + 
        "         <xsl:apply-templates select=\"description\"/>\n" + 
        "      </subjectOf>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/link/url\">\n" + 
        "      <url><xsl:value-of select=\".\"/></url>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/link/description\">\n" + 
        "      <description><xsl:value-of select=\".\"/></description>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"text()\"/>\n" + 
        "</xsl:stylesheet>\n"));
  }
}
