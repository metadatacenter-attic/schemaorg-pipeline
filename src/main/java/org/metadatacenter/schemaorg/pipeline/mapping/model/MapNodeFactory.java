package org.metadatacenter.schemaorg.pipeline.mapping.model;

import java.util.List;

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

  public PairNode pairNode(String value1, String value2) {
    return new PairNode(value1, value2);
  }

  public FunctionNode functionNode(String name, List<String> args) {
    return new FunctionNode(name, args);
  }

  public ObjectNode objectNode(String parent, String path) {
    return new ObjectNode(parent, path, this);
  }

  public ObjectNode objectNode(String path) {
    return new ObjectNode(path, this);
  }

  public ObjectNode rootNode() {
    return new ObjectNode("", this);
  }

  public ArrayNode arrayNode() {
    return new ArrayNode(this);
  }
}
