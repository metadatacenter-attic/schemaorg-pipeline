package org.metadatacenter.schemaorg.pipeline.mapping.translator;

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

  public void addObjectTemplate(String attribute, String path, String type, Map<String, String> valueMap) {
    templates.add(new ObjectTemplate(attribute, path, type, valueMap));
  }

  public void addPathTemplate(String attribute, String path) {
    templates.add(new PathTemplate(attribute, path));
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
          indent(6).append(String.format("<%s _context=\"http://schema.org\" _type=\"%s\">", ot.attribute, ot.type));
          newline();
          indent(9).append("<xsl:apply-templates />");
          newline();
          for (String attrName : ot.valueMap.keySet()) {
            String value = ot.valueMap.get(attrName);
            if (isArray(value)) {
              String[] valueArray = value.substring(1, value.length()-1).split(",");
              for (String itemValue : valueArray) {
                if (isConstant(itemValue)) {
                  indent(9).append(String.format("<%s>%s</%s>", attrName, itemValue, attrName));
                  newline();
                }
              }
            } else {
              if (isConstant(value)) {
                indent(9).append(String.format("<%s>%s</%s>", attrName, value, attrName));
                newline();
              }
            }
          }
          isTopLevel = false;
        } else {
          indent(3).append(String.format("<xsl:template match=\"%s\">", removeStartingSlash(ot.path)));
          newline();
          indent(6).append(String.format("<%s _type=\"%s\">", ot.attribute, ot.type));
          newline();
          for (String attrName : ot.valueMap.keySet()) {
            String value = ot.valueMap.get(attrName);
            if (isArray(value)) {
              String[] valueArray = value.substring(1, value.length()-1).split(",");
              for (String itemValue : valueArray) {
                if (isPath(itemValue)) {
                  indent(9).append(String.format("<xsl:apply-templates select=\"%s\"/>", removeStartingSlash(itemValue)));
                } else {
                  indent(9).append(String.format("<%s>%s</%s>", attrName, itemValue, attrName));
                }
                newline();
              }
            } else {
              if (isPath(value)) {
                indent(9).append(String.format("<xsl:apply-templates select=\"%s\"/>", removeStartingSlash(value)));
              } else {
                indent(9).append(String.format("<%s>%s</%s>", attrName, value, attrName));
              }
              newline();
            }
          }
        }
        indent(6).append(String.format("</%s>", ot.attribute));
        newline();
        indent(3).append("</xsl:template>");
      } else if (template.isPathTemplate()) {
        PathTemplate pt = (PathTemplate)template;
        indent(3).append(String.format("<xsl:template match=\"%s\">", removeStartingSlash(pt.path)));
        newline();
        indent(6).append(String.format("<%s><xsl:value-of select=\".\"/></%s>", pt.attribute, pt.attribute));
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

  private static String removeStartingSlash(String path) {
    return path.substring(1);
  }

  private static boolean isConstant(String value) {
    return !isPath(value) && !isArray(value);
  }

  private static boolean isPath(String value) {
    return value.startsWith("/");
  }

  private static boolean isArray(String value) {
    return value.startsWith("[") && value.endsWith("]");
  }

  abstract class XslTemplate {
    protected final String attribute;
    public XslTemplate(String attribute) {
      this.attribute = attribute;
    }
    public boolean isObjectTemplate() { return false; }
    public boolean isPathTemplate() { return false; }
  }

  class ObjectTemplate extends XslTemplate {
    protected final String path;
    protected final String type;
    protected final Map<String, String> valueMap;
    public ObjectTemplate(String attribute, String path, String type, Map<String, String> valueMap) {
      super(attribute);
      this.path = path;
      this.type = type;
      this.valueMap = valueMap;
    }
    @Override public boolean isObjectTemplate() { return true; }
  }

  class PathTemplate extends XslTemplate {
    protected final String path;
    public PathTemplate(String attribute, String path) {
      super(attribute);
      this.path = path;
    }
    @Override public boolean isPathTemplate() { return true; }
  }
}
