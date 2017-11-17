package org.metadatacenter.schemaorg.pipeline.embed;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.metadatacenter.schemaorg.pipeline.embed.HtmlAttributes.*;
import static org.metadatacenter.schemaorg.pipeline.embed.HtmlTag.*;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.annotation.Nonnull;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class SchemaToHtml {

  private static final List<String> NO_RENDER_ATTRIBUTES = Lists.newArrayList();
  static {
    NO_RENDER_ATTRIBUTES.add("@context");
    NO_RENDER_ATTRIBUTES.add("@type");
    NO_RENDER_ATTRIBUTES.add("@id");
  }

  public static String transform(@Nonnull String jsonString) {
    checkNotNull(jsonString);
    return convert(jsonString, new Properties());
  }

  public static String convert(@Nonnull String jsonString, @Nonnull Properties props) {
    checkNotNull(jsonString);
    checkNotNull(props);
    return convert(new JSONObject(jsonString), props);
  }

  public static String convert(@Nonnull JSONObject jsonObject) {
    checkNotNull(jsonObject);
    return convert(jsonObject, new Properties());
  }

  @SuppressWarnings("unchecked")
  public static String convert(@Nonnull JSONObject jsonObject, @Nonnull Properties props) {
    checkNotNull(jsonObject);
    checkNotNull(props);
    StringBuilder sb = new StringBuilder();
    sb.append(HTML.open());
    newline(sb);
    indent(sb, 3).append(HEAD.open());
    newline(sb);
    if (props.containsKey("charset")) {
      String charset = props.get("charset").toString();
      indent(sb, 6).append(META.open(charset(charset)));
    } else {
      indent(sb, 6).append(META.open(charset("UTF-8")));
    }
    if (props.containsKey("description")) {
      newline(sb);
      String description = props.get("description").toString();
      indent(sb, 6).append(META.open(name("description"), content(description)));
    }
    if (props.containsKey("keywords")) {
      newline(sb);
      String keywords = props.get("keywords").toString();
      indent(sb, 6).append(META.open(name("keywords"), content(keywords)));
    }
    if (props.containsKey("author")) {
      newline(sb);
      String author = props.get("author").toString();
      indent(sb, 6).append(META.open(name("author"), content(author)));
    }
    if (props.containsKey("css")) {
      newline(sb);
      String css = props.get("css").toString();
      indent(sb, 6).append(LINK.open(rel("stylesheet"), href(css)));
    } else {
      newline(sb);
      sb.append(cssing(6));
    }
    newline(sb);
    indent(sb, 6).append(SCRIPT.open(type("application/ld+json")));
    newline(sb);
    sb.append(jsonObject.toString(3));
    newline(sb);
    indent(sb, 3).append(SCRIPT.close());
    newline(sb);
    indent(sb, 3).append(HEAD.close());
    newline(sb);
    indent(sb, 3).append(BODY.open());
    newline(sb);
    sb.append(tabling(jsonObject, 6));
    newline(sb);
    indent(sb, 3).append(BODY.close());
    newline(sb);
    sb.append(HTML.close());
    return sb.toString();
  }

  private static String cssing(int startIndentation) {
    StringBuilder sb = new StringBuilder();
    indent(sb, startIndentation).append(STYLE.open());
    newline(sb);
    indent(sb, startIndentation).append("table, th, td { border: 1px solid black; }");
    newline(sb);
    indent(sb, startIndentation).append("table { border-collapse: collapse; width: 720px; }");
    newline(sb);
    indent(sb, startIndentation).append("th, td { text-align: left; padding: 8px; }");
    newline(sb);
    indent(sb, startIndentation).append("td.key { font-family: verdana; font-size: 12px; font-weight: bold; text-transform: capitalize; width: 16%; background-color: #f2f2f2; }");
    newline(sb);
    indent(sb, startIndentation).append("td.value { font-family: monospace; white-space: pre-wrap; white-space: -moz-pre-wrap; white-space: -pre-wrap; white-space: -o-pre-wrap; word-wrap: break-word; }");
    newline(sb);
    indent(sb, startIndentation).append("td.index { font-family: verdana; font-size: 12px; width: 16px; }");
    newline(sb);
    indent(sb, startIndentation).append("td.item { /* empty */ }");
    newline(sb);
    indent(sb, startIndentation).append(STYLE.close());
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private static String tabling(JSONObject jsonObject, int startIndentation) {
    StringBuilder sb = new StringBuilder();
    indent(sb, startIndentation).append(TABLE.open());
    jsonObject.keySet().removeAll(NO_RENDER_ATTRIBUTES);
    for (String key : jsonObject.keySet()) {
      newline(sb);
      indent(sb, (startIndentation + 3)).append(TR.open());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open(cssClass("key"))).append(trimSchemaPrefix(key)).append(TD.close());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open(cssClass("value")));
      Object node = jsonObject.get(key);
      if (node instanceof JSONObject) {
        sb.append(tabling((JSONObject) node, 0));
      } else if (node instanceof JSONArray) {
        sb.append(listing((JSONArray) node, 0));
      } else {
        String valueString = node.toString();
        if (isValidUrl(valueString)) {
          sb.append(A.open(href(valueString), target("_blank"))).append(valueString).append(A.close());
        } else {
          sb.append(valueString);
        }
      }
      indent(sb, 0).append(TD.close());
      newline(sb);
      indent(sb, (startIndentation + 3)).append(TR.close());
    }
    newline(sb);
    indent(sb, startIndentation).append(TABLE.close());
    return sb.toString();
  }

 @SuppressWarnings("unchecked")
  private static String listing(JSONArray jsonArray, int startIndentation) {
    StringBuilder sb = new StringBuilder();
    indent(sb, startIndentation).append(TABLE.open());
    for (int i = 0; i < jsonArray.length(); i++) {
      newline(sb);
      indent(sb, (startIndentation + 3)).append(TR.open());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open(cssClass("index"))).append(i + 1).append(TD.close());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open(cssClass("item")));
      newline(sb);
      Object arrayNode = jsonArray.get(i);
      if (arrayNode instanceof JSONObject) {
        sb.append(tabling((JSONObject) arrayNode, (startIndentation + 9)));
      } else if (arrayNode instanceof JSONArray) {
        sb.append(listing((JSONArray) arrayNode, (startIndentation + 9)));
      } else {
        String valueString = arrayNode.toString();
        if (isValidUrl(valueString)) {
          sb.append(A.open(href(valueString), target("_blank"))).append(valueString).append(A.close());
        } else {
          sb.append(valueString);
        }
      }
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.close());
      newline(sb);
      indent(sb, (startIndentation + 3)).append(TR.close());
    }
    newline(sb);
    indent(sb, startIndentation).append(TABLE.close());
    return sb.toString();
  }

 private static String trimSchemaPrefix(String key) {
    return key.replaceFirst("^(schema:)", "");
  }

  private static StringBuilder indent(StringBuilder sb, int size) {
    sb.append(Strings.repeat(" ", size));
    return sb;
  }

  private static StringBuilder newline(StringBuilder sb) {
    sb.append("\n");
    return sb;
  }

  public static boolean isValidUrl(String urlString) {
    try {
      URL url = new URL(urlString);
      url.toURI();
      return true;
    } catch (Exception exception) {
      return false;
    }
  }
}
