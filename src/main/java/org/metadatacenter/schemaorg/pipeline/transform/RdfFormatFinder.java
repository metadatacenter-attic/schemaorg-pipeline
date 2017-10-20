package org.metadatacenter.schemaorg.pipeline.transform;

import java.util.Map;
import org.eclipse.rdf4j.rio.RDFFormat;
import com.google.common.collect.Maps;

public class RdfFormatFinder {

  private static Map<String, RDFFormat> library = Maps.newHashMap();
  static {
    library.put(RDFFormat.RDFXML.getName(), RDFFormat.RDFXML);
    library.put(RDFFormat.NTRIPLES.getName(), RDFFormat.NTRIPLES);
    library.put(RDFFormat.TURTLE.getName(), RDFFormat.TURTLE);
    library.put(RDFFormat.NQUADS.getName(), RDFFormat.NQUADS);
    library.put(RDFFormat.TRIG.getName(), RDFFormat.TRIG);
    library.put(RDFFormat.JSONLD.getName(), RDFFormat.JSONLD);
  }

  public static RDFFormat find(String format, RDFFormat defaultFormat) {
    RDFFormat rdfFormat = library.get(format);
    if (rdfFormat == null) {
      return defaultFormat;
    }
    return rdfFormat;
  }
}
