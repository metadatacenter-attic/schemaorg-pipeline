package org.metadatacenter.sandbox;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.io.Files;
import net.thisptr.jackson.jq.JsonQuery;
import net.thisptr.jackson.jq.exception.JsonQueryException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class JqUtils {

  public static String transform(String templateResource, String dataResource) throws Exception {
    return transform(getFile(templateResource), getFile(dataResource));
  }

  public static String transform(File templateFile, File sourceData) throws Exception {
    ObjectMapper MAPPER = new ObjectMapper();
    MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

    String template = Files.toString(templateFile, Charset.defaultCharset());
    JsonQuery jq = JsonQuery.compile(template);

    final JsonParser parser = MAPPER.getFactory().createParser(sourceData);

    StringBuffer output = new StringBuffer();
    while (!parser.isClosed()) {
      final JsonNode tree = parser.readValueAsTree();
      if (tree == null) {
        continue;
      }
      boolean needNewline = false;
      for (final JsonNode out : jq.apply(tree)) {
        if (needNewline) {
          output.append("\n");
        }
        if (out.isTextual()) {
          output.append(out.asText());
        } else {
          DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
          pp.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
          output.append(MAPPER.writer(pp).writeValueAsString(MAPPER.treeToValue(out, Object.class)));
        }
        needNewline = true;
      }
    }
    return output.toString();
  }

  private static File getFile(String pathname) {
    URL resourceUrl = JqUtils.class.getClassLoader().getResource(pathname);
    try {
      return new File(resourceUrl.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
