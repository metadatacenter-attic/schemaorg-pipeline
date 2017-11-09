package org.metadatacenter.schemaorg.pipeline.alma.databind.node;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;

public abstract class MapNode implements Iterable<MapNode> {

  protected final static List<MapNode> NO_NODES = Lists.newArrayList();

  public Iterator<String> attributeNames() {
    return Collections.emptyIterator();
  }

  public MapNode get(String attrName) {
    return null;
  }

  public boolean has(String attrName) {
    return get(attrName) != null;
  }

  public boolean isPathNode() {
    return false;
  }

  public boolean isConstantNode() {
    return false;
  }

  public boolean isObjectNode() {
    return false;
  }

  public boolean isArrayNode() {
    return false;
  }

  public Iterator<MapNode> elements() {
    return NO_NODES.iterator();
  }

  @Override
  public Iterator<MapNode> iterator() {
    return elements();
  }

  public abstract String getValue();
}
