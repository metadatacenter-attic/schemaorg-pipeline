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
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeTranslator;
import org.metadatacenter.schemaorg.pipeline.mapping.TranslatorHandler;
import org.metadatacenter.schemaorg.pipeline.mapping.translator.XsltTranslatorHandler;
import org.metadatacenter.schemaorg.pipeline.release.SchemaToHtml;
import org.metadatacenter.schemaorg.pipeline.transform.XmlToSchema;
import org.metadatacenter.schemaorg.pipeline.transform.datasource.XsltTransformer;
import com.google.common.base.Charsets;

public class PipelineXmlDemoTest {

  private static final String CLINICAL_TRIALS_MAPPING =
      "@type:             MedicalTrial\n" + 
      "name:              /clinical_study/brief_title\n" + 
      "description:       /clinical_study/detailed_description/textblock\n" + 
      "identifier:        /clinical_study/id_info/nct_id\n" + 
      "sponsor:           /clinical_study/sponsors/lead_sponsor\n" + 
      "    @type:         Organization\n" + 
      "    name:          /agency\n" + 
      "    description:   /agency_class\n" + 
      "collaborator:      /clinical_study/sponsors/collaborator\n" + 
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

  @Test
  public void shouldProduceResultsFromClinicalTrials() throws Exception {
    TranslatorHandler handler = new XsltTranslatorHandler();
    String stylesheet = MapNodeTranslator.translate(handler, CLINICAL_TRIALS_MAPPING);

    XsltTransformer transformer = XsltTransformer.newTransformer(stylesheet);

    String xmlDocument = "document.xml";
    String output = Pipeline.create()
        .pipe(this::readDocument)
        .pipe(transformer::transform)
        .pipe(XmlToSchema::transform)
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
      System.out.println(output);
      return output;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
