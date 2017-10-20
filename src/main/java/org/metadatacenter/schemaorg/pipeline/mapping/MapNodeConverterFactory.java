package org.metadatacenter.schemaorg.pipeline.mapping;

public interface MapNodeConverterFactory {

  /**
   * Creates a map node converter
   * 
   * @param parameters extra parameters required to create a converter
   * @return The converter created by this factory
   */
  MapNodeConverter createConverter(Parameters parameters);
}
