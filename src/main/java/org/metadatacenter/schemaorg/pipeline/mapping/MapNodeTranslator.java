package org.metadatacenter.schemaorg.pipeline.mapping;

import org.metadatacenter.schemaorg.pipeline.alma.databind.AttributeMapper;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;

public class MapNodeTranslator {

  public static String translate(TranslatorHandler handler, final String mappingString) {
    final AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(mappingString);
    return translate(handler, mapNode);
  }

  public static String translate(TranslatorHandler handler, final MapNode mapNode) {
    return handler.translate(mapNode);
  }
}
