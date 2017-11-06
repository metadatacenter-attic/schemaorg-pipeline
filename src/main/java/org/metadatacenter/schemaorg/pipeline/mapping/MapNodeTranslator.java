package org.metadatacenter.schemaorg.pipeline.mapping;

import org.metadatacenter.schemaorg.pipeline.alma.databind.AttributeMapper;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.ObjectNode;

public class MapNodeTranslator {

  public static String translate(TranslatorHandler handler, final String mappingString) {
    final AttributeMapper mapper = new AttributeMapper();
    ObjectNode objectNode = mapper.readText(mappingString);
    return translate(handler, objectNode);
  }

  public static String translate(TranslatorHandler handler, final ObjectNode objectNode) {
    return handler.translate(objectNode);
  }
}
