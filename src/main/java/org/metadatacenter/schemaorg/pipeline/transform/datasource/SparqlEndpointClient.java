package org.metadatacenter.schemaorg.pipeline.transform.datasource;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.metadatacenter.schemaorg.pipeline.transform.RdfFormatFinder;
import com.google.common.collect.Lists;

public class SparqlEndpointClient {

  public static String evaluate(String endpointUrl, String constructSparqlQuery) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    evaluate(endpointUrl, constructSparqlQuery, RDFFormat.TURTLE, out);
    return out.toString();
  }

  public static String evaluate(String endpointUrl, String constructSparqlQuery, String rdfFormat) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    evaluate(endpointUrl, constructSparqlQuery, rdfFormat, out);
    return out.toString();
  }

  public static void evaluate(String endpointUrl, String constructSparqlQuery, OutputStream out) {
    evaluate(endpointUrl, constructSparqlQuery, RDFFormat.TURTLE, out);
  }

  public static void evaluate(String endpointUrl, String constructSparqlQuery, String rdfFormat,
      OutputStream out) {
    RDFFormat format = RdfFormatFinder.find(rdfFormat, RDFFormat.TURTLE);
    evaluate(endpointUrl, constructSparqlQuery, format, out);
  }

  private static void evaluate(String endpointUrl, String sparqlString, RDFFormat rdfFormat,
      OutputStream outStream) {
    Repository repository = new SPARQLRepository(endpointUrl);
    repository.initialize();
    try (RepositoryConnection conn = repository.getConnection()) {
      GraphQuery query = conn.prepareGraphQuery(QueryLanguage.SPARQL, sparqlString);
      List<Statement> statements = Lists.newArrayList();
      try (GraphQueryResult result = query.evaluate()) {
        while (result.hasNext()) {
          Statement stmt = result.next();
          statements.add(stmt);
        }
      }
      if (!statements.isEmpty()) {
        Rio.write(statements, outStream, rdfFormat);
      }
    }
  }
}
