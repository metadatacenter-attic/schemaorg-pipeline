package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import java.util.List;
import org.metadatacenter.schemaorg.pipeline.mapping.model.ObjectNode;
import com.google.common.collect.Lists;

public final class ReservedAttributes {

  public static final String ID = "@id";
  public static final String TYPE = "@type";
  public static final String PREFIX = "@prefix";

  public static final List<String> ALL_RESERVED_ATTRIBUTES = Lists.newArrayList();
  static {
    ALL_RESERVED_ATTRIBUTES.add(ID);
    ALL_RESERVED_ATTRIBUTES.add(TYPE);
    ALL_RESERVED_ATTRIBUTES.add(PREFIX);
  }

  public static boolean isId(String s) {
    return ID.equals(s);
  }

  public static boolean isType(String s) {
    return TYPE.equals(s);
  }

  public static boolean isPrefix(String s) {
    return PREFIX.equals(s);
  }

  public static boolean isReserved(String s) {
    return ALL_RESERVED_ATTRIBUTES.contains(s);
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
