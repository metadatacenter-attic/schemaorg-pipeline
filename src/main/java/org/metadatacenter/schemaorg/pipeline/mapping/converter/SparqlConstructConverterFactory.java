package org.metadatacenter.schemaorg.pipeline.mapping.converter;

import java.util.Iterator;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeConverter;
import org.metadatacenter.schemaorg.pipeline.mapping.MapNodeConverterFactory;
import org.metadatacenter.schemaorg.pipeline.mapping.Parameters;

public class SparqlConstructConverterFactory implements MapNodeConverterFactory {

  @Override
  public MapNodeConverter createConverter(Parameters parameters) {
    SparqlConstructConverter converter = new SparqlConstructConverter();
    converter.setInstanceIri(parameters.get("instanceIri"));
    for (Iterator<String> iter = parameters.iterate("prefix"); iter.hasNext();) {
      converter.addPrefix(iter.next());
    }
    return converter;
  }
}
