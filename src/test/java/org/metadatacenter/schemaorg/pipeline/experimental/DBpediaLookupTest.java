package org.metadatacenter.schemaorg.pipeline.experimental;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Optional;
import org.junit.Test;

public class DBpediaLookupTest {

  @Test
  public void canFindIdentifiers() {
    NameLookup dbpediaLookup = new DBpediaLookup();
    Optional<String> result1 = dbpediaLookup.find("Stanford University");
    Optional<String> result2 = dbpediaLookup.find("Stanford University Medical Center");
    Optional<String> result3 = dbpediaLookup.find("California");
    Optional<String> result4 = dbpediaLookup.find("united states");
    Optional<String> result5 = dbpediaLookup.find("Colorectal cancer");
    Optional<String> result6 = dbpediaLookup.find("lepirudin");
    // Asserts
    assertThat(result1.isPresent(), equalTo(true));
    assertThat(result1.get(), equalTo("http://dbpedia.org/resource/Stanford_University"));
    assertThat(result2.isPresent(), equalTo(true));
    assertThat(result2.get(), equalTo("http://dbpedia.org/resource/Stanford_University_Medical_Center"));
    assertThat(result3.isPresent(), equalTo(true));
    assertThat(result3.get(), equalTo("http://dbpedia.org/resource/California"));
    assertThat(result4.isPresent(), equalTo(true));
    assertThat(result4.get(), equalTo("http://dbpedia.org/resource/United_States"));
    assertThat(result5.isPresent(), equalTo(true));
    assertThat(result5.get(), equalTo("http://dbpedia.org/resource/Colorectal_cancer"));
    assertThat(result6.isPresent(), equalTo(true));
    assertThat(result6.get(), equalTo("http://dbpedia.org/resource/Lepirudin"));
  }
}
