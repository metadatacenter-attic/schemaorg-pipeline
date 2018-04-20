package org.metadatacenter.schemaorg.pipeline.operation.transform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.rio.rdfxml.RDFXMLWriter;
import com.google.common.base.Charsets;

public class SchemaToRdf {

  public static String transform(String jsonString) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream in = new ByteArrayInputStream(jsonString.getBytes(Charsets.UTF_8));
    transform(in, out);
    return out.toString();
  }

  public static void transform(InputStream in, OutputStream out) {
    jsonLdToGraph(inputStreamToGraph(in), out);
  }

  private static Collection<Statement> inputStreamToGraph(InputStream in) {
    try {
      final RDFParser rdfParser = Rio.createParser(RDFFormat.JSONLD);
      final StatementCollector collector = new StatementCollector();
      rdfParser.setRDFHandler(collector);
      rdfParser.parse(in, "http://schema.org"); // empty base IRI
      return collector.getStatements();
    } catch (Exception e) {
      throw new RuntimeException(e); // TODO Switch to a custom exception
    }
  }

  private static void jsonLdToGraph(Collection<Statement> rdfGraph, OutputStream out) {
    RDFWriter writer = new RDFXMLWriter(out);
    try {
      writer.startRDF();
      for (Statement st : rdfGraph) {
        writer.handleStatement(st);
      }
      writer.endRDF();
    } catch (RDFHandlerException e) {
      throw new RuntimeException(e); // TODO Switch to a custom exception
    }
  }
}
