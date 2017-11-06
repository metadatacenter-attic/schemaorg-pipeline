package org.metadatacenter.schemaorg.pipeline.mapping;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.ObjectNode;

public abstract class TranslatorHandler {

  public String translate(ObjectNode objectNode) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    translate(objectNode, out);
    return out.toString();
  }

  public abstract void translate(ObjectNode objectNode, OutputStream out);
}
