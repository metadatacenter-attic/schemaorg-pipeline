package org.metadatacenter.schemaorg.pipeline.transform;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFWriter;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;

public class JsonLdSchemaWriter extends AbstractRDFWriter {

  public static final RDFFormat JSONLD_SCHEMA = new RDFFormat("JSON-LD",
      Arrays.asList("application/ld+json"), Charsets.UTF_8, Arrays.asList("jsonld"),
      SimpleValueFactory.getInstance().createIRI("http://www.w3.org/ns/formats/JSON-LD"),
      true,  // SUPPORT_NAMESPACES
      true);  // SUPPORT_CONTEXT

  private final Model model = new LinkedHashModel();

  private final StatementCollector statementCollector = new StatementCollector(model);

  private final Writer writer;

  public JsonLdSchemaWriter(OutputStream outputStream) {
    this(new BufferedWriter(new OutputStreamWriter(outputStream, Charsets.UTF_8)));
  }

  public JsonLdSchemaWriter(Writer writer) {
    this.writer = writer;
  }

  @Override
  public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
    model.setNamespace(prefix, uri);
  }

  @Override
  public void startRDF() throws RDFHandlerException {
    statementCollector.clear();
    model.clear();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void endRDF() throws RDFHandlerException {
    final JsonLdSchemaInternalParser serialiser = new JsonLdSchemaInternalParser();
    try {
      Object output = JsonLdProcessor.fromRDF(model, getJsonLdOptions(), serialiser);
      Map<String, Object> framedObject = JsonLdProcessor.frame(output, getDefaultContext(), getProcessingOptions());
      List<Map<String, Object>> listOfGraphs = (List<Map<String, Object>>) framedObject.get("@graph");
      Map<String, Object> object = createSchemaObject(getDefaultContext(), listOfGraphs);
      JsonUtils.write(writer, object);
    } catch (final JsonLdError e) {
      throw new RDFHandlerException("Could not render JSONLD", e);
    } catch (final JsonGenerationException e) {
      throw new RDFHandlerException("Could not render JSONLD", e);
    } catch (final JsonMappingException e) {
      throw new RDFHandlerException("Could not render JSONLD", e);
    } catch (final IOException e) {
      throw new RDFHandlerException("Could not render JSONLD", e);
    }
  }

  private Map<String, Object> createSchemaObject(Map<String, Object> context, List<Map<String, Object>> graphs) {
    LinkedHashMap<String, Object> object = Maps.newLinkedHashMap();
    object.putAll(context);
    object.putAll(graphs.stream().findFirst().get());
    return object;
  }

  private static JsonLdOptions getJsonLdOptions() {
    final JsonLdOptions options = new JsonLdOptions();
    options.setCompactArrays(true);
    options.setUseNativeTypes(true);
    return options;
  }

  private static Map<String, Object> getDefaultContext() {
    final Map<String, Object> namespaces = new LinkedHashMap<String, Object>();
    namespaces.put("schema", "http://schema.org/");
    final Map<String, Object> context = new HashMap<String, Object>();
    context.put("@context", namespaces);
    return context;
  }

  private static JsonLdOptions getProcessingOptions() {
    JsonLdOptions opts = new JsonLdOptions();
    opts.setUseRdfType(false);
    opts.setUseNativeTypes(true);
    opts.setCompactArrays(true);
    return opts;
  }

  @Override
  public void handleStatement(Statement st) throws RDFHandlerException {
    statementCollector.handleStatement(st);
  }

  @Override
  public void handleComment(String comment) throws RDFHandlerException {
    // NO-OP
  }

  @Override
  public RDFFormat getRDFFormat() {
    return JSONLD_SCHEMA;
  }
}
