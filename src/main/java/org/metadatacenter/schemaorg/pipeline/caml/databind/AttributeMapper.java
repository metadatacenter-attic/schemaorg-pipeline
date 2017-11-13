package org.metadatacenter.schemaorg.pipeline.caml.databind;

import org.metadatacenter.schemaorg.pipeline.caml.databind.node.ObjectNode;

public class AttributeMapper {

  public ObjectNode readText(String text) {
    return parse(text);
  }

  private ObjectNode parse(String text) {
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

    MapNodeTreeBuilder treeBuilder = MapNodeTreeBuilder.newInstance();
    root.accept(treeBuilder);
    return treeBuilder.build();
  }
}
