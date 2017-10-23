package org.metadatacenter.schemaorg.pipeline.mapping;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;

public abstract class MapNodeConverter {

  public String transform(MapNode mapNode) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    transform(mapNode, out);
    return out.toString();
  }

  public abstract void transform(MapNode mapNode, OutputStream out);

  public abstract String getName();
}
