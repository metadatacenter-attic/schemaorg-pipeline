package org.metadatacenter.schemaorg.pipeline.alma.databind;

import org.metadatacenter.schemaorg.pipeline.alma.databind.node.ArrayNode;
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

    ObjectNode rootNode = nodeFactory.objectNode("root");
    MapNodeProvider mapNodeProvider = new MapNodeProvider(rootNode);
    root.accept(mapNodeProvider);
    return mapNodeProvider.getMapNode();
  }

  private class MapNodeProvider implements SectionVisitor {

    private final ObjectNode objectNode;

    public MapNodeProvider(ObjectNode objectNode) {
      this.objectNode = objectNode;
    }

    @Override
    public void visit(Section section) {
      for (Section subSection : section.getSubSections()) {
        String text = subSection.getText();
        String attrName = MapString.read(text).key();
        String mappedData = MapString.read(text).value();
        MapNode mapNode = null;
        if (subSection.hasSubSections()) {
          mapNode = createObjectNode(subSection, mappedData);
        } else {
          mapNode = createPathOrConstantNode(mappedData);
        }
        storeNode(attrName, mapNode);
      }
    }

    private void storeNode(String attrName, MapNode mapNode) {
      MapNode foundNode = objectNode.get(attrName);
      if (foundNode == null) {
        objectNode.put(attrName, mapNode);
      } else {
        ArrayNode arrayNode = getOrCreateArrayNode(foundNode);
        arrayNode.add(mapNode);
        objectNode.put(attrName, arrayNode);
      }
    }

    private MapNode createObjectNode(Section subSection, String mappedData) {
      ObjectNode parentNode = nodeFactory.objectNode(mappedData);
      MapNodeProvider mapNodeProvider = new MapNodeProvider(parentNode);
      subSection.accept(mapNodeProvider);
      return mapNodeProvider.getMapNode();
    }

    public MapNode createPathOrConstantNode(String mappedData) {
      MapNode mapNode = null;
      if (mappedData.startsWith("/")) {
        mapNode = nodeFactory.pathNode(mappedData);
      } else {
        mapNode = nodeFactory.constantNode(mappedData);
      }
      return mapNode;
    }

    private ArrayNode getOrCreateArrayNode(MapNode foundNode) {
      ArrayNode arrayNode = null;
      if (foundNode.isArrayNode()) {
        arrayNode = (ArrayNode) foundNode;
      } else {
        arrayNode = nodeFactory.arrayNode();
        arrayNode.add(foundNode);
      }
      return arrayNode;
    }

    public MapNode getMapNode() {
      return objectNode;
    }
  }
}
