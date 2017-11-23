package org.metadatacenter.schemaorg.pipeline.operation.embed;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Map;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;

public class HtmlTag {

  public static final HtmlTag HTML = new HtmlTag("html");
  public static final HtmlTag TITLE = new HtmlTag("title");
  public static final HtmlTag META = new HtmlTag("meta");
  public static final HtmlTag HEAD = new HtmlTag("head");
  public static final HtmlTag LINK = new HtmlTag("link");
  public static final HtmlTag STYLE = new HtmlTag("style");
  public static final HtmlTag BODY = new HtmlTag("body");
  public static final HtmlTag SCRIPT = new HtmlTag("script");
  public static final HtmlTag DIV = new HtmlTag("div");
  public static final HtmlTag SPAN = new HtmlTag("span");
  public static final HtmlTag TABLE = new HtmlTag("table");
  public static final HtmlTag TH = new HtmlTag("th");
  public static final HtmlTag TR = new HtmlTag("tr");
  public static final HtmlTag TD = new HtmlTag("td");
  public static final HtmlTag UL = new HtmlTag("ul");
  public static final HtmlTag OL = new HtmlTag("ol");
  public static final HtmlTag LI = new HtmlTag("li");
  public static final HtmlTag H1 = new HtmlTag("h1");
  public static final HtmlTag H2 = new HtmlTag("h2");
  public static final HtmlTag H3 = new HtmlTag("h3");
  public static final HtmlTag H4 = new HtmlTag("h4");
  public static final HtmlTag PRE = new HtmlTag("pre");
  public static final HtmlTag A = new HtmlTag("a");
  public static final HtmlTag P = new HtmlTag("p");

  private final String name;

  public HtmlTag(@Nonnull String name) {
    this.name = checkNotNull(name);
  }

  public String open() {
    return openTag(name);
  }

  @SuppressWarnings("unchecked")
  public String open(Map<String, String>... attribute) {
    Map<String, String> allAttributes = Maps.newLinkedHashMap();
    for (Map<String, String> attr : attribute) {
      allAttributes.putAll(attr);
    }
    return openTag(name, allAttributes);
  }

  public String close() {
    return closeTag(name);
  }

  public String inline() {
    return inlineTag(name);
  }

  @SuppressWarnings("unchecked")
  public String inline(Map<String, String>... attribute) {
    Map<String, String> allAttributes = Maps.newLinkedHashMap();
    for (Map<String, String> attr : attribute) {
      allAttributes.putAll(attr);
    }
    return inlineTag(name, allAttributes);
  }

  public static String openTag(String elementName) {
    return String.format("<%s>", elementName);
  }

  public static String openTag(String elementName, Map<String, String> attributeMap) {
    if (attributeMap.isEmpty()) {
      return openTag(elementName);
    } else {
      return String.format("<%s %s>", elementName, printAttributes(attributeMap));
    }
  }

  public static String closeTag(String elementName) {
    return String.format("</%s>", elementName);
  }

  public static String inlineTag(String elementName) {
    return String.format("<%s>", elementName);
  }

  public static String inlineTag(String elementName, Map<String, String> attributeMap) {
    if (attributeMap.isEmpty()) {
      return inlineTag(elementName);
    } else {
      return String.format("<%s %s />", elementName, printAttributes(attributeMap));
    }
  }

  private static String printAttributes(Map<String, String> attributeMap) {
    StringBuilder sb = new StringBuilder();
    boolean needWhitespace = false;
    for (String attributeName : attributeMap.keySet()) {
      if (needWhitespace) {
        sb.append(" ");
      }
      sb.append(String.format("%s=\"%s\"", attributeName, attributeMap.get(attributeName)));
      needWhitespace = true;
    }
    return sb.toString();
  }
}
