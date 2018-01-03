package org.metadatacenter.schemaorg.pipeline.operation.transform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class XmlToSchema {

  public static String transform(String xmlDocument) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    transform(xmlDocument, out);
    return out.toString();
  }

  public static String transform(InputStream in) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    transform(in, out);
    return out.toString();
  }

  public static void transform(String xmlDocument, OutputStream out) {
    try {
      JSONObject jsonObject = XML.toJSONObject(xmlDocument);
      JSONObject schemaInstance = (JSONObject) jsonObject.get("instance");
      JSONObject finalSchemaInstance = fixJsonLabel(schemaInstance);
      try (OutputStreamWriter osw = new OutputStreamWriter(out, Charsets.UTF_8)) {
        osw.write(finalSchemaInstance.toString());
      }
    } catch (Exception e) {
      // TODO Throw a custom exception
    }
  }

  public static void transform(InputStream in, OutputStream out) throws IOException {
    String xmlDocument = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
    transform(xmlDocument, out);
  }

  private static JSONObject fixJsonLabel(JSONObject jsonObject) {
    return visitObjectNode(jsonObject);
  }

  private static JSONObject visitObjectNode(JSONObject jsonObject) {
    JSONObject newJsonObject = new JSONObject();
    for (String key : jsonObject.keySet()) {
      Object node = jsonObject.get(key);
      if (node instanceof JSONObject) {
        node = visitObjectNode((JSONObject) node);
      } else if (node instanceof JSONArray) {
        node = visitArrayNode((JSONArray) node);
      }
      newJsonObject.put(rename(key), node);
    }
    return newJsonObject;
  }

  private static JSONArray visitArrayNode(JSONArray jsonArray) {
    JSONArray newJsonArray = new JSONArray();
    for (int i = 0; i < jsonArray.length(); i++) {
      Object item = jsonArray.get(i);
      if (!item.equals("")) {
        if (item instanceof JSONObject) {
          item = visitObjectNode((JSONObject) item);
        } else if (item instanceof JSONArray) {
          item = visitArrayNode((JSONArray) item);
        }
        putIfNotDuplicates(newJsonArray, item);
      }
    }
    return newJsonArray;
  }

  private static void putIfNotDuplicates(JSONArray newJsonArray, Object arrayNode) {
    if (!newJsonArray.toList().contains(arrayNode)) {
      newJsonArray.put(arrayNode);
    }
  }

  private static String rename(String label) {
    if (label.equals("_context")) {
      return "@context";
    } else if (label.equals("_type")) {
      return "@type";
    } else {
      return label;
    }
  }
}
