package org.metadatacenter.schemaorg.pipeline.experimental;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.Optional;
import org.junit.BeforeClass;
import org.junit.Test;

public class IdentifiersResolverTest {

  private static IdentifiersResolver resolver;

  @BeforeClass
  public static void setUp() {
    resolver = new IdentifiersResolver();
  }

  @Test
  public void shouldResolveDrugBankId() {
    String id = "DB00088";
    Optional<String> resolvedId = resolver.expand(id, "drugbank");
    // Asserts
    assertThat(resolvedId.isPresent(), equalTo(true));
    assertThat(resolvedId.get(), equalTo("http://identifiers.org/drugbank/DB00088"));
  }

  @Test
  public void shouldResolveClinicalTrialsId() {
    String id = "NCT00002126";
    Optional<String> resolvedId = resolver.expand(id, "clinicaltrials");
    assertThat(resolvedId.isPresent(), equalTo(true));
    assertThat(resolvedId.get(), equalTo("http://identifiers.org/clinicaltrials/NCT00002126"));
  }

  @Test
  public void shouldFailResolveClinicalTrialsId_MismatchNamespace() {
    String id = "NCT00002126";
    Optional<String> resolvedId = resolver.expand(id, "drugbank");
    assertThat(resolvedId.isPresent(), equalTo(false));
  }

  @Test
  public void shouldFailResolveClinicalTrialsId_MismatchPattern() {
    String id = "NCT002126";
    Optional<String> resolvedId = resolver.expand(id, "clinicaltrials");
    assertThat(resolvedId.isPresent(), equalTo(false));
  }
}
