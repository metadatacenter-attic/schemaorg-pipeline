package org.metadatacenter.schemaorg.pipeline.alma.databind.node;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

public class ArrayNode extends MapNode {

  private final List<MapNode> nodes = Lists.newArrayList();
  private final MapNodeFactory factory;

  public ArrayNode(@Nonnull MapNodeFactory factory) {
    this.factory = checkNotNull(factory);
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

  public void add(MapNode mapNode) {
    nodes.add(mapNode);
  }

  public MapNode get(int index) {
    return nodes.get(index);
  }

  @Override
  public boolean isArrayNode() {
    return true;
  }

  @Override
  public String asText() {
    return nodes.toString();
  }

  @Override
  public Iterator<MapNode> elements() {
    return nodes.iterator();
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodes);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ArrayNode)) {
      return false;
    }
    ArrayNode other = (ArrayNode) obj;
    return Objects.equals(nodes, other.nodes);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("nodes", nodes)
        .toString();
  }
}
