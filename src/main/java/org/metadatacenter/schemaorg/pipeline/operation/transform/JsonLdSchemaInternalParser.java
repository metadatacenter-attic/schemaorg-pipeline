package org.metadatacenter.schemaorg.pipeline.operation.transform;

import java.util.List;
import java.util.Set;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import com.github.jsonldjava.core.JsonLdError;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.core.RDFParser;
import com.google.common.collect.Lists;

class JsonLdSchemaInternalParser implements RDFParser {

  private static final List<String> PREDICATES_WITH_LITERAL = Lists.newArrayList();
  static {
    PREDICATES_WITH_LITERAL.add("http://schema.org/sameAs");
  }

  public void setPrefix(RDFDataset result, String fullUri, String prefix) {
    result.setNamespace(fullUri, prefix);
  }

  public void handleStatement(RDFDataset result, Statement nextStatement) {
    final String subject = getResourceValue(nextStatement.getSubject());
    final String predicate = getResourceValue(nextStatement.getPredicate());
    final Value object = nextStatement.getObject();

    if (object instanceof Literal) {
      result.addTriple(subject, predicate, object.stringValue(), XMLSchema.STRING.stringValue(), null);
    } else {
      if (PREDICATES_WITH_LITERAL.contains(predicate)) {
        result.addTriple(subject, predicate, object.stringValue(), XMLSchema.STRING.stringValue(), null);
      } else {
        result.addTriple(subject, predicate, getResourceValue((Resource) object));
      }
    }
  }

  private String getResourceValue(Resource resource) {
    if (resource == null) {
      return null;
    } else if (resource instanceof IRI) {
      return resource.stringValue();
    } else if (resource instanceof BNode) {
      return "_:" + resource.stringValue();
    }
    throw new IllegalStateException(
        "Did not recognise resource type: " + resource.getClass().getName());
  }

  @Override
  public RDFDataset parse(Object input) throws JsonLdError {
    final RDFDataset result = new RDFDataset();
    if (input instanceof Statement) {
      handleStatement(result, (Statement) input);
    } else if (input instanceof Model) {
      final Set<Namespace> namespaces = ((Model) input).getNamespaces();
      for (final Namespace nextNs : namespaces) {
        result.setNamespace(nextNs.getName(), nextNs.getPrefix());
      }
    }
    for (final Statement nextStatement : (Model) input) {
      handleStatement(result, nextStatement);
    }
    return result;
  }
}
