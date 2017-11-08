package org.metadatacenter.schemaorg.pipeline.experimental;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Optional;
import org.junit.BeforeClass;
import org.junit.Test;

public class IdentifiersAutoCompleteTest {

  private static IdentifiersAutoComplete autoComplete;

  @BeforeClass
  public static void setUp() {
    autoComplete = IdentifiersAutoComplete.newInstance();
  }

  @Test
  public void shouldAutoComplete_DrugBank_Variant1() {
    String id = "DB00088";
    Optional<String> resolvedId = autoComplete.resolve(id, "drugbank");
    // Asserts
    assertThat(resolvedId.isPresent(), equalTo(true));
    assertThat(resolvedId.get(), equalTo("http://identifiers.org/drugbank/DB00088"));
  }

  @Test
  public void shouldAutoComplete_DrugBank_Variant2() {
    String id = "drugbank:DB00088";
    Optional<String> resolvedId = autoComplete.resolve(id, "drugbank");
    // Asserts
    assertThat(resolvedId.isPresent(), equalTo(true));
    assertThat(resolvedId.get(), equalTo("http://identifiers.org/drugbank/DB00088"));
  }

  @Test
  public void shouldAutoComplete_ClinicalTrials() {
    String id = "NCT00002126";
    Optional<String> resolvedId = autoComplete.resolve(id, "clinicaltrials");
    assertThat(resolvedId.isPresent(), equalTo(true));
    assertThat(resolvedId.get(), equalTo("http://identifiers.org/clinicaltrials/NCT00002126"));
  }

  @Test
  public void shouldFailedAutoComplete_MismatchNamespace() {
    String id = "NCT00002126";
    Optional<String> resolvedId = autoComplete.resolve(id, "drugbank");
    assertThat(resolvedId.isPresent(), equalTo(false));
  }

  @Test
  public void shouldFailedAutoComplete_MismatchPattern() {
    String id = "NCT002126";
    Optional<String> resolvedId = autoComplete.resolve(id, "clinicaltrials");
    assertThat(resolvedId.isPresent(), equalTo(false));
  }
}
