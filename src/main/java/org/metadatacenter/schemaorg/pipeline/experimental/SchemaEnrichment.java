package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;
import javax.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

public class SchemaEnrichment {

  private static final String JSONLD_ID = "@id";

  private static final String SCHEMA_NAME = "schema:name";
  private static final String SCHEMA_NAME_SHORT = "name";
  private static final String SCHEMA_IDENTIFIER = "schema:identifier";
  private static final String SCHEMA_IDENTIFIER_SHORT = "identifier";

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
    if (!hasId(jsonObject) && hasSchemaName(jsonObject)) {
      Object obj = getSchemaName(jsonObject);
      if (obj instanceof String) {
        String name = (String) obj;
        Optional<String> id = lookup.find(name);
        if (id.isPresent()) {
          jsonObject.put(JSONLD_ID, id.get());
        }
      } else if (obj instanceof JSONArray) {
        JSONArray nameArray = (JSONArray) obj;
        for (Object nameObject : nameArray) {
          if (nameObject instanceof String) {
            String name = (String) nameObject;
            Optional<String> id = lookup.find(name);
            if (id.isPresent()) {
              jsonObject.put(JSONLD_ID, id.get());
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
    if (!hasId(jsonObject) && hasSchemaIdentifier(jsonObject)) {
      Object obj = getSchemaIdentifier(jsonObject);
      if (obj instanceof String) {
        String codedId = (String) obj;
        Optional<String> id = resolver.resolve(codedId, namespace);
        if (id.isPresent()) {
          jsonObject.put(JSONLD_ID, id.get());
        }
      } else if (obj instanceof JSONArray) {
        JSONArray codedIdArray = (JSONArray) obj;
        for (Object codedIdObject : codedIdArray) {
          if (codedIdObject instanceof String) {
            String codedId = (String) codedIdObject;
            Optional<String> id = resolver.resolve(codedId, namespace);
            if (id.isPresent()) {
              jsonObject.put(JSONLD_ID, id.get());
              break;
            }
          }
        }
      }
    }
  }

  private static boolean hasId(JSONObject jsonObject) {
    return jsonObject.has(JSONLD_ID);
  }

  private static boolean hasSchemaName(JSONObject jsonObject) {
    return jsonObject.has(SCHEMA_NAME) || jsonObject.has(SCHEMA_NAME_SHORT);
  }

  private static boolean hasSchemaIdentifier(JSONObject jsonObject) {
    return jsonObject.has(SCHEMA_IDENTIFIER) || jsonObject.has(SCHEMA_IDENTIFIER_SHORT);
  }

  @Nullable
  private static Object getSchemaName(JSONObject jsonObject) {
    Object obj = null;
    if (jsonObject.has(SCHEMA_NAME)) {
      obj = jsonObject.get(SCHEMA_NAME);
    } else if (jsonObject.has(SCHEMA_NAME_SHORT)) {
      obj = jsonObject.get(SCHEMA_NAME_SHORT);
    }
    return obj;
  }

  @Nullable
  private static Object getSchemaIdentifier(JSONObject jsonObject) {
    Object obj = null;
    if (jsonObject.has(SCHEMA_IDENTIFIER)) {
      obj = jsonObject.get(SCHEMA_IDENTIFIER);
    } else if (jsonObject.has(SCHEMA_IDENTIFIER_SHORT)) {
      obj = jsonObject.get(SCHEMA_IDENTIFIER_SHORT);
    }
    return obj;
  }
}
