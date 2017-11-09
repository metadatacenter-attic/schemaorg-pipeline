package org.metadatacenter.schemaorg.pipeline.alma.databind.node;

public class MapNodeFactory {

  public PathNode pathNode(String parent, String path) {
    return new PathNode(parent, path);
  }

  public PathNode pathNode(String path) {
    return new PathNode(path);
  }

  public ConstantNode constantNode(String value) {
    return new ConstantNode(value);
  }

  public ObjectNode objectNode(String parent, String path) {
    return new ObjectNode(parent, path, this);
  }

  public ObjectNode objectNode(String path) {
    return new ObjectNode(path, this);
  }

  public ArrayNode arrayNode() {
    return new ArrayNode(this);
  }
}
