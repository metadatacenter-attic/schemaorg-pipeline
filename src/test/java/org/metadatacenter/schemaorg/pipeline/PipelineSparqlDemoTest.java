package org.metadatacenter.schemaorg.pipeline;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.api.Pipeline;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeTranslator;
import org.metadatacenter.schemaorg.pipeline.mapping.TranslatorHandler;
import org.metadatacenter.schemaorg.pipeline.mapping.translator.SparqlConstructTranslatorHandler;
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
  public void shouldProduceResultsFromBio2RDF() throws IOException {
    TranslatorHandler handler = createTranslatorHandler();
    String query = MapNodeTranslator.translate(handler, DRUGBANK_MAPPING);

    SparqlEndpointClient bio2rdf = SparqlEndpointClient.BIO2RDF;

    String instanceId = "http://bio2rdf.org/drugbank:DB00112";
    String output = Pipeline.create()
        .pipe(s -> bio2rdf.evaluatePreparedQuery(s, instanceId))
        .pipe(RdfToSchema::transform)
        .pipe(SchemaToHtml::transform)
        .run(query);

    String input = DRUGBANK_MAPPING;

    showTextEditor(input, ".txt");
    showTextEditor(output, ".html");
  }

  private SparqlConstructTranslatorHandler createTranslatorHandler() {
    SparqlConstructTranslatorHandler handler = new SparqlConstructTranslatorHandler();
    handler.addPrefix("schema", "http://schema.org/");
    handler.addPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    handler.addPrefix("db", "http://bio2rdf.org/drugbank_vocabulary:");
    handler.addPrefix("bio2rdf", "http://bio2rdf.org/bio2rdf_vocabulary:");
    return handler;
  }

  private static void showTextEditor(String text, String extension) throws IOException {
    File file = File.createTempFile(UUID.randomUUID().toString(), extension);
    try (FileOutputStream fop = new FileOutputStream(file)) {
      byte[] contentInBytes = text.getBytes();
      fop.write(contentInBytes);
      Desktop.getDesktop().edit(file);
    }
  }
}
