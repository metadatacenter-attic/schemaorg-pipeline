package org.metadatacenter.schemaorg.pipeline.caml.databind;

import org.metadatacenter.schemaorg.pipeline.caml.databind.node.ArrayNode;
import org.metadatacenter.schemaorg.pipeline.caml.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.caml.databind.node.MapNodeFactory;
import org.metadatacenter.schemaorg.pipeline.caml.databind.node.ObjectNode;

/*package*/ class MapNodeTreeBuilder implements SectionVisitor {

  private final ObjectNode objectNode;
  private final MapNodeFactory nodeFactory;

  public MapNodeTreeBuilder(ObjectNode objectNode) {
    this.objectNode = objectNode;
    this.nodeFactory = objectNode.getNodeFactory();
  }

  public static MapNodeTreeBuilder newInstance() {
    ObjectNode rootNode = new ObjectNode("", new MapNodeFactory());
    return new MapNodeTreeBuilder(rootNode);
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
    String parentPath = objectNode.getAbsolutePath();
    ObjectNode parentNode = nodeFactory.objectNode(parentPath, mappedData);
    MapNodeTreeBuilder treeBuilder = new MapNodeTreeBuilder(parentNode);
    subSection.accept(treeBuilder);
    return treeBuilder.build();
  }

  public MapNode createPathOrConstantNode(String mappedData) {
    MapNode mapNode = null;
    if (mappedData.startsWith("/")) {
      String parentPath = objectNode.getAbsolutePath();
      mapNode = nodeFactory.pathNode(parentPath, mappedData);
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

  public ObjectNode build() {
    return objectNode;
  }
}
