package org.metadatacenter.schemaorg.pipeline.mapping.converter;

import org.metadatacenter.schemaorg.pipeline.mapping.Parameters;

public class XsltParameters extends Parameters {

  public XsltParameters() {
    super();
  }

  public void setDocumentRoot(String documentRoot) {
    set("documentRoot", documentRoot);
  }
}
