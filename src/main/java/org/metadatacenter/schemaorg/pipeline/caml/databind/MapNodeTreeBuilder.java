package org.metadatacenter.schemaorg.pipeline.caml.databind;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        mapNode = createPathOrConstantOrPairNode(mappedData);
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

  public MapNode createPathOrConstantOrPairNode(String mappedData) {
    MapNode mapNode = null;
    if (mappedData.startsWith("/")) {
      mapNode = createPathNode(mappedData);
    } else if (mappedData.startsWith("'") && mappedData.endsWith("'")) {
      mapNode = createConstantNode(mappedData);
    } else if (mappedData.startsWith("(") && mappedData.endsWith(")")) {
      mapNode = createPairNode(mappedData);
    }
    return mapNode;
  }

  private MapNode createPathNode(String value) {
    String parentPath = objectNode.getAbsolutePath();
    return nodeFactory.pathNode(parentPath, value);
  }

  private MapNode createConstantNode(String value) {
    final Pattern constantPattern = Pattern.compile("^'(.*)'$");
    Matcher m = constantPattern.matcher(value);
    if (m.find()) {
      String constant = m.group(1);
      return nodeFactory.constantNode(constant);
    }
    throw new IllegalArgumentException("The string \"" + value + "\" is not a valid constant value");
  }

  private MapNode createPairNode(String value) {
    final Pattern pairPattern = Pattern.compile("^\\('(.*)',\\s*'(.*)'\\)$");
    Matcher m = pairPattern.matcher(value);
    if (m.find()) {
      String value1 = m.group(1);
      String value2 = m.group(2);
      return nodeFactory.pairNode(value1, value2);
    }
    throw new IllegalArgumentException("The string \"" + value + "\" is not a valid pair value");
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
