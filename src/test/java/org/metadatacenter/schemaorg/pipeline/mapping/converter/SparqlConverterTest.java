package org.metadatacenter.schemaorg.pipeline.mapping.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.alma.databind.AttributeMapper;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.SparqlConstructConverter;

public class SparqlConverterTest {

  @Test
  public void shouldConvertToSparqlConstruct() {
    final String mapping =
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
        "drugCost:             /db:product\n" + 
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
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(mapping);
    SparqlConstructConverter converter = new SparqlConstructConverter();
    converter.setInstanceIri("http://bio2rdf.org/drugbank:DB00112");
    converter.addPrefix("schema: <http://schema.org/>");
    converter.addPrefix("rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
    converter.addPrefix("db: <http://bio2rdf.org/drugbank_vocabulary:>");
    converter.addPrefix("bio2rdf: <http://bio2rdf.org/bio2rdf_vocabulary:>");
    // Assertion
    assertThat(converter.transform(mapNode), equalTo(
        "PREFIX schema: <http://schema.org/>\n" +
        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
        "PREFIX db: <http://bio2rdf.org/drugbank_vocabulary:>\n" +
        "PREFIX bio2rdf: <http://bio2rdf.org/bio2rdf_vocabulary:>\n" +
        "\n" +
        "CONSTRUCT { \n" + 
        "   ?__instance__ a schema:Drug.\n" + 
        "   ?__instance__ schema:name ?name.\n" + 
        "   ?__instance__ schema:description ?description.\n" + 
        "   ?__instance__ schema:identifier ?identifier.\n" + 
        "   ?__instance__ schema:url ?url.\n" + 
        "   ?__instance__ schema:sameAs ?sameAs.\n" + 
        "   ?__instance__ schema:proprietaryName ?proprietaryName.\n" + 
        "   ?__instance__ schema:nonProprietaryName ?nonProprietaryName.\n" + 
        "   ?__instance__ schema:clinicalPharmacology ?clinicalPharmacology.\n" + 
        "   ?__instance__ schema:drugClass ?drugClass.\n" + 
        "   ?drugCost a schema:DrugCost.\n" + 
        "   ?drugCost schema:costPerUnit ?drugCostCostPerUnit.\n" + 
        "   ?drugCost schema:costCurrency 'USD'.\n" + 
        "   ?drugCost schema:drugUnit ?drugCostDrugUnit.\n" + 
        "   ?availableStrength a schema:DrugStrength.\n" + 
        "   ?availableStrength schema:description ?availableStrengthDescription.\n" + 
        "   ?__instance__ schema:administrationRoute ?administrationRoute.\n" + 
        "   ?__instance__ schema:administrationForm ?administrationForm.\n" + 
        "   ?__instance__ schema:mechanismOfAction ?mechanismOfAction.\n" + 
        "   ?__instance__ schema:interactingDrug ?interactingDrug.\n" + 
        "   ?__instance__ schema:foodWarning ?foodWarning.\n" + 
        "   ?__instance__ schema:legalStatus ?legalStatus.\n" + 
        "   ?manufacturer a schema:Organization.\n" + 
        "   ?manufacturer schema:name ?manufacturerName.\n" + 
        " }\n" + 
        "WHERE { \n" + 
        "   OPTIONAL { ?__instance__ dcterms:title ?name. }\n" + 
        "   OPTIONAL { ?__instance__ dcterms:description ?description. }\n" + 
        "   OPTIONAL { ?__instance__ dcterms:identifier ?identifier. }\n" + 
        "   OPTIONAL { ?__instance__ bio2rdf:uri ?url. }\n" + 
        "   OPTIONAL { ?__instance__ rdfs:seeAlso ?sameAs. }\n" + 
        "   OPTIONAL { ?__instance__ db:brand [dcterms:title ?proprietaryName]. }\n" + 
        "   OPTIONAL { ?__instance__ db:synonym [dcterms:title ?nonProprietaryName]. }\n" + 
        "   OPTIONAL { ?__instance__ db:pharmacodynamics [dcterms:description ?clinicalPharmacology]. }\n" + 
        "   OPTIONAL { ?__instance__ db:category [dcterms:title ?drugClass]. }\n" + 
        "   OPTIONAL { \n" + 
        "      ?__instance__ db:product ?drugCost.\n" + 
        "      ?drugCost db:price ?drugCostCostPerUnit.\n" + 
        "      ?drugCost dcterms:title ?drugCostDrugUnit.\n" + 
        "    }\n" + 
        "   OPTIONAL { \n" + 
        "      ?__instance__ db:dosage ?availableStrength.\n" + 
        "      ?availableStrength dcterms:title ?availableStrengthDescription.\n" + 
        "    }\n" + 
        "   OPTIONAL { ?__instance__ db:dosage [db:route [dcterms:title ?administrationRoute]]. }\n" + 
        "   OPTIONAL { ?__instance__ db:dosage [db:form [dcterms:title ?administrationForm]]. }\n" + 
        "   OPTIONAL { ?__instance__ db:mechanism-of-action [dcterms:description ?mechanismOfAction]. }\n" + 
        "   OPTIONAL { ?__instance__ db:ddi-interactor-in [dcterms:title ?interactingDrug]. }\n" + 
        "   OPTIONAL { ?__instance__ db:food-interaction [rdf:value ?foodWarning]. }\n" + 
        "   OPTIONAL { ?__instance__ db:group [bio2rdf:identifier ?legalStatus]. }\n" + 
        "   OPTIONAL { \n" + 
        "      ?__instance__ db:manufacturer ?manufacturer.\n" + 
        "      ?manufacturer rdf:value ?manufacturerName.\n" + 
        "    }\n" + 
        "   FILTER ( ?__instance__ = <http://bio2rdf.org/drugbank:DB00112> )\n" + 
        " }\n")
    );
  }
}
