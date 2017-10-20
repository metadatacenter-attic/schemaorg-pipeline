package org.metadatacenter.schemaorg.pipeline.mapping;

public class ConverterNotFoundException extends Exception {

  static final long serialVersionUID = 1L;

  private final String converterName;
  private final MapNodeConverterFactoryRegistry registry;

  public ConverterNotFoundException(String converterName,
      MapNodeConverterFactoryRegistry registry) {
    this.converterName = converterName;
    this.registry = registry;
  }

  @Override
  public String getMessage() {
    return String.format("The converter '%s' was not found. The system only recognizes: %s",
        converterName, registry.getConverterNames());
  }
}
