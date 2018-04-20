package org.metadatacenter.schemaorg.pipeline.operation.translate;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

class XsltLayout {

  private final StringBuilder stringBuilder;

  private List<XslTemplate> templates = Lists.newArrayList();

  public XsltLayout() {
    this(new StringBuilder());
  }

  public XsltLayout(@Nonnull StringBuilder stringBuilder) {
    this.stringBuilder = checkNotNull(stringBuilder);
  }

  public void addObjectTemplate(String name, String path, Map<String, String> attributeMap) {
    templates.add(new ObjectTemplate(name, path, attributeMap));
  }

  public void addPathTemplate(String name, String path) {
    templates.add(new PathTemplate(name, path));
  }

  public void addFunctionTemplate(String name, String functionName, List<String> arguments) {
    if ("concat".equals(functionName)) {
      String path = "";
      StringBuilder expressionBuilder = new StringBuilder("concat");
      expressionBuilder.append("(");
      boolean needComma = false;
      for (String argument : arguments) {
        if (needComma) {
          expressionBuilder.append(",");
        }
        if (isPath(argument)) {
          path = argument;
          expressionBuilder.append(".");
        } else if (isConstant(argument)) {
          expressionBuilder.append(argument);
        }
        needComma = true;
      }
      expressionBuilder.append(")");
      templates.add(new FunctionTemplate(name, path, expressionBuilder.toString()));
    }
  }

  @Override
  public String toString() {
    append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    newline();
    append("<xsl:stylesheet version=\"2.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">");
    newline();
    indent(3).append("<xsl:output method=\"xml\" indent=\"yes\" />");
    newline();
    boolean isTopLevel = true;
    for (XslTemplate template : templates) {
      if (template.isObjectTemplate()) {
        ObjectTemplate ot = (ObjectTemplate)template;
        if (isTopLevel) {
          indent(3).append("<xsl:template match=\"/*\">");
          newline();
          indent(6).append(String.format("<%s _context=\"http://schema.org\">", ot.name));
          newline();
          indent(9).append("<xsl:apply-templates />");
          newline();
          for (String attrName : ot.attributeMap.keySet()) {
            String value = ot.attributeMap.get(attrName);
            if (isArray(value)) {
              String[] valueArray = getArrayValues(value);
              for (String itemValue : valueArray) {
                if (isConstant(itemValue)) {
                  indent(9).append(String.format("<%s>%s</%s>", attrName, itemValue, attrName));
                  newline();
                }
              }
            } else if (isConstant(value)) {
              String constant = getConstantValue(value);
              indent(9).append(String.format("<%s>%s</%s>", attrName, constant, attrName));
              newline();
            }
          }
          isTopLevel = false;
        } else {
          indent(3).append(String.format("<xsl:template match=\"%s\">", ot.path));
          newline();
          indent(6).append(String.format("<%s>", ot.name));
          newline();
          for (String attrName : ot.attributeMap.keySet()) {
            String value = ot.attributeMap.get(attrName);
            if (isArray(value)) {
              String[] valueArray = getArrayValues(value);
              for (String itemValue : valueArray) {
                if (isPath(itemValue)) {
                  String path = getPathValue(itemValue);
                  if (".".equals(path)) {
                    indent(9).append(String.format("<%s><xsl:value-of select=\".\"/></%s>", attrName, attrName));
                    newline();
                  } else {
                    indent(9).append(String.format("<xsl:apply-templates select=\"%s\"/>", path));
                    newline();
                  }
                } else if (isConstant(itemValue)) {
                  indent(9).append(String.format("<%s>%s</%s>", attrName, itemValue, attrName));
                  newline();
                }
              }
            } else if (isPath(value)) {
              String path = getPathValue(value);
              if (".".equals(path)) {
                indent(9).append(String.format("<%s><xsl:value-of select=\".\"/></%s>", attrName, attrName));
                newline();
              } else {
                indent(9).append(String.format("<xsl:apply-templates select=\"%s\"/>", path));
                newline();
              }
            } else if (isConstant(value)) {
              String constant = getConstantValue(value);
              indent(9).append(String.format("<%s>%s</%s>", attrName, constant, attrName));
              newline();
            }
          }
        }
        indent(6).append(String.format("</%s>", ot.name));
        newline();
        indent(3).append("</xsl:template>");
      } else if (template.isPathTemplate()) {
        PathTemplate pt = (PathTemplate) template;
        indent(3).append(String.format("<xsl:template match=\"%s\">", pt.path));
        newline();
        indent(6).append(String.format("<%s><xsl:value-of select=\".\"/></%s>", pt.name, pt.name));
        newline();
        indent(3).append("</xsl:template>");
      } else if (template.isFunctionTemplate()) {
        FunctionTemplate ft = (FunctionTemplate) template;
        indent(3).append(String.format("<xsl:template match=\"%s\">", ft.path));
        newline();
        indent(6).append(String.format("<%s><xsl:value-of select=\"%s\"/></%s>", ft.name, ft.expression, ft.name));
        newline();
        indent(3).append("</xsl:template>");
      }
      newline();
    }
    indent(3).append("<xsl:template match=\"text()\"/>");
    newline();
    append("</xsl:stylesheet>");
    return stringBuilder.toString();
  }

  private void append(String text) {
    stringBuilder.append(text);
  }

  private StringBuilder indent(int size) {
    stringBuilder.append(Strings.repeat(" ", size));
    return stringBuilder;
  }

  private void newline() {
    stringBuilder.append("\n");
  }

  private static String getPathValue(String path) {
    return path.substring(1);
  }

  private static String getConstantValue(String value) {
    return value.substring(1, value.length()-1);
  }

  private static String[] getArrayValues(String value) {
    return value.substring(1, value.length()-1).split(",");
  }

  private static boolean isConstant(String value) {
    return value.startsWith("'") && value.endsWith("'");
  }

  private static boolean isPath(String value) {
    return value.startsWith("/");
  }

  private static boolean isArray(String value) {
    return value.startsWith("[") && value.endsWith("]");
  }

  abstract class XslTemplate {
    protected final String name;
    public XslTemplate(String name) {
      this.name = name;
    }
    public boolean isObjectTemplate() { return false; }
    public boolean isPathTemplate() { return false; }
    public boolean isFunctionTemplate() { return false; }
  }

  class ObjectTemplate extends XslTemplate {
    protected final String path;
    protected final Map<String, String> attributeMap;
    public ObjectTemplate(String name, String path, Map<String, String> attributeMap) {
      super(name);
      this.path = path;
      this.attributeMap = attributeMap;
    }
    @Override public boolean isObjectTemplate() { return true; }
  }

  class PathTemplate extends XslTemplate {
    protected final String path;
    public PathTemplate(String name, String path) {
      super(name);
      this.path = path;
    }
    @Override public boolean isPathTemplate() { return true; }
  }

  class FunctionTemplate extends XslTemplate {
    protected final String path;
    protected final String expression;
    public FunctionTemplate(String name, String path, String expression) {
      super(name);
      this.path = path;
      this.expression = expression;
    }
    @Override public boolean isFunctionTemplate() { return true; }
  }
}