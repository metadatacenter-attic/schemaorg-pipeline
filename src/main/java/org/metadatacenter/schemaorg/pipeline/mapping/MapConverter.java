package org.metadatacenter.schemaorg.pipeline.mapping;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.SparqlConstructConverterFactory;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.XsltConverterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapConverter {

  private static final Logger logger = LoggerFactory.getLogger(MapConverter.class);

  private final MapNodeConverterFactoryRegistry registry;

  public MapConverter(@Nonnull MapNodeConverterFactoryRegistry registry) {
    this.registry = checkNotNull(registry);
  }

  public static MapConverter newInstance() {
    MapNodeConverterFactoryRegistry registry = new MapNodeConverterFactoryRegistry();
    registry.registerConverterFactory("sparql-construct", new SparqlConstructConverterFactory());
    registry.registerConverterFactory("xslt", new XsltConverterFactory());
    return new MapConverter(registry);
  }

  public MapNodeConverter use(String converterName) throws ConverterNotFoundException {
    return use(converterName, new EmptyParameters());
  }

  public MapNodeConverter use(String converterName, Parameters parameters)
      throws ConverterNotFoundException {
    MapNodeConverterFactory converterFactory = registry.getConverterFactory(converterName);
    MapNodeConverter converter = converterFactory.createConverter(parameters);
    logger.info("Use converter {\"converterName\":\"{}\", \"parameters\":{}}", converter.getName(), parameters);
    return converter;
  }

  public String convert(MapNode mapNode, MapNodeConverter converter) {
    return converter.transform(mapNode);
  }

  class EmptyParameters extends Parameters {
    public EmptyParameters() {
      super();
    }
  }
}
