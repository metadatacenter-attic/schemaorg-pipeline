package org.metadatacenter.schemaorg.pipeline.api;

public interface Pipe<I, O> {

  O run(I input);
  
  default <R> Pipe<I, R> pipe(Pipe<O, R> pipe) {
    return input -> pipe.run(run(input));
  }
}
