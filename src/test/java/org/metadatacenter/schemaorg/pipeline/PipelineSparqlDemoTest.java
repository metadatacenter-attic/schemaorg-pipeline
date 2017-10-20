package org.metadatacenter.schemaorg.pipeline;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.alma.databind.AttributeMapper;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapping.ConverterNotFoundException;
import org.metadatacenter.schemaorg.pipeline.mapping.MapConverter;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.SparqlParameters;
import org.metadatacenter.schemaorg.pipeline.release.SchemaToHtml;
import org.metadatacenter.schemaorg.pipeline.transform.RdfToSchema;
import org.metadatacenter.schemaorg.pipeline.transform.datasource.SparqlEndpointClient;

public class PipelineSparqlDemoTest {

  private static final String DRUGBANK_MAPPING =
      "@type:                Drug\n" + 
      "name:                 /dcterms:title\n" + 
      "description:          /dcterms:description\n" + 
      "identifier:           /dcterms:identifier\n" + 
      "url:                  /bio2rdf:uri\n" + 
      "sameAs:               /rdfs:seeAlso\n" + 
      "proprietaryName:      /db:brand/dcterms:title\n" + 
      "nonProprietaryName:   /db:synonym/dcterms:title\n" + 
      "clinicalPharmacology: /db:pharmacodynamics/dcterms:description\n" + 
      "drugClass:            /db:category/dcterms:title\n" + 
      "cost:                 /db:product\n" + 
      "   @type:             DrugCost\n" + 
      "   costPerUnit:       /db:price\n" + 
      "   costCurrency:      USD\n" + 
      "   drugUnit:          /dcterms:title\n" +
      "availableStrength:    /db:dosage\n" + 
      "   @type:             DrugStrength\n" + 
      "   description:       /dcterms:title\n" + 
      "administrationRoute:  /db:dosage/db:route/dcterms:title\n" + 
      "administrationForm:   /db:dosage/db:form/dcterms:title\n" + 
      "mechanismOfAction:    /db:mechanism-of-action/dcterms:description\n" + 
      "interactingDrug:      /db:ddi-interactor-in/dcterms:title\n" + 
      "foodWarning:          /db:food-interaction/rdf:value\n" + 
      "availableStrength:    /db:dosage\n" + 
      "   @type:             DrugStrength\n" + 
      "   description:       /dcterms:title\n" + 
      "legalStatus:          /db:group/bio2rdf:identifier\n" + 
      "manufacturer:         /db:manufacturer\n" + 
      "   @type:             Organization\n" + 
      "   name:              /rdf:value";

  @Test
  public void shouldProduceResultsFromBio2RDF() throws ConverterNotFoundException, IOException {
    AttributeMapper attributeMapper = new AttributeMapper();
    MapNode mapNode = attributeMapper.readText(DRUGBANK_MAPPING);
    showTextEditor(DRUGBANK_MAPPING);

    SparqlParameters parameters = new SparqlParameters();
    parameters.setInstanceIri("http://bio2rdf.org/drugbank:DB00001");
    parameters.addPrefix("schema", "http://schema.org/");
    parameters.addPrefix("db", "http://bio2rdf.org/drugbank_vocabulary:");
    parameters.addPrefix("bio2rdf", "http://bio2rdf.org/bio2rdf_vocabulary:");

    MapConverter converter = MapConverter.newInstance();
    String queryString = converter.use("sparql-construct", parameters).transform(mapNode);
    showTextEditor(queryString);

    SparqlEndpointClient endpointClient = new SparqlEndpointClient("http://bio2rdf.org/sparql");
    String graphResult = endpointClient.evaluate(queryString, "N-Triples");
    showTextEditor(graphResult);

    RdfToSchema graphToJsonLd = new RdfToSchema();
    String jsonLdResult = graphToJsonLd.transform(graphResult);
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
