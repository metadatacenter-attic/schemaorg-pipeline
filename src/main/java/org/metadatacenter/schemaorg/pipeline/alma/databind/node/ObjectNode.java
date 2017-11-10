package org.metadatacenter.schemaorg.pipeline.alma.databind.node;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

public class ObjectNode extends MapNode {

  public static final String OBJECT_ID_KEYWORD = "@id";
  public static final String OBJECT_TYPE_KEYWORD = "@type";

  private final String parent;
  private final String path;
  private final Map<String, MapNode> children = Maps.newLinkedHashMap();
  private final MapNodeFactory factory;

  public ObjectNode(@Nonnull String parent, @Nonnull String path, @Nonnull MapNodeFactory factory) {
    this.parent = checkNotNull(parent);
    this.path = checkNotNull(path);
    this.factory = checkNotNull(factory);
  }

  public ObjectNode(@Nonnull String path, @Nonnull MapNodeFactory factory) {
    this("", path, factory);
  }

  public Iterator<String> attributeNames() {
    return children.keySet().iterator();
  }

  public MapNodeFactory getNodeFactory() {
    return factory;
  }

  public void put(String attrName, MapNode mapNode) {
    children.put(attrName, mapNode);
  }

  public MapNode get(String attrName) {
    return children.get(attrName);
  }

  public Map<String, MapNode> getObjectMap() {
    Map<String, MapNode> objectMap = Maps.newLinkedHashMap();
    for (Iterator<String> iter = attributeNames(); iter.hasNext(); ) {
      String attrName = iter.next();
      objectMap.put(attrName, get(attrName));
    }
    return objectMap;
  }

  public String getId() {
    return children.getOrDefault(OBJECT_ID_KEYWORD, new NullNode()).getValue();
  }

  public String getType() {
    return children.getOrDefault(OBJECT_TYPE_KEYWORD, new NullNode()).getValue();
  }

  @Override
  public boolean isObjectNode() {
    return true;
  }

  public String getRelativePath() {
    return path;
  }

  public String getAbsolutePath() {
    return parent + path;
  }

  @Override
  public String getValue() {
    return getRelativePath();
  }

  @Override
  public Iterator<MapNode> iterator() {
    return children.values().iterator();
  }

  @Override
  public int hashCode() {
    return Objects.hash(parent, path, children);
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
    return Objects.equals(parent, other.parent)
        && Objects.equals(path, other.path)
        && Objects.equals(children, other.children);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("parent", parent)
        .add("path", path)
        .add("children", children)
        .toString();
  }
}
