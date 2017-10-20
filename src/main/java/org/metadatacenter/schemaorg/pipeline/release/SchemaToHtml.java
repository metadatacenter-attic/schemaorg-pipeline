package org.metadatacenter.schemaorg.pipeline.release;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.metadatacenter.schemaorg.pipeline.release.HtmlAttributes.charset;
import static org.metadatacenter.schemaorg.pipeline.release.HtmlAttributes.content;
import static org.metadatacenter.schemaorg.pipeline.release.HtmlAttributes.name;
import static org.metadatacenter.schemaorg.pipeline.release.HtmlAttributes.type;
import static org.metadatacenter.schemaorg.pipeline.release.HtmlTag.*;
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

  private static final List<String> PREFORMATTED_ATTRIBUTES = Lists.newArrayList();
  static {
    PREFORMATTED_ATTRIBUTES.add("description");
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

  private static String tabling(JSONObject jsonObject, int startIndentation) {
    StringBuilder sb = new StringBuilder();
    indent(sb, startIndentation).append(TABLE.open());
    jsonObject.keySet().removeAll(NO_RENDER_ATTRIBUTES);
    for (String key : jsonObject.keySet()) {
      newline(sb);
      indent(sb, (startIndentation + 3)).append(TR.open());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open()).append(key).append(TD.close());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open());
      newline(sb);
      Object node = jsonObject.get(key);
      if (node instanceof JSONObject) {
        sb.append(tabling((JSONObject) node, (startIndentation + 9)));
      } else if (node instanceof JSONArray) {
        sb.append(listing((JSONArray) node, (startIndentation + 9)));
      } else {
        sb.append(texting(node, (startIndentation + 9), PREFORMATTED_ATTRIBUTES.contains(key)));
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

  private static String listing(JSONArray jsonArray, int startIndentation) {
    StringBuilder sb = new StringBuilder();
    indent(sb, startIndentation).append(TABLE.open());
    for (int i = 0; i < jsonArray.length(); i++) {
      newline(sb);
      indent(sb, (startIndentation + 3)).append(TR.open());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open()).append(i + 1).append(TD.close());
      newline(sb);
      indent(sb, (startIndentation + 6)).append(TD.open());
      newline(sb);
      Object arrayNode = jsonArray.get(i);
      if (arrayNode instanceof JSONObject) {
        sb.append(tabling((JSONObject) arrayNode, (startIndentation + 9)));
      } else if (arrayNode instanceof JSONArray) {
        sb.append(listing((JSONArray) arrayNode, (startIndentation + 9)));
      } else {
        sb.append(texting(arrayNode, (startIndentation + 9), false));
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

  private static String texting(Object value, int startIndentation, boolean isPreformattedText) {
    StringBuilder sb = new StringBuilder();
    if (isPreformattedText) {
      indent(sb, startIndentation).append(PRE.open()).append(value.toString()).append(PRE.close());
    } else {
      indent(sb, startIndentation).append(value.toString());
    }
    return sb.toString();
  }

  private static StringBuilder indent(StringBuilder sb, int size) {
    sb.append(Strings.repeat(" ", size));
    return sb;
  }

  private static StringBuilder newline(StringBuilder sb) {
    sb.append("\n");
    return sb;
  }
}
