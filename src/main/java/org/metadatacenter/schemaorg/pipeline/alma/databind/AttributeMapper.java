package org.metadatacenter.schemaorg.pipeline.alma.databind;

import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNodeFactory;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.ObjectNode;

public class AttributeMapper {

  private final MapNodeFactory nodeFactory = new MapNodeFactory();

  public MapNode readText(String text) {
    return parse(text);
  }

  private MapNode parse(String text) {
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

    MapNodeProvider mapNodeProvider = new MapNodeProvider("root");
    root.accept(mapNodeProvider);
    return mapNodeProvider.getMapNode();
  }

  private class MapNodeProvider implements SectionVisitor {

    private final ObjectNode mapNode;

    public MapNodeProvider(String rootAttr) {
      this.mapNode = nodeFactory.objectNode(rootAttr);
    }

    @Override
    public void visit(Section section) {
      for (Section child : section.getChildren()) {
        String text = child.getText();
        String attr = MapString.read(text).key();
        String refAttr = MapString.read(text).value();
        if (!child.hasChildren()) {
          mapNode.put(attr, refAttr);
        } else {
          MapNodeProvider mapNodeProvider = new MapNodeProvider(refAttr);
          child.accept(mapNodeProvider);
          mapNode.put(MapString.read(text).key(), mapNodeProvider.getMapNode());
        }
      }
    }

    public MapNode getMapNode() {
      return mapNode;
    }
  }
}
