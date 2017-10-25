package org.metadatacenter.schemaorg.pipeline.transform.datasource;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.Nonnull;
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

  public static final SparqlEndpointClient BIO2RDF = new SparqlEndpointClient("http://bio2rdf.org/sparql");

  private final String endpointUrl;
  private final Repository repository;

  public SparqlEndpointClient(@Nonnull String endpointUrl) {
    this.endpointUrl = checkNotNull(endpointUrl);
    this.repository = new SPARQLRepository(endpointUrl);
    repository.initialize();
  }

  public String getEndpointUrl() {
    return endpointUrl;
  }

  public String evaluate(String constructSparqlQuery) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    evaluate(constructSparqlQuery, RDFFormat.TURTLE, out);
    return out.toString();
  }

  public String evaluate(String constructSparqlQuery, String rdfFormat) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    evaluate(constructSparqlQuery, rdfFormat, out);
    return out.toString();
  }

  public void evaluate(String constructSparqlQuery, OutputStream out) {
    evaluate(constructSparqlQuery, RDFFormat.TURTLE, out);
  }

  public void evaluate(String constructSparqlQuery, String rdfFormat, OutputStream out) {
    RDFFormat format = RdfFormatFinder.find(rdfFormat, RDFFormat.TURTLE);
    evaluate(constructSparqlQuery, format, out);
  }

  private void evaluate(String queryString, RDFFormat rdfFormat, OutputStream out) {
    try (RepositoryConnection conn = repository.getConnection()) {
      GraphQuery query = conn.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
      List<Statement> statements = Lists.newArrayList();
      try (GraphQueryResult result = query.evaluate()) {
        while (result.hasNext()) {
          Statement stmt = result.next();
          statements.add(stmt);
        }
      }
      if (!statements.isEmpty()) {
        Rio.write(statements, out, rdfFormat);
      }
    }
  }
}
