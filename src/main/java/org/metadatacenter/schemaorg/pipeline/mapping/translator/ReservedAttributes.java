package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import org.metadatacenter.schemaorg.pipeline.mapmodel.ObjectNode;

public final class ReservedAttributes {

  public static final String ID = "@id";
  public static final String TYPE = "@type";
  public static final String PREFIX = "@prefix";

  public static boolean isId(String s) {
    return ID.equals(s);
  }

  public static boolean isType(String s) {
    return TYPE.equals(s);
  }

  public static boolean isPrefix(String s) {
    return PREFIX.equals(s);
  }

  public static String getId(final ObjectNode objectNode) {
    return objectNode.get(ID).getValue();
  }

  public static String getType(final ObjectNode objectNode) {
    return objectNode.get(TYPE).getValue();
  }

  public static String getPrefix(final ObjectNode objectNode) {
    return objectNode.get(PREFIX).getValue();
  }
}
