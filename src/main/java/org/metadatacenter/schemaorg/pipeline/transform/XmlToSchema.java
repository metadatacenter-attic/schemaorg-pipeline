package org.metadatacenter.schemaorg.pipeline.transform;

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
      schemaInstance = renameJsonKey(schemaInstance, "_context", "@context");
      schemaInstance = renameJsonKey(schemaInstance, "_type", "@type");
      try (OutputStreamWriter osw = new OutputStreamWriter(out, Charsets.UTF_8)) {
        osw.write(schemaInstance.toString());
      }
    } catch (Exception e) {
      // TODO Throw a custom exception
    }
  }

  public static void transform(InputStream in, OutputStream out) throws IOException {
    String xmlDocument = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
    transform(xmlDocument, out);
  }

  private static JSONObject renameJsonKey(JSONObject jsonObject, String oldName, String newName) {
    JSONObject newJsonObject = new JSONObject();
    for (String key : jsonObject.keySet()) {
      Object node = jsonObject.get(key);
      if (node instanceof JSONObject) {
        node = renameJsonKey((JSONObject) node, oldName, newName);
      } else if (node instanceof JSONArray) {
        node = renameJsonKey((JSONArray) node, oldName, newName);
      }
      newJsonObject.put(rename(key, oldName, newName), node);
    }
    return newJsonObject;
  }

  private static JSONArray renameJsonKey(JSONArray jsonArray, String oldName, String newName) {
    JSONArray newJsonArray = new JSONArray();
    for (int i = 0; i < jsonArray.length(); i++) {
      Object arrayNode = jsonArray.get(i);
      if (arrayNode instanceof JSONObject) {
        arrayNode = renameJsonKey((JSONObject) arrayNode, oldName, newName);
      } else if (arrayNode instanceof JSONArray) {
        arrayNode = renameJsonKey((JSONArray) arrayNode, oldName, newName);
      }
      newJsonArray.put(arrayNode);
    }
    return newJsonArray;
  }

  private static String rename(String originalName, String oldName, String newName) {
    return originalName.equals(oldName) ? newName : originalName;
  }
}
