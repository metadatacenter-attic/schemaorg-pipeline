package org.metadatacenter.schemaorg.pipeline;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.api.Pipeline;
import org.metadatacenter.schemaorg.pipeline.experimental.DBpediaLookup;
import org.metadatacenter.schemaorg.pipeline.experimental.IdExpander;
import org.metadatacenter.schemaorg.pipeline.experimental.IdentifiersResolver;
import org.metadatacenter.schemaorg.pipeline.experimental.SchemaEnrichment;
import org.metadatacenter.schemaorg.pipeline.experimental.TermLookup;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeTranslator;
import org.metadatacenter.schemaorg.pipeline.mapping.TranslatorHandler;
import org.metadatacenter.schemaorg.pipeline.mapping.translator.XsltTranslatorHandler;
import org.metadatacenter.schemaorg.pipeline.release.SchemaToHtml;
import org.metadatacenter.schemaorg.pipeline.transform.XmlToSchema;
import org.metadatacenter.schemaorg.pipeline.transform.datasource.XsltTransformer;
import com.google.common.base.Charsets;

public class PipelineXmlDemoTest {

  private static final String CLINICAL_TRIALS_MAPPING =
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

  @Test
  public void shouldProduceResultsFromClinicalTrials() throws Exception {
    TranslatorHandler handler = new XsltTranslatorHandler();
    String stylesheet = MapNodeTranslator.translate(handler, CLINICAL_TRIALS_MAPPING);

    XsltTransformer transformer = XsltTransformer.newTransformer(stylesheet);

    TermLookup dbpediaLookup = new DBpediaLookup();
    IdExpander identifiersResolver = new IdentifiersResolver();

    String xmlDocument = "document.xml";
    String output = Pipeline.create()
        .pipe(this::readDocument)
        .pipe(transformer::transform)
        .pipe(XmlToSchema::transform)
        .pipe(s -> SchemaEnrichment.fillOutIdFromObjectIdentifier(s, identifiersResolver))
        .pipe(s -> SchemaEnrichment.fillOutIdFromObjectName(s, dbpediaLookup))
        .pipe(SchemaToHtml::transform)
        .run(xmlDocument);

    String input = CLINICAL_TRIALS_MAPPING;

    showTextEditor(input, ".txt");
    showTextEditor(output, ".html");
  }

  private static void showTextEditor(String text, String extension) throws IOException {
    File file = File.createTempFile(UUID.randomUUID().toString(), extension);
    try (FileOutputStream fop = new FileOutputStream(file)) {
      byte[] contentInBytes = text.getBytes();
      fop.write(contentInBytes);
      Desktop.getDesktop().edit(file);
    }
  }

  private String readDocument(String path) {
    final InputStream in = getClass().getResourceAsStream(path);
    try {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = in.read(buffer)) != -1) {
          result.write(buffer, 0, length);
      }
      String output = result.toString(Charsets.UTF_8.name());
      return output;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
