package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.ConstantNode;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.ObjectNode;
import org.metadatacenter.schemaorg.pipeline.alma.databind.node.PathNode;
import org.metadatacenter.schemaorg.pipeline.mapping.TranslatorHandler;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class SparqlConstructTranslatorHandler extends TranslatorHandler {

  private static final String ROOT_INSTANCE_NAME = "s";

  private List<String> prefixes = Lists.newArrayList();

  public void addPrefix(@Nonnull String prefixLabel, @Nonnull String prefixNamespace) {
    checkNotNull(prefixLabel);
    checkNotNull(prefixNamespace);
    prefixes.add(String.format("%s: <%s>", prefixLabel, prefixNamespace));
  }

  @Override
  public void translate(ObjectNode objectNode, OutputStream out) {
    final SparqlConstructLayout sparqlLayout = new SparqlConstructLayout();
    sparqlLayout.addPrefixes(prefixes);
    init(objectNode, sparqlLayout);
    String filterTemplate = rootVar() + " = <%s>";
    sparqlLayout.addFilter(filterTemplate);
    try (PrintWriter printer = new PrintWriter(out)) {
      printer.println(sparqlLayout.toString());
    }
  }

  private void init(ObjectNode objectNode, SparqlConstructLayout sparqlLayout) {
    AtomicInteger counter = new AtomicInteger();
    visit(objectNode, "", sparqlLayout, counter);
  }

  private void visit(MapNode mapNode, String subjectInstanceName, SparqlConstructLayout sparqlLayout,
      final AtomicInteger counter) {
    for (Iterator<String> iter = mapNode.attributeNames(); iter.hasNext();) {
      String attrName = iter.next();
      MapNode node = mapNode.get(attrName);
      String mapValue = node.asText();
      String objectInstanceName = mergeNames(subjectInstanceName, attrName);
      if (node instanceof ObjectNode) {
        String newSubjectInstance = mergeNames(subjectInstanceName, attrName);
        constructTripleTemplate(
            subject(subjectInstanceName),
            predicate(attrName),
            object(objectInstanceName),
            sparqlLayout);
        constructTriplePattern(
            subject(subjectInstanceName),
            predicateObject(mapValue, object(objectInstanceName)),
            sparqlLayout,
            newSubjectInstance,
            counter);
        visit(node, newSubjectInstance, sparqlLayout, counter);
      } else if (node instanceof PathNode) {
        constructTripleTemplate(
            subject(subjectInstanceName),
            predicate(attrName),
            object(objectInstanceName),
            sparqlLayout);
        constructTriplePattern(
            subject(subjectInstanceName),
            predicateObject(mapValue, object(objectInstanceName)),
            sparqlLayout,
            subjectInstanceName,
            counter);
      } else if (node instanceof ConstantNode) {
        constructTripleTemplate(
            subject(subjectInstanceName),
            predicate(attrName),
            literal(attrName, mapValue),
            sparqlLayout);
      }
    }
  }

  private static void constructTripleTemplate(String subject, String predicate, String object,
      SparqlConstructLayout sparqlLayout) {
    String tripleTemplate = String.format("%s %s %s.", subject, predicate, object);
    sparqlLayout.addTripleTemplate(tripleTemplate);
  }

  private static void constructTriplePattern(String subject, String predicateObject,
      SparqlConstructLayout sparqlLayout, String patternGroup, final AtomicInteger counter) {
    String triplePattern = String.format("%s %s.", subject, predicateObject);
    if (Strings.isNullOrEmpty(patternGroup)) {
      patternGroup = ROOT_INSTANCE_NAME + counter.getAndIncrement();
    }
    sparqlLayout.addTriplePattern(triplePattern, patternGroup);
  }

  private static String predicateObject(String path, String objectVar) {
    String predicateObjectString = "";
    String closingBrackets = "";
    boolean needBrackets = false;
    for (String pathElement : path.split("/")) {
      if (!Strings.isNullOrEmpty(pathElement)) {
        if (needBrackets) {
          predicateObjectString += " [";
          closingBrackets += "]";
        }
        predicateObjectString += pathElement;
        needBrackets = true;
      }
    }
    predicateObjectString += " " + objectVar + closingBrackets;
    return predicateObjectString;
  }

  private static String subject(String subjecInstancetName) {
    return var(subjecInstancetName);
  }

  private static String predicate(String propertyName) {
    if (propertyName.equals(ObjectNode.OBJECT_TYPE_KEYWORD)) {
      return typeAssertionProperty();
    } else {
      return schema(propertyName);
    }
  }

  private static String object(String objectInstanceName) {
    return var(objectInstanceName);
  }

  private static String literal(String propertyName, String constantValue) {
    if (propertyName.equals(ObjectNode.OBJECT_TYPE_KEYWORD)) {
      return schema(constantValue);
    } else {
      return "'" + constantValue + "'";
    }
  }

  private static String rootVar() {
    return "?" + ROOT_INSTANCE_NAME;
  }

  private static String var(@Nullable String varname) {
    if (Strings.isNullOrEmpty(varname)) {
      return rootVar();
    } else {
      return "?" + varname;
    }
  }

  private static String mergeNames(@Nullable String str1, @Nonnull String str2) {
    if (Strings.isNullOrEmpty(str1)) {
      return str2.substring(0, 1).toLowerCase() + str2.substring(1);
    } else {
      return str1.substring(0, 1).toLowerCase() + str1.substring(1)
          + str2.substring(0, 1).toUpperCase() + str2.substring(1);
    }
  }

  private static String schema(String name) {
    return "schema:" + name;
  }

  private static String typeAssertionProperty() {
    return "a"; // A syntactic sugar for rdf:type
  }
}
