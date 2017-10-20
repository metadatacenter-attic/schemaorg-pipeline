package org.metadatacenter.schemaorg.pipeline.mapping.converter;

import org.metadatacenter.schemaorg.pipeline.mapping.Parameters;

public class SparqlParameters extends Parameters {

  public SparqlParameters() {
    super();
  }

  public void setInstanceIri(String instanceIri) {
    set("instanceIri", instanceIri);
  }

  public void addPrefix(String prefixLabel, String namespace) {
    String prefixDefinition = String.format("%s: <%s>", prefixLabel, namespace);
    add("prefix", prefixDefinition);
  }
}
