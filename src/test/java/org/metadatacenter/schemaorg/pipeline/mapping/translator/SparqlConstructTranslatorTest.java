package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeTranslator;

public class SparqlConstructTranslatorTest {

  @Test
  public void shouldConvertToSparqlConstruct() {
    final String mapping =
        "@prefix:              ('schema', 'http://schema.org/')\n" + 
        "@prefix:              ('rdf', 'http://www.w3.org/1999/02/22-rdf-syntax-ns#')\n" +
        "@prefix:              ('rdfs', 'http://www.w3.org/2000/01/rdf-schema#')\n" + 
        "@prefix:              ('dcterms', 'http://purl.org/dc/terms/')\n" + 
        "@prefix:              ('db', 'http://bio2rdf.org/drugbank_vocabulary:')\n" + 
        "@prefix:              ('bio2rdf', 'http://bio2rdf.org/bio2rdf_vocabulary:')\n" + 
        "@type:                ('Drug', 'db:Drug')\n" + 
        "name:                 /dcterms:title\n" + 
        "description:          /dcterms:description\n" + 
        "identifier:           /dcterms:identifier\n" + 
        "url:                  /bio2rdf:uri\n" + 
        "sameAs:               /rdfs:seeAlso\n" + 
        "proprietaryName:      /db:brand/dcterms:title\n" + 
        "nonProprietaryName:   /db:synonym/dcterms:title\n" + 
        "clinicalPharmacology: /db:pharmacodynamics/dcterms:description\n" + 
        "drugClass:            /db:category/dcterms:title\n" + 
        "drugCost:             /db:product\n" + 
        "   @type:             'DrugCost'\n" + 
        "   costPerUnit:       /db:price\n" + 
        "   costCurrency:      'USD'\n" + 
        "   drugUnit:          /dcterms:title\n" +
        "availableStrength:    /db:dosage\n" + 
        "   @type:             'DrugStrength'\n" + 
        "   description:       /dcterms:title\n" + 
        "administrationRoute:  /db:dosage/db:route/dcterms:title\n" + 
        "administrationForm:   /db:dosage/db:form/dcterms:title\n" + 
        "mechanismOfAction:    /db:mechanism-of-action/dcterms:description\n" + 
        "interactingDrug:      /db:ddi-interactor-in/dcterms:title\n" + 
        "foodWarning:          /db:food-interaction/rdf:value\n" + 
        "legalStatus:          /db:group/bio2rdf:identifier\n" + 
        "manufacturer:         /db:manufacturer\n" + 
        "   @type:             'Organization'\n" + 
        "   name:              /rdf:value";
    SparqlConstructTranslatorHandler handler = new SparqlConstructTranslatorHandler();
    // Assertion
    assertThat(MapNodeTranslator.translate(handler, mapping), equalTo(
        "PREFIX schema: <http://schema.org/>\n" + 
        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
        "PREFIX dcterms: <http://purl.org/dc/terms/>\n" + 
        "PREFIX db: <http://bio2rdf.org/drugbank_vocabulary:>\n" + 
        "PREFIX bio2rdf: <http://bio2rdf.org/bio2rdf_vocabulary:>\n" + 
        "\n" + 
        "CONSTRUCT { \n" + 
        "   ?s a schema:Drug.\n" + 
        "   ?s schema:name ?name.\n" + 
        "   ?s schema:description ?description.\n" + 
        "   ?s schema:identifier ?identifier.\n" + 
        "   ?s schema:url ?url.\n" + 
        "   ?s schema:sameAs ?sameAs.\n" + 
        "   ?s schema:proprietaryName ?proprietaryName.\n" + 
        "   ?s schema:nonProprietaryName ?nonProprietaryName.\n" + 
        "   ?s schema:clinicalPharmacology ?clinicalPharmacology.\n" + 
        "   ?s schema:drugClass ?drugClass.\n" + 
        "   ?s schema:drugCost ?drugCost.\n" + 
        "   ?drugCost a schema:DrugCost.\n" + 
        "   ?drugCost schema:costPerUnit ?drugCostCostPerUnit.\n" + 
        "   ?drugCost schema:costCurrency 'USD'.\n" + 
        "   ?drugCost schema:drugUnit ?drugCostDrugUnit.\n" + 
        "   ?s schema:availableStrength ?availableStrength.\n" + 
        "   ?availableStrength a schema:DrugStrength.\n" + 
        "   ?availableStrength schema:description ?availableStrengthDescription.\n" + 
        "   ?s schema:administrationRoute ?administrationRoute.\n" + 
        "   ?s schema:administrationForm ?administrationForm.\n" + 
        "   ?s schema:mechanismOfAction ?mechanismOfAction.\n" + 
        "   ?s schema:interactingDrug ?interactingDrug.\n" + 
        "   ?s schema:foodWarning ?foodWarning.\n" + 
        "   ?s schema:legalStatus ?legalStatus.\n" + 
        "   ?s schema:manufacturer ?manufacturer.\n" + 
        "   ?manufacturer a schema:Organization.\n" + 
        "   ?manufacturer schema:name ?manufacturerName.\n" + 
        " }\n" + 
        "WHERE { \n" + 
        "   OPTIONAL { ?s a db:Drug. }\n" +
        "   OPTIONAL { ?s dcterms:title ?name. }\n" + 
        "   OPTIONAL { ?s dcterms:description ?description. }\n" + 
        "   OPTIONAL { ?s dcterms:identifier ?identifier. }\n" + 
        "   OPTIONAL { ?s bio2rdf:uri ?url. }\n" + 
        "   OPTIONAL { ?s rdfs:seeAlso ?sameAs. }\n" + 
        "   OPTIONAL { ?s db:brand [dcterms:title ?proprietaryName]. }\n" + 
        "   OPTIONAL { ?s db:synonym [dcterms:title ?nonProprietaryName]. }\n" + 
        "   OPTIONAL { ?s db:pharmacodynamics [dcterms:description ?clinicalPharmacology]. }\n" + 
        "   OPTIONAL { ?s db:category [dcterms:title ?drugClass]. }\n" + 
        "   OPTIONAL { \n" + 
        "      ?s db:product ?drugCost.\n" + 
        "      ?drugCost db:price ?drugCostCostPerUnit.\n" + 
        "      ?drugCost dcterms:title ?drugCostDrugUnit.\n" + 
        "    }\n" + 
        "   OPTIONAL { \n" + 
        "      ?s db:dosage ?availableStrength.\n" + 
        "      ?availableStrength dcterms:title ?availableStrengthDescription.\n" + 
        "    }\n" + 
        "   OPTIONAL { ?s db:dosage [db:route [dcterms:title ?administrationRoute]]. }\n" + 
        "   OPTIONAL { ?s db:dosage [db:form [dcterms:title ?administrationForm]]. }\n" + 
        "   OPTIONAL { ?s db:mechanism-of-action [dcterms:description ?mechanismOfAction]. }\n" + 
        "   OPTIONAL { ?s db:ddi-interactor-in [dcterms:title ?interactingDrug]. }\n" + 
        "   OPTIONAL { ?s db:food-interaction [rdf:value ?foodWarning]. }\n" + 
        "   OPTIONAL { ?s db:group [bio2rdf:identifier ?legalStatus]. }\n" + 
        "   OPTIONAL { \n" + 
        "      ?s db:manufacturer ?manufacturer.\n" + 
        "      ?manufacturer rdf:value ?manufacturerName.\n" + 
        "    }\n" + 
        "   FILTER ( ?s = <%s> )\n" + 
        " }\n")
    );
  }
}
