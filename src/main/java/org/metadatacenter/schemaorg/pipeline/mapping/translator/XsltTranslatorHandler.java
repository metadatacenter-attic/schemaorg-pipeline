package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.ObjectNode;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.PathNode;
import org.metadatacenter.schemaorg.pipeline.mapping.TranslatorHandler;
import com.google.common.collect.Maps;

public class XsltTranslatorHandler extends TranslatorHandler {

  @Override
  public void translate(ObjectNode objectNode, OutputStream out) {
    final XsltLayout xsltLayout = new XsltLayout();
    init(objectNode, xsltLayout);
    try (PrintWriter printer = new PrintWriter(out)) {
      printer.println(xsltLayout.toString());
    }
  }

  private void init(ObjectNode objectNode, final XsltLayout xsltLayout) {
    translateObjectNode("instance", objectNode, xsltLayout);
    visit(objectNode, xsltLayout);
  }

  private void visit(MapNode mapNode, XsltLayout xsltLayout) {
    for (Iterator<String> iter = mapNode.attributeNames(); iter.hasNext();) {
      String attrName = iter.next();
      MapNode node = mapNode.get(attrName);
      if (node.isObjectNode()) {
        translateObjectNode(attrName, (ObjectNode) node, xsltLayout);
        visit(node, xsltLayout);
      } else if (node.isPathNode()) {
        translatePathNode(attrName, (PathNode) node, xsltLayout);
      } else if (node.isArrayNode()) {
        for (Iterator<MapNode> arrIter = node.elements(); arrIter.hasNext();) {
          MapNode item = arrIter.next();
          if (item.isObjectNode()) {
            translateObjectNode(attrName, (ObjectNode) item, xsltLayout);
            visit(item, xsltLayout);
          } else if (item.isPathNode()) {
            translatePathNode(attrName, (PathNode) item, xsltLayout);
          }
        }
      }
    }
  }

  private void translatePathNode(String attrName, PathNode pathNode, XsltLayout xsltLayout) {
    xsltLayout.addPathTemplate(attrName, pathNode.getAbsolutePath());
  }

  private void translateObjectNode(String attrName, ObjectNode objectNode, XsltLayout xsltLayout) {
    String objectPath = objectNode.getAbsolutePath();
    String objectType = objectNode.getType();
    Map<String, String> objectMap = toMapOfString(objectNode.getObjectMap());
    xsltLayout.addObjectTemplate(attrName, objectPath, objectType, objectMap);
  }

  private static Map<String, String> toMapOfString(Map<String, MapNode> objectMap) {
    Map<String, String> mapOfString = Maps.newLinkedHashMap();
    for (String attrName : objectMap.keySet()) {
      if (!attrName.equals(ObjectNode.OBJECT_TYPE_KEYWORD)) {
        MapNode mapNode = objectMap.get(attrName);
        mapOfString.put(attrName, mapNode.getValue());
      }
    }
    return mapOfString;
  }
}
