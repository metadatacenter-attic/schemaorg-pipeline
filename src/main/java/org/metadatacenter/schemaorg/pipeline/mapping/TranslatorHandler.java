package org.metadatacenter.schemaorg.pipeline.mapping;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;

public abstract class TranslatorHandler {

  public String translate(MapNode mapNode) {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    translate(mapNode, out);
    return out.toString();
  }

  public abstract void translate(MapNode mapNode, OutputStream out);
}
