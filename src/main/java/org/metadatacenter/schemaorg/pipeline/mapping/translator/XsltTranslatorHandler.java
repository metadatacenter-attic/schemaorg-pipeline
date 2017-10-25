package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapping.TranslatorHandler;
import com.google.common.collect.Maps;

public class XsltTranslatorHandler extends TranslatorHandler {

  private static final String INSTANCE_TYPE = "@type";

  @Override
  public void translate(MapNode mapNode, OutputStream out) {
    final XsltLayout xsltLayout = new XsltLayout();
    init(mapNode, xsltLayout);
    try (PrintWriter printer = new PrintWriter(out)) {
      printer.println(xsltLayout.toString());
    }
  }

  private void init(MapNode mapNode, XsltLayout xsltLayout) {
    String documentType = getType(mapNode);
    xsltLayout.addDocumentType(documentType);
    parse(mapNode, xsltLayout);
  }

  private void parse(MapNode mapNode, XsltLayout xsltLayout) {
    for (Iterator<String> iter = mapNode.attributeNames(); iter.hasNext();) {
      String attrName = iter.next();
      MapNode node = mapNode.get(attrName);
      if (node.isObjectNode()) {
        String path = node.asText();
        String type = getType(node);
        Map<String, String> valueMap = getValueMap(node);
        xsltLayout.addObjectTemplate(attrName, path, type, valueMap);
        parse(node, xsltLayout);
      } else if (node.isPathNode()) {
        String path = node.asText();
        xsltLayout.addPathTemplate(attrName, path);
      }
    }
  }

  private String getType(MapNode node) {
    return node.get(INSTANCE_TYPE).asText();
  }

  private Map<String, String> getValueMap(MapNode node) {
    Map<String, String> valueMap = Maps.newLinkedHashMap();
    for (Iterator<String> iter = node.attributeNames(); iter.hasNext();) {
      String attrName = iter.next();
      if (attrName.equals(INSTANCE_TYPE)) {
        continue;
      }
      String value = node.get(attrName).asText();
      valueMap.put(attrName, value);
    }
    return valueMap;
  }
}
