package org.metadatacenter.schemaorg.pipeline.experimental;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;

public class BioPortalRecommenderTest {

  @Test
  public void canRecommendEntities() {
    BioPortalRecommender recommender = new BioPortalRecommender();
    Optional<Map<String, String>> result1 = recommender.find("Arrhythmia").stream().findFirst();
    Optional<Map<String, String>> result2 = recommender.find("Cardiovascular Diseases").stream().findFirst();
    Optional<Map<String, String>> result3 = recommender.find("Heart Arrest").stream().findFirst();
    Optional<Map<String, String>> result4 = recommender.find("Heart Diseases").stream().findFirst();
    Optional<Map<String, String>> result5 = recommender.find("Myocardial Infarction").stream().findFirst();
    Optional<Map<String, String>> result6 = recommender.find("Ventricular Fibrillation").stream().findFirst();
    Optional<Map<String, String>> result7 = recommender.find("Cerebral Anoxia").stream().findFirst();
    // Asserts
    assertThat(result1.isPresent(), equalTo(true));
    assertThat(result1.get().get(TermLookup.CONCEPT_IRI), equalTo("http://purl.bioontology.org/ontology/CSP/1393-3277"));
    assertThat(result2.isPresent(), equalTo(true));
    assertThat(result2.get().get(TermLookup.CONCEPT_IRI), equalTo("http://purl.bioontology.org/ontology/MESH/D002318"));
    assertThat(result3.isPresent(), equalTo(true));
    assertThat(result3.get().get(TermLookup.CONCEPT_IRI), equalTo("http://purl.bioontology.org/ontology/MESH/D006323"));
    assertThat(result4.isPresent(), equalTo(true));
    assertThat(result4.get().get(TermLookup.CONCEPT_IRI), equalTo("http://purl.bioontology.org/ontology/MESH/D006331"));
    assertThat(result5.isPresent(), equalTo(true));
    assertThat(result5.get().get(TermLookup.CONCEPT_IRI), equalTo("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C27996"));
    assertThat(result6.isPresent(), equalTo(true));
    assertThat(result6.get().get(TermLookup.CONCEPT_IRI), equalTo("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#C50799"));
    assertThat(result7.isPresent(), equalTo(true));
    assertThat(result7.get().get(TermLookup.CONCEPT_IRI), equalTo("http://id.loc.gov/authorities/subjects/sh85022083"));
  }
}
