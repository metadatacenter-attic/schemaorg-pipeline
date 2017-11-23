package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeTranslator;
import org.metadatacenter.schemaorg.pipeline.mapping.MappingLanguages;
import org.metadatacenter.schemaorg.pipeline.operation.translate.SparqlConstructTranslatorHandler;
import static org.hamcrest.MatcherAssert.assertThat;

public class RmlToSparqlConstructTest {

  @Test
  public void shouldConvertToSparqlConstruct() {
    final String rmlMapping =
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.\n" + 
        "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.\n" + 
        "@prefix dcterms: <http://purl.org/dc/terms/>.\n" + 
        "@prefix db: <http://bio2rdf.org/drugbank_vocabulary:>.\n" + 
        "@prefix bio2rdf: <http://bio2rdf.org/bio2rdf_vocabulary:>.\n" + 
        "<#Root>\n" +
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Drug\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/dcterms:title\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:description;\n" + 
        "      rr:objectMap [ rml:reference \"/dcterms:description\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:identifier;\n" + 
        "      rr:objectMap [ rml:reference \"/dcterms:identifier\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:url;\n" + 
        "      rr:objectMap [ rml:reference \"/bio2rdf:uri\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:sameAs;\n" + 
        "      rr:objectMap [ rml:reference \"/rdfs:seeAlso\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:proprietaryName;\n" + 
        "      rr:objectMap [ rml:reference \"/db:brand/dcterms:title\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:nonProprietaryName;\n" + 
        "      rr:objectMap [ rml:reference \"/db:synonym/dcterms:title\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:clinicalPharmacology;\n" + 
        "      rr:objectMap [ rml:reference \"/db:pharmacodynamics/dcterms:description\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:drugClass;\n" + 
        "      rr:objectMap [ rml:reference \"/db:category/dcterms:title\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:administrationRoute;\n" + 
        "      rr:objectMap [ rml:reference \"/db:dosage/db:route/dcterms:title\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:administrationForm;\n" + 
        "      rr:objectMap [ rml:reference \"/db:dosage/db:form/dcterms:title\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:mechanismOfAction;\n" + 
        "      rr:objectMap [ rml:reference \"/db:mechanism-of-action/dcterms:description\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:interactingDrug;\n" + 
        "      rr:objectMap [ rml:reference \"/db:ddi-interactor-in/dcterms:title\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:foodWarning;\n" + 
        "      rr:objectMap [ rml:reference \"/db:food-interaction/rdf:value\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:legalStatus;\n" + 
        "      rr:objectMap [ rml:reference \"/db:group/bio2rdf:identifier\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:drugCost;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#DrugCost> ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:availableStrength;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#AvailableStrength> ];\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:manufacturer;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Manufacturer> ];\n" + 
        "   ].\n" +
        "<#DrugCost>\n" +
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/db:product\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:DrugCost\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:costPerUnit;\n" + 
        "      rr:objectMap [ rml:reference \"/db:price\" ]\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:costCurrency;\n" + 
        "      rr:objectMap [ rr:constant \"USD\" ]\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:drugUnit;\n" + 
        "      rr:objectMap [ rml:reference \"/dcterms:title\" ]\n" + 
        "   ].\n" +
        "<#AvailableStrength>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/db:dosage\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:DrugStrength\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:description;\n" + 
        "      rr:objectMap [ rml:reference \"/dcterms:title\" ]\n" + 
        "   ].\n" +
        "<#Manufacturer>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/db:manufacturer\"\n" + 
        "   ];\n" + 
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Organization\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:name;\n" + 
        "      rr:objectMap [ rml:reference \"/rdf:value\" ]\n" + 
        "   ].";
    SparqlConstructTranslatorHandler handler = new SparqlConstructTranslatorHandler();
    
    String output = MapNodeTranslator.translate(handler, rmlMapping, MappingLanguages.RML);
    // Assertion
    assertThat(output.length(), equalTo(2830));
  }
}
