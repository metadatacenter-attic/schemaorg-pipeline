package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Ignore;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeTranslator;
import org.metadatacenter.schemaorg.pipeline.mapping.MappingLanguages;
import static org.hamcrest.MatcherAssert.assertThat;

public class RmlToXsltTest {

  @Test
  public void shouldTranslateConstant() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"clinicaltrials\" ]\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(438));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "         <additionalType>clinicaltrials</additionalType>\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" +
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Test
  public void shouldTranslatePath() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/clinical_study/official_title\" ]\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(502));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" +
//        "   <xsl:template match=\"clinical_study/official_title\">\n" + 
//        "      <name><xsl:value-of select=\".\"/></name>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Test
  public void shouldTranslateObject() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:sponsor;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Sponsor> ]\n" + 
        "   ]." + 
        "<#Sponsor>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/sponsors/lead_sponsor\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Organization\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/agency\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"Lead Sponsor\" ]\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(755));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" +
//        "   <xsl:template match=\"clinical_study/sponsors/lead_sponsor\">\n" + 
//        "      <sponsor _type=\"Organization\">\n" + 
//        "         <additionalType>Lead Sponsor</additionalType>\n" + 
//        "         <xsl:apply-templates select=\"agency\"/>\n" + 
//        "      </sponsor>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/sponsors/lead_sponsor/agency\">\n" + 
//        "      <name><xsl:value-of select=\".\"/></name>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Test
  public void shouldTranslateArrayOfConstants() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"clinicaltrials\" ];\n" + 
        "      rr:objectMap [ rr:constant \"CohortStudy\" ];\n" + 
        "      rr:objectMap [ rr:constant \"Observational\" ]\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(548));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "         <additionalType>clinicaltrials</additionalType>\n" + 
//        "         <additionalType>Observational</additionalType>\n" + 
//        "         <additionalType>CohortStudy</additionalType>\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" +
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Test
  public void shouldTranslateArrayOfPaths() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:identifier;\n" + 
        "      rr:objectMap [ rml:reference \"/clinical_study/id_info/org_study_id\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/clinical_study/id_info/nct_id\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/clinical_study/id_info/secondary_id\" ]\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(792));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" +
//        "   <xsl:template match=\"clinical_study/id_info/org_study_id\">\n" + 
//        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/id_info/nct_id\">\n" + 
//        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/id_info/secondary_id\">\n" + 
//        "      <identifier><xsl:value-of select=\".\"/></identifier>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Ignore
  @Test
  public void shouldTranslateArrayOfObjects() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:subjectOf;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Reference1> ];\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Reference2> ];\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Reference3> ]\n" + 
        "   ].\n" + 
        "<#Reference1>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/references\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:CreativeWork\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"pubmed\" ];\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:alternateName;\n" + 
        "      rr:objectMap [ rml:reference \"/citation\" ];\n" + 
        "   ].\n" + 
        "<#Reference2>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/results_reference\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:CreativeWork\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"pubmed\" ];\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:alternateName;\n" + 
        "      rr:objectMap [ rml:reference \"/citation\" ];\n" + 
        "   ].\n" + 
        "<#Reference3>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/link\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:WebPage\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:alternateName;\n" + 
        "      rr:objectMap [ rml:reference \"/description\" ];\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
//    assertThat(output.length(), equalTo(755));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" +
//        "   <xsl:template match=\"clinical_study/references\">\n" + 
//        "      <subjectOf _type=\"CreativeWork\">\n" + 
//        "         <additionalType>pubmed</additionalType>\n" + 
//        "         <xsl:apply-templates select=\"citation\"/>\n" + 
//        "      </subjectOf>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/references/citation\">\n" + 
//        "      <alternateName><xsl:value-of select=\".\"/></alternateName>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/results_reference\">\n" + 
//        "      <subjectOf _type=\"CreativeWork\">\n" + 
//        "         <additionalType>pubmed</additionalType>\n" + 
//        "         <xsl:apply-templates select=\"citation\"/>\n" + 
//        "      </subjectOf>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/results_reference/citation\">\n" + 
//        "      <alternateName><xsl:value-of select=\".\"/></alternateName>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/link\">\n" + 
//        "      <subjectOf _type=\"WebPage\">\n" + 
//        "         <xsl:apply-templates select=\"description\"/>\n" + 
//        "      </subjectOf>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/link/description\">\n" + 
//        "      <description><xsl:value-of select=\".\"/></description>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Test
  public void shouldTranslateNestedObject() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:studyLocation;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#StudyLocation> ]\n" + 
        "   ].\n" + 
        "<#StudyLocation>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:AdministrativeArea\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:address;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Address> ];\n" + 
        "   ].\n" + 
        "<#Address>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility/address\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:PostalAddress\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:addressLocality;\n" + 
        "      rr:objectMap [ rml:reference \"/city\" ];\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:addressCountry;\n" + 
        "      rr:objectMap [ rr:constant \"USA\" ];\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(972));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility\">\n" + 
//        "      <studyLocation _type=\"AdministrativeArea\">\n" + 
//        "         <xsl:apply-templates select=\"address\"/>\n" + 
//        "      </studyLocation>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility/address\">\n" + 
//        "      <address _type=\"PostalAddress\">\n" + 
//        "         <xsl:apply-templates select=\"city\"/>\n" + 
//        "         <addressCountry>USA</addressCountry>\n" + 
//        "      </address>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility/address/city\">\n" + 
//        "      <addressLocality><xsl:value-of select=\".\"/></addressLocality>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Test
  public void shouldTranslateArrayOfConstantsInNestedObject() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:studyLocation;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#StudyLocation> ]\n" + 
        "   ].\n" + 
        "<#StudyLocation>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:AdministrativeArea\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:address;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Address> ];\n" + 
        "   ].\n" + 
        "<#Address>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility/address\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:PostalAddress\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:addressLocality;\n" + 
        "      rr:objectMap [ rr:constant \"San Francisco\" ];\n" + 
        "      rr:objectMap [ rr:constant \"Palo Alto\" ];\n" + 
        "      rr:objectMap [ rr:constant \"Mountain View\" ];\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(891));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility\">\n" + 
//        "      <studyLocation _type=\"AdministrativeArea\">\n" + 
//        "         <xsl:apply-templates select=\"address\"/>\n" + 
//        "      </studyLocation>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility/address\">\n" + 
//        "      <address _type=\"PostalAddress\">\n" + 
//        "         <addressLocality>San Francisco</addressLocality>\n" + 
//        "         <addressLocality>Palo Alto</addressLocality>\n" + 
//        "         <addressLocality>Mountain View</addressLocality>\n" + 
//        "      </address>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Test
  public void shouldTranslateArrayOfPathsInNestedObject() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:studyLocation;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#StudyLocation> ]\n" + 
        "   ].\n" + 
        "<#StudyLocation>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:AdministrativeArea\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:address;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Address> ];\n" + 
        "   ].\n" + 
        "<#Address>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility/address\"\n" +
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:PostalAddress\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:addressLocality;\n" + 
        "      rr:objectMap [ rml:reference \"/city\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/subsidiary/city\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/administration/city\" ];\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
    assertThat(output.length(), equalTo(1388));
//    assertThat(output, equalTo(
//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
//        "<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
//        "   <xsl:output method=\"xml\" indent=\"yes\" />\n" + 
//        "   <xsl:template match=\"/*\">\n" + 
//        "      <instance _context=\"http://schema.org\" _type=\"MedicalTrial\">\n" + 
//        "         <xsl:apply-templates />\n" + 
//        "      </instance>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility\">\n" + 
//        "      <studyLocation _type=\"AdministrativeArea\">\n" + 
//        "         <xsl:apply-templates select=\"address\"/>\n" + 
//        "      </studyLocation>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility/address\">\n" + 
//        "      <address _type=\"PostalAddress\">\n" + 
//        "         <xsl:apply-templates select=\"city\"/>\n" + 
//        "         <xsl:apply-templates select=\"subsidiary/city\"/>\n" + 
//        "         <xsl:apply-templates select=\"administration/city\"/>\n" + 
//        "      </address>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility/address/city\">\n" + 
//        "      <addressLocality><xsl:value-of select=\".\"/></addressLocality>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility/address/subsidiary/city\">\n" + 
//        "      <addressLocality><xsl:value-of select=\".\"/></addressLocality>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"clinical_study/location/facility/address/administration/city\">\n" + 
//        "      <addressLocality><xsl:value-of select=\".\"/></addressLocality>\n" + 
//        "   </xsl:template>\n" + 
//        "   <xsl:template match=\"text()\"/>\n" + 
//        "</xsl:stylesheet>\n"));
  }

  @Ignore
  @Test
  public void shouldTranslateClinicalTrialsMapping() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalTrial\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"clinicaltrials\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/official_title\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:alternateName;\n" + 
        "      rr:objectMap [ rml:reference \"/brief_title\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/acronym\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:identifier;\n" + 
        "      rr:objectMap [ rml:reference \"/id_info/org_study_id\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/id_info/nct_id\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/id_info/secondary_id\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:status;\n" + 
        "      rr:objectMap [ rml:reference \"/overall_status\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:description;\n" + 
        "      rr:objectMap [ rml:reference \"/detailed_description/textblock\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:disambiguatingDescription;\n" + 
        "      rr:objectMap [ rml:reference \"/brief_summary/textblock\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:studySubject;\n" + 
        "      rr:objectMap [ rml:reference \"/condition\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:code;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Code> ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:phase;\n" + 
        "      rr:objectMap [ rml:reference \"/phase\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:trialDesign;\n" + 
        "      rr:objectMap [ rml:reference \"/study_design_info/intervention_model\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:population;\n" + 
        "      rr:objectMap [ rml:reference \"/eligibility/criteria/textblock\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:sponsor;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#LeadSponsor> ];\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Collaborator> ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:studyLocation;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Facility> ];\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Countries> ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:subjectOf;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#References> ];\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#ResultsReference> ];\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Link> ]\n" + 
        "   ].   \n" + 
        "<#Code>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/keyword\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:MedicalCode\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:codeValue;\n" + 
        "      rr:objectMap [ rml:reference \"/.\" ];\n" + 
        "   ].\n" + 
        "<#LeadSponsor>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/sponsors/lead_sponsor\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Organization\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/agency\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"Lead Sponsor\" ];\n" + 
        "   ].\n" + 
        "<#Collaborator>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/sponsors/collaborator\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Organization\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/agency\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"Collaborator\" ];\n" + 
        "   ].\n" + 
        "<#Facility>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:AdministrativeArea\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/name\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"Facility\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:address;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Address> ]\n" + 
        "   ].\n" + 
        "<#Address>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location/facility/address\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:PostalAddress\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:addressLocality;\n" + 
        "      rr:objectMap [ rml:reference \"/city\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:addressRegion;\n" + 
        "      rr:objectMap [ rml:reference \"/state\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:postalCode;\n" + 
        "      rr:objectMap [ rml:reference \"/zip\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:addressCountry;\n" + 
        "      rr:objectMap [ rml:reference \"/country\" ]\n" + 
        "   ].\n" + 
        "<#Countries>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/location_countries\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:AdministrativeArea\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/country\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"Country Location\" ];\n" + 
        "   ].\n" + 
        "<#References>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/references\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:CreativeWork\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"pubmed\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:identifier;\n" + 
        "      rr:objectMap [ rml:reference \"/PMID\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:alternateName;\n" + 
        "      rr:objectMap [ rml:reference \"/citation\" ];\n" + 
        "   ].\n" + 
        "<#References>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/results_reference\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:CreativeWork\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:additionalType;\n" + 
        "      rr:objectMap [ rr:constant \"pubmed\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:identifier;\n" + 
        "      rr:objectMap [ rml:reference \"/PMID\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:alternateName;\n" + 
        "      rr:objectMap [ rml:reference \"/citation\" ];\n" + 
        "   ].\n" + 
        "<#Link>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/clinical_study/link\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:WebPage\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:url;\n" + 
        "      rr:objectMap [ rml:reference \"/url\" ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:description;\n" + 
        "      rr:objectMap [ rml:reference \"/description\" ];\n" + 
        "   ].";
    String output = MapNodeTranslator.translate(new XsltTranslatorHandler(), rmlMapping, MappingLanguages.RML);
    // Asserts
//    assertThat(output.length(), equalTo(755));
  }
}
