package org.metadatacenter.schemaorg.pipeline.mapping.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.alma.databind.AttributeMapper;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.XsltConverter;

public class XsltConverterTest {

  @Test
  public void shouldConvertToXsltTemplates() {
    final String mapping =
        "@type:             MedicalTrial\n" + 
        "name:              /clinical_study/brief_title\n" + 
        "description:       /clinical_study/detailed_description/textblock\n" + 
        "identifier:        /clinical_study/id_info/nct_id\n" + 
        "sponsor:           /clinical_study/sponsors/lead_sponsor\n" + 
        "    @type:         Organization\n" + 
        "    name:          /agency\n" + 
        "    description:   /agency_class\n" + 
        "sponsor:           /clinical_study/sponsors/collaborator\n" + 
        "    @type:         Organization\n" + 
        "    name:          /agency\n" + 
        "    description:   /agency_class\n" + 
        "location:          /clinical_study/location\n" + 
        "    @type:         Place\n" + 
        "    name:          /facility/name\n" + 
        "    address:       /facility/address\n" + 
        "        @type:     Address\n" + 
        "        city:      /city\n" + 
        "        state:     /state\n" + 
        "        zip:       /zip\n" + 
        "        country:   USA";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(mapping);
    XsltConverter converter = new XsltConverter();
    converter.setDocumentRoot("clinical_study");
    // Assertions
    assertThat(converter.transform(mapNode), equalTo(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
        "   <xsl:template match=\"clinical_study\">\n" + 
        "      <instance _type=\"MedicalTrial\">\n" + 
        "         <xsl:apply-templates />\n" + 
        "      </instance>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/brief_title\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/detailed_description/textblock\">\n" + 
        "      <description><xsl:value-of select=\".\"/></description>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/id_info/nct_id\">\n" + 
        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/sponsors/collaborator\">\n" + 
        "      <sponsor _type=\"Organization\">\n" + 
        "         <xsl:apply-templates select=\"agency\"/>\n" + 
        "         <xsl:apply-templates select=\"agency_class\"/>\n" + 
        "      </sponsor>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"agency\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"agency_class\">\n" + 
        "      <description><xsl:value-of select=\".\"/></description>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"clinical_study/location\">\n" + 
        "      <location _type=\"Place\">\n" + 
        "         <xsl:apply-templates select=\"facility/name\"/>\n" + 
        "         <xsl:apply-templates select=\"facility/address\"/>\n" + 
        "      </location>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"facility/name\">\n" + 
        "      <name><xsl:value-of select=\".\"/></name>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"facility/address\">\n" + 
        "      <address _type=\"Address\">\n" + 
        "         <xsl:apply-templates select=\"city\"/>\n" + 
        "         <xsl:apply-templates select=\"state\"/>\n" + 
        "         <xsl:apply-templates select=\"zip\"/>\n" + 
        "         <country>USA</country>\n" + 
        "      </address>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"city\">\n" + 
        "      <city><xsl:value-of select=\".\"/></city>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"state\">\n" + 
        "      <state><xsl:value-of select=\".\"/></state>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"zip\">\n" + 
        "      <zip><xsl:value-of select=\".\"/></zip>\n" + 
        "   </xsl:template>\n" + 
        "   <xsl:template match=\"text()\"/>\n" + 
        "</xsl:stylesheet>\n"));
  }
}
