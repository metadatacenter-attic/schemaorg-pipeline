package org.metadatacenter.schemaorg.pipeline.mapmodel;

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

  public MapNodeFactory getNodeFactory() {
    return factory;
  }

  public void add(MapNode mapNode) {
    nodes.add(mapNode);
  }

  public MapNode get(int index) {
    return nodes.get(index);
  }

  public int size() {
    return nodes.size();
  }

  @Override
  public boolean isArrayNode() {
    return true;
  }

  @Override
  public String getValue() {
    StringBuilder valueBuilder = new StringBuilder();
    valueBuilder.append("[");
    boolean needComma = false;
    for (MapNode mapNode : nodes) {
      if (needComma) {
        valueBuilder.append(",");
      }
      needComma = true;
      String value = mapNode.getValue();
      valueBuilder.append(value);
    }
    valueBuilder.append("]");
    return valueBuilder.toString();
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
