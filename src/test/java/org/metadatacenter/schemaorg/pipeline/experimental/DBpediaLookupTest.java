package org.metadatacenter.schemaorg.pipeline.experimental;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;

public class DBpediaLookupTest {

  @Test
  public void canFindIdentifiers() {
    TermLookup dbpediaLookup = new DBpediaLookup();
    Optional<Map<String, String>> result1 = dbpediaLookup.find("Stanford University").stream().findFirst();
    Optional<Map<String, String>> result2 = dbpediaLookup.find("Stanford University Medical Center").stream().findFirst();
    Optional<Map<String, String>> result3 = dbpediaLookup.find("California").stream().findFirst();
    Optional<Map<String, String>> result4 = dbpediaLookup.find("united states").stream().findFirst();
    Optional<Map<String, String>> result5 = dbpediaLookup.find("Colorectal cancer").stream().findFirst();
    Optional<Map<String, String>> result6 = dbpediaLookup.find("lepirudin").stream().findFirst();
    // Asserts
    assertThat(result1.isPresent(), equalTo(true));
    assertThat(result1.get().get(TermLookup.CONCEPT_IRI), equalTo("http://dbpedia.org/resource/Stanford_University"));
    assertThat(result2.isPresent(), equalTo(true));
    assertThat(result2.get().get(TermLookup.CONCEPT_IRI), equalTo("http://dbpedia.org/resource/Stanford_University_Medical_Center"));
    assertThat(result3.isPresent(), equalTo(true));
    assertThat(result3.get().get(TermLookup.CONCEPT_IRI), equalTo("http://dbpedia.org/resource/California"));
    assertThat(result4.isPresent(), equalTo(true));
    assertThat(result4.get().get(TermLookup.CONCEPT_IRI), equalTo("http://dbpedia.org/resource/United_States"));
    assertThat(result5.isPresent(), equalTo(true));
    assertThat(result5.get().get(TermLookup.CONCEPT_IRI), equalTo("http://dbpedia.org/resource/Colorectal_cancer"));
    assertThat(result6.isPresent(), equalTo(true));
    assertThat(result6.get().get(TermLookup.CONCEPT_IRI), equalTo("http://dbpedia.org/resource/Lepirudin"));
  }
}
