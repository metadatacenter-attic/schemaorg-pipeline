package org.metadatacenter.schemaorg.pipeline.mapping.translator;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SparqlConstructLayout {

  private final static String SPARQL_PREFIX = "PREFIX";
  private final static String SPARQL_CONSTRUCT = "CONSTRUCT";
  private final static String SPARQL_WHERE = "WHERE";
  private final static String SPARQL_OPTIONAL = "OPTIONAL";
  private final static String SPARQL_FILTER = "FILTER";
  
  private final StringBuilder stringBuilder;
  
  private List<String> prefixes = Lists.newArrayList();
  private List<String> tripleTemplates = Lists.newArrayList();
  private Map<String, List<String>> triplePatterns = Maps.newLinkedHashMap();
  private List<String> filters = Lists.newArrayList();

  public SparqlConstructLayout() {
    this(new StringBuilder());
  }

  public SparqlConstructLayout(@Nonnull StringBuilder stringBuilder) {
    this.stringBuilder = checkNotNull(stringBuilder);
  }

  public void addPrefixes(List<String> prefixes) {
    this.prefixes = prefixes;
  }

  public void addTripleTemplate(String tripleTemplate) {
    tripleTemplates.add(tripleTemplate);
  }
  
  public void addTriplePattern(String triplePattern, String patternGroup) {
    List<String> triplePatternList = triplePatterns.get(patternGroup);
    if (triplePatternList == null) {
      triplePatternList = Lists.newArrayList();
      triplePatterns.put(patternGroup, triplePatternList);
    }
    triplePatternList.add(triplePattern);
  }

  public void addFilter(String filter) {
    filters.add(filter);
  }

  public String toString() {
    if (!prefixes.isEmpty()) {
      for (String prefix : prefixes) {
        append(SPARQL_PREFIX).append(" ").append(prefix);
        newline();
      }
      newline();
    }
    openBracket(SPARQL_CONSTRUCT, "{");
    newline();
    for (String tripleTemplate : tripleTemplates) {
      indent(3).append(tripleTemplate);
      newline();
    }
    closeBracket("}");
    newline();
    openBracket(SPARQL_WHERE, "{");
    newline();
    for (String patternGroup : triplePatterns.keySet()) {
      indent(3);
      List<String> triplePatternList = triplePatterns.get(patternGroup);
      if (triplePatternList.size() == 1) {
        openBracket(SPARQL_OPTIONAL, "{").append(triplePatternList.get(0));
        closeBracket("}");
        newline();
      } else {
        openBracket(SPARQL_OPTIONAL, "{");
        newline();
        for (String triplePattern : triplePatternList) {
          indent(6).append(triplePattern);
          newline();
        }
        indent(3);
        closeBracket("}");
        newline();
      }
    }
    if (!filters.isEmpty()) {
      for (String filter : filters) {
        indent(3);
        openBracket(SPARQL_FILTER, "(");
        indent(0).append(filter);
        closeBracket(")");
        newline();
      }
    }
    closeBracket("}");
    return stringBuilder.toString();
  }

  private StringBuilder append(String text) {
    stringBuilder.append(text);
    return stringBuilder;
  }

  private StringBuilder openBracket(String pretext, String bracket) {
    if (!Strings.isNullOrEmpty(pretext)) {
      stringBuilder.append(pretext).append(" ");
    }
    stringBuilder.append(bracket).append(" ");
    return stringBuilder;
  }

  private void closeBracket(String bracket) {
    stringBuilder.append(" ").append(bracket);
  }

  private StringBuilder indent(int size) {
    stringBuilder.append(Strings.repeat(" ", size));
    return stringBuilder;
  }

  private void newline() {
    stringBuilder.append("\n");
  }

}
