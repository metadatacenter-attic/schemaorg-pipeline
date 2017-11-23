package org.metadatacenter.schemaorg.pipeline;

public class Pipeline {

  public static Pipe<String, String> create() {
    return Object::toString;
  }
}