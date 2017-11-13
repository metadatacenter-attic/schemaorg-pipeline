package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.metadatacenter.schemaorg.pipeline.caml.databind.node.MapNode;
import org.metadatacenter.schemaorg.pipeline.caml.databind.node.ObjectNode;
import org.metadatacenter.schemaorg.pipeline.mapping.TranslatorHandler;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class SparqlConstructTranslatorHandler extends TranslatorHandler {

  private static final String ROOT_INSTANCE_NAME = "s";
  private static final String FILTER_TEMPLATE = var(ROOT_INSTANCE_NAME) + " = <%s>";

  private String typeAssertion = "";

  private List<String> prefixes = Lists.newArrayList();

  public void addPrefix(@Nonnull String prefixLabel, @Nonnull String prefixNamespace) {
    checkNotNull(prefixLabel);
    checkNotNull(prefixNamespace);
    prefixes.add(String.format("%s: <%s>", prefixLabel, prefixNamespace));
  }

  public void setInstanceType(@Nonnull String instanceType) {
    checkNotNull(instanceType);
    typeAssertion = String.format("%s a %s", var(ROOT_INSTANCE_NAME), instanceType);
  }

  @Override
  public void translate(ObjectNode objectNode, OutputStream out) {
    SparqlConstructLayout sparqlLayout = initSparqlLayout();
    visit(objectNode, ROOT_INSTANCE_NAME, sparqlLayout, new AtomicInteger());
    try (PrintWriter printer = new PrintWriter(out)) {
      printer.println(sparqlLayout.toString());
    }
  }

  private SparqlConstructLayout initSparqlLayout() {
    SparqlConstructLayout layout = new SparqlConstructLayout();
    layout.setTypeAssertion(typeAssertion);
    layout.addPrefixes(prefixes);
    layout.addFilter(FILTER_TEMPLATE);
    return layout;
  }

  private void visit(MapNode mapNode, String subjectVar, SparqlConstructLayout layout,
      final AtomicInteger counter) {
    for (Iterator<String> iter = mapNode.attributeNames(); iter.hasNext();) {
      String attrName = iter.next();
      MapNode node = mapNode.get(attrName);
      String objectVar = mergeNames(subjectVar, attrName);
      if (node.isObjectNode()) {
        String dataPath = node.getValue();
        constructTripleTemplate(
            subject(subjectVar),
            predicate(attrName),
            object(objectVar),
            layout);
        constructTriplePattern(
            subject(subjectVar),
            predicateObject(dataPath, object(objectVar)),
            patternGroup(objectVar, counter),
            layout);
        visit(node, objectVar, layout, counter);
      } else if (node.isPathNode()) {
        String dataPath = node.getValue();
        constructTripleTemplate(
            subject(subjectVar),
            predicate(attrName),
            object(objectVar),
            layout);
        constructTriplePattern(
            subject(subjectVar),
            predicateObject(dataPath, object(objectVar)),
            patternGroup(subjectVar, counter),
            layout);
      } else if (node.isConstantNode()) {
        String constantValue = node.getValue();
        constructTripleTemplate(
            subject(subjectVar),
            predicate(attrName),
            literal(attrName, constantValue),
            layout);
      } else if (node.isArrayNode()) {
        int arrIndex = 0;
        for (Iterator<MapNode> arrIter = node.elements(); arrIter.hasNext(); arrIndex++) {
          MapNode item = arrIter.next();
          objectVar = mergeNames(subjectVar, attrName + arrIndex);
          if (item.isObjectNode()) {
            String dataPath = item.getValue();
            constructTripleTemplate(
                subject(subjectVar),
                predicate(attrName),
                object(objectVar),
                layout);
            constructTriplePattern(
                subject(subjectVar),
                predicateObject(dataPath, object(objectVar)),
                patternGroup(objectVar, counter),
                layout);
            visit(item, objectVar, layout, counter);
          } else if (item.isPathNode()) {
            String dataPath = item.getValue();
            constructTriplePattern(
                subject(subjectVar),
                predicateObject(dataPath,
                    object(objectVar)),
                patternGroup(subjectVar, counter),
                layout);
            constructTripleTemplate(
                subject(subjectVar),
                predicate(attrName),
                object(objectVar),
                layout);
          } else if (item.isConstantNode()) {
            String constantValue = item.getValue();
            constructTripleTemplate(
                subject(subjectVar),
                predicate(attrName),
                literal(attrName, constantValue),
                layout);
          }
        }
      }
    }
  }

  private static void constructTripleTemplate(String subject, String predicate, String object,
      SparqlConstructLayout layout) {
    String tripleTemplate = String.format("%s %s %s.", subject, predicate, object);
    layout.addTripleTemplate(tripleTemplate);
  }

  private static void constructTriplePattern(String subject, String predicateObject, String patternGroup,
      SparqlConstructLayout layout) {
    String triplePattern = String.format("%s %s.", subject, predicateObject);
    layout.addTriplePattern(triplePattern, patternGroup);
  }

  private static String predicateObject(String dataPath, String objectVar) {
    String predicateObjectString = "";
    String closingBrackets = "";
    boolean needBrackets = false;
    for (String pathElement : dataPath.split("/")) {
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

  private static String subject(String subjectVar) {
    return var(subjectVar);
  }

  private static String predicate(String attrName) {
    if (attrName.equals(ObjectNode.OBJECT_TYPE_KEYWORD)) {
      return typeAssertionProperty();
    } else {
      return schema(attrName);
    }
  }

  private static String object(String objectVar) {
    return var(objectVar);
  }

  private static String literal(String propertyName, String constantValue) {
    if (propertyName.equals(ObjectNode.OBJECT_TYPE_KEYWORD)) {
      return schema(constantValue);
    } else {
      return "'" + constantValue + "'";
    }
  }

  private static String var(@Nullable String varname) {
    return "?" + varname;
  }

  private static String patternGroup(String subjectVar, AtomicInteger counter) {
    String patternGroup = subjectVar;
    if (ROOT_INSTANCE_NAME.equals(subjectVar)) {
      patternGroup = ROOT_INSTANCE_NAME + counter.getAndIncrement();
    }
    return patternGroup;
  }

  private static String mergeNames(@Nullable String str1, @Nonnull String str2) {
    if (ROOT_INSTANCE_NAME.equals(str1)) {
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
