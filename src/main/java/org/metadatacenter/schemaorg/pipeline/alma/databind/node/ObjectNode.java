package org.metadatacenter.schemaorg.pipeline.alma.databind.node;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

public class ObjectNode extends MapNode {

  private final String path;
  private final Map<String, MapNode> children = Maps.newLinkedHashMap();
  private final MapNodeFactory factory;

  public ObjectNode(@Nonnull String path, @Nonnull MapNodeFactory factory) {
    this.path = checkNotNull(path);
    this.factory = checkNotNull(factory);
  }

  public Iterator<String> attributeNames() {
    return children.keySet().iterator();
  }

  public final PathNode pathNode(String v) {
    return factory.pathNode(v);
  }

  public final ConstantNode constantNode(String v) {
    return factory.constantNode(v);
  }

  public final ObjectNode objectNode(String v) {
    return factory.objectNode(v);
  }

  public final ArrayNode arrayNode() {
    return factory.arrayNode();
  }

  public void put(String attrName, MapNode mapNode) {
    children.put(attrName, mapNode);
  }

  public MapNode get(String attrName) {
    return children.get(attrName);
  }

  @Override
  public boolean isObjectNode() {
    return true;
  }

  @Override
  public String asText() {
    return path;
  }

  @Override
  public Iterator<MapNode> iterator() {
    return children.values().iterator();
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, children);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ObjectNode)) {
      return false;
    }
    ObjectNode other = (ObjectNode) obj;
    return Objects.equals(path, other.path) && Objects.equals(children, other.children);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("path", path)
        .add("children", children)
        .toString();
  }
}
