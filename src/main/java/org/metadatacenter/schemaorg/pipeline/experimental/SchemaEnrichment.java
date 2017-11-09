package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

public class SchemaEnrichment {

  public static String fillOutIdFromObjectName(String jsonString, NameLookup lookup) {
    final JSONObject jsonObject = new JSONObject(jsonString);
    doFillOut(jsonObject, lookup);
    return jsonObject.toString();
  }

  private static void doFillOut(JSONObject jsonObject, NameLookup lookup) {
    fillOutId(jsonObject, lookup);
    for (String key : jsonObject.keySet()) {
      Object obj = jsonObject.get(key);
      if (obj instanceof JSONObject) {
        doFillOut((JSONObject) obj, lookup);
      } else if (obj instanceof JSONArray) {
        JSONArray jsonArray = (JSONArray) obj;
        for (Object arrayItem : jsonArray) {
          if (arrayItem instanceof JSONObject) {
            doFillOut((JSONObject) arrayItem, lookup);
          }
        }
      }
    }
  }

  private static void fillOutId(JSONObject jsonObject, NameLookup lookup) {
    if (!jsonObject.has("@id") && jsonObject.has("name")) {
      Object obj = jsonObject.get("name");
      if (obj instanceof String) {
        String name = (String) obj;
        Optional<String> id = lookup.find(name);
        if (id.isPresent()) {
          jsonObject.put("@id", id.get());
        }
      } else if (obj instanceof JSONArray) {
        JSONArray nameArray = (JSONArray) obj;
        for (Object nameObject : nameArray) {
          if (nameObject instanceof String) {
            String name = (String) nameObject;
            Optional<String> id = lookup.find(name);
            if (id.isPresent()) {
              jsonObject.put("@id", id.get());
              break;
            }
          }
        }
      }
    }
  }

  public static String fillOutIdFromObjectIdentifier(String jsonString, String namespace, IdResolver resolver) {
    final JSONObject jsonObject = new JSONObject(jsonString);
    doFillOut(jsonObject, namespace, resolver);
    return jsonObject.toString();
  }

  private static void doFillOut(JSONObject jsonObject, String namespace, IdResolver resolver) {
    fillOutId(jsonObject, namespace, resolver);
    for (String key : jsonObject.keySet()) {
      Object obj = jsonObject.get(key);
      if (obj instanceof JSONObject) {
        doFillOut((JSONObject) obj, namespace, resolver);
      }
    }
  }

  private static void fillOutId(JSONObject jsonObject, String namespace, IdResolver resolver) {
    if (!jsonObject.has("@id") && jsonObject.has("identifier")) {
      Object obj = jsonObject.get("identifier");
      if (obj instanceof String) {
        String codedId = (String) obj;
        Optional<String> id = resolver.resolve(codedId, namespace);
        if (id.isPresent()) {
          jsonObject.put("@id", id.get());
        }
      } else if (obj instanceof JSONArray) {
        JSONArray codedIdArray = (JSONArray) obj;
        for (Object codedIdObject : codedIdArray) {
          if (codedIdObject instanceof String) {
            String codedId = (String) codedIdObject;
            Optional<String> id = resolver.resolve(codedId, namespace);
            if (id.isPresent()) {
              jsonObject.put("@id", id.get());
              break;
            }
          }
        }
      }
    }
  }
}
