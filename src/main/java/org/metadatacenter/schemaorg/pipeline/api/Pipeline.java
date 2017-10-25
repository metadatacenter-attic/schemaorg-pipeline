package org.metadatacenter.schemaorg.pipeline.api;

public class Pipeline {

  public static Pipe<String, String> create() {
    return Object::toString;
  }
}