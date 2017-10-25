package org.metadatacenter.schemaorg.pipeline.mapping.converter;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeConverter;
import com.google.common.collect.Maps;

public class XsltConverter extends MapNodeConverter {

  private static final String VERSION_NUMBER = "1.0";

  private static final String INSTANCE_TYPE = "@type";

  @Override
  public String getName() {
    return String.format("XSLT Simple Converter v%s", VERSION_NUMBER);
  }

  @Override
  public void transform(MapNode mapNode, OutputStream out) {
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
