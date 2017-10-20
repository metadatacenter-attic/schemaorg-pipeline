package org.metadatacenter.schemaorg.pipeline.transform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Collection;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import com.google.common.base.Charsets;

public class RdfToSchema {

  public String transform(String graphString) {
    return transform(graphString, "Turtle");
  }

  public String transform(String graphString, String rdfFormat) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    InputStream in = new ByteArrayInputStream(graphString.getBytes(Charsets.UTF_8));
    RDFFormat format = RdfFormatFinder.find(rdfFormat, RDFFormat.TURTLE);
    transform(in, format, out);
    return out.toString();
  }

  public void transform(InputStream in, OutputStream out) {
    transform(in, "Turtle", out);
  }

  public void transform(InputStream in, String rdfFormat, OutputStream out) {
    RDFFormat format = RdfFormatFinder.find(rdfFormat, RDFFormat.TURTLE);
    transform(in, format, out);
  }

  private void transform(InputStream in, RDFFormat rdfFormat, OutputStream out) {
    graphToJsonLd(inputStreamToGraph(in, rdfFormat), out);
  }

  private static Collection<Statement> inputStreamToGraph(InputStream in, RDFFormat rdfFormat) {
    try {
      final RDFParser rdfParser = Rio.createParser(rdfFormat);
      final StatementCollector collector = new StatementCollector();
      rdfParser.setRDFHandler(collector);
      rdfParser.parse(in, ""); // empty base IRI
      return collector.getStatements();
    } catch (Exception e) {
      throw new RuntimeException(e); // TODO Switch to a custom exception
    }
  }

  public static String graphToJsonLd(Collection<Statement> rdfGraph, OutputStream out) {
    StringWriter outputWriter = new StringWriter();
    RDFWriter writer = new JsonLdSchemaWriter(out);
    try {
      writer.startRDF();
      for (Statement st : rdfGraph) {
        writer.handleStatement(st);
      }
      writer.endRDF();
      return outputWriter.getBuffer().toString();
    } catch (RDFHandlerException e) {
      throw new RuntimeException(e); // TODO Switch to a custom exception
    }
  }
}
