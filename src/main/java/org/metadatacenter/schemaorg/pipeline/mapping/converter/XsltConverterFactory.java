package org.metadatacenter.schemaorg.pipeline.mapping.converter;

import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeConverter;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeConverterFactory;
import org.metadatacenter.schemaorg.pipeline.mapping.Parameters;

public class XsltConverterFactory implements MapNodeConverterFactory {

  @Override
  public MapNodeConverter createConverter(Parameters parameters) {
    XsltConverter converter = new XsltConverter();
    converter.setDocumentRoot(parameters.get("documentRoot"));
    return converter;
  }
}
