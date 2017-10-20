package org.metadatacenter.schemaorg.pipeline;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.alma.databind.AttributeMapper;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapping.MapConverter;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.XsltParameters;
import org.metadatacenter.schemaorg.pipeline.release.SchemaToHtml;
import org.metadatacenter.schemaorg.pipeline.transform.XmlToSchema;
import org.metadatacenter.schemaorg.pipeline.transform.datasource.XsltTransformer;

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
    AttributeMapper attributeMapper = new AttributeMapper();
    MapNode mapNode = attributeMapper.readText(CLINICAL_TRIALS_MAPPING);
    showTextEditor(CLINICAL_TRIALS_MAPPING);

    XsltParameters parameters = new XsltParameters();
    parameters.setDocumentRoot("clinical_study");

    MapConverter converter = MapConverter.newInstance();
    String xsltString = converter.use("xslt", parameters).transform(mapNode);
    showTextEditor(xsltString);

    XsltTransformer transformer = XsltTransformer.newTransformer(xsltString);
    InputStream in = getClass().getResourceAsStream("document.xml");
    String xmlResult = transformer.transform(in);
    showTextEditor(xmlResult);

    XmlToSchema xmlToSchema = new XmlToSchema();
    String jsonLdResult = xmlToSchema.transform(xmlResult);
    showTextEditor(jsonLdResult);
    
    String htmlResult = SchemaToHtml.transform(jsonLdResult);
    showTextEditor(htmlResult);
  }

  private static void showTextEditor(String text) throws IOException {
    File file = File.createTempFile(UUID.randomUUID().toString(), ".txt");
    try (FileOutputStream fop = new FileOutputStream(file)) {
      byte[] contentInBytes = text.getBytes();
      fop.write(contentInBytes);
      Desktop.getDesktop().edit(file);
    }
  }
}
