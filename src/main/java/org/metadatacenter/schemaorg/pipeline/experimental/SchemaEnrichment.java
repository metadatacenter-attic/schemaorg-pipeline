package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.common.collect.Lists;

public class SchemaEnrichment {

  private static final String JSONLD_ID = "@id";

  private static final String SCHEMA_NAME = "schema:name";
  private static final String SCHEMA_NAME_SHORT = "name";
  private static final String SCHEMA_CODE_VALUE = "schema:codeValue";
  private static final String SCHEMA_CODE_VALUE_SHORT = "codeValue";
  private static final String SCHEMA_IDENTIFIER = "schema:identifier";
  private static final String SCHEMA_IDENTIFIER_SHORT = "identifier";
  private static final String SCHEMA_ADDITIONAL_TYPE = "schema:additionalType";
  private static final String SCHEMA_ADDITIONAL_TYPE_SHORT = "additionalType";

  public static String fillOutIdFromObjectName(String jsonString, TermLookup lookup) {
    final JSONObject jsonObject = new JSONObject(jsonString);
    scanObjectName(jsonObject, lookup);
    return jsonObject.toString();
  }

  private static void scanObjectName(JSONObject jsonObject, TermLookup lookup) {
    doFillOutIdFromObjectName(jsonObject, lookup);
    for (String key : jsonObject.keySet()) {
      Object obj = jsonObject.get(key);
      if (obj instanceof JSONObject) {
        scanObjectName((JSONObject) obj, lookup);
      } else if (obj instanceof JSONArray) {
        JSONArray jsonArray = (JSONArray) obj;
        for (Object arrayItem : jsonArray) {
          if (arrayItem instanceof JSONObject) {
            scanObjectName((JSONObject) arrayItem, lookup);
          }
        }
      }
    }
  }

  private static void doFillOutIdFromObjectName(JSONObject jsonObject, TermLookup lookup) {
    if (!hasId(jsonObject) && hasName(jsonObject)) {
      Object obj = getName(jsonObject);
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

  public static String fillOutIdFromObjectCodeValue(String jsonString, TermLookup lookup) {
    final JSONObject jsonObject = new JSONObject(jsonString);
    scanObjectCodeValue(jsonObject, lookup);
    return jsonObject.toString();
  }

  private static void scanObjectCodeValue(JSONObject jsonObject, TermLookup lookup) {
    doFillOutIdFromObjectCodeValue(jsonObject, lookup);
    for (String key : jsonObject.keySet()) {
      Object obj = jsonObject.get(key);
      if (obj instanceof JSONObject) {
        scanObjectCodeValue((JSONObject) obj, lookup);
      } else if (obj instanceof JSONArray) {
        JSONArray jsonArray = (JSONArray) obj;
        for (Object arrayItem : jsonArray) {
          if (arrayItem instanceof JSONObject) {
            scanObjectCodeValue((JSONObject) arrayItem, lookup);
          }
        }
      }
    }
  }

  private static void doFillOutIdFromObjectCodeValue(JSONObject jsonObject, TermLookup lookup) {
    if (!hasId(jsonObject) && hasCodeValue(jsonObject)) {
      Object obj = getCodeValue(jsonObject);
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

  public static String fillOutIdFromObjectIdentifier(String jsonString, IdExpander resolver) {
    final JSONObject jsonObject = new JSONObject(jsonString);
    doFillOut(jsonObject, resolver);
    return jsonObject.toString();
  }

  private static void doFillOut(JSONObject jsonObject, IdExpander resolver) {
    fillOutId(jsonObject, resolver);
    for (String key : jsonObject.keySet()) {
      Object obj = jsonObject.get(key);
      if (obj instanceof JSONObject) {
        doFillOut((JSONObject) obj, resolver);
      } else if (obj instanceof JSONArray) {
        JSONArray jsonArray = (JSONArray) obj;
        for (Object arrayItem : jsonArray) {
          if (arrayItem instanceof JSONObject) {
            doFillOut((JSONObject) arrayItem, resolver);
          }
        }
      }
    }
  }

  private static void fillOutId(JSONObject jsonObject, IdExpander resolver) {
    if (!hasId(jsonObject) && hasSchemaIdentifier(jsonObject) && hasSchemaAdditionalType(jsonObject)) {
      Object identifier = getSchemaIdentifier(jsonObject);
      List<String> candidateNamespaces = getSchemaAdditionalType(jsonObject);
      if (identifier instanceof String || identifier instanceof Integer) {
        String shortId = identifier.toString();
        String id = resolveShortId(shortId, candidateNamespaces, resolver);
        if (id != null) {
          jsonObject.put(JSONLD_ID, id);
        }
      } else if (identifier instanceof JSONArray) {
        for (Object candidateIdentifier : (JSONArray) identifier) {
          if (candidateIdentifier instanceof String || candidateIdentifier instanceof Integer) {
            String shortId = candidateIdentifier.toString();
            String id = resolveShortId(shortId, candidateNamespaces, resolver);
            if (id != null) {
              jsonObject.put(JSONLD_ID, id);
              break; // found the best resolved short identifiers
            }
          }
        }
      }
    }
  }

  @Nullable
  private static String resolveShortId(String shortId, List<String> candidateNamespaces, IdExpander resolver) {
    for (String namespace : candidateNamespaces) {
      Optional<String> resolvedId = resolver.expand(shortId, namespace);
      if (resolvedId.isPresent()) {
        return resolvedId.get();
      }
    }
    return null;
  }

  private static boolean hasId(JSONObject jsonObject) {
    return jsonObject.has(JSONLD_ID);
  }

  private static boolean hasName(JSONObject jsonObject) {
    return jsonObject.has(SCHEMA_NAME) || jsonObject.has(SCHEMA_NAME_SHORT);
  }

  private static boolean hasCodeValue(JSONObject jsonObject) {
    return jsonObject.has(SCHEMA_CODE_VALUE) || jsonObject.has(SCHEMA_CODE_VALUE_SHORT);
  }

  private static boolean hasSchemaIdentifier(JSONObject jsonObject) {
    return jsonObject.has(SCHEMA_IDENTIFIER) || jsonObject.has(SCHEMA_IDENTIFIER_SHORT);
  }

  private static boolean hasSchemaAdditionalType(JSONObject jsonObject) {
    return jsonObject.has(SCHEMA_ADDITIONAL_TYPE) || jsonObject.has(SCHEMA_ADDITIONAL_TYPE_SHORT);
  }

  @Nullable
  private static Object getName(JSONObject jsonObject) {
    Object obj = null;
    if (jsonObject.has(SCHEMA_NAME)) {
      obj = jsonObject.get(SCHEMA_NAME);
    } else if (jsonObject.has(SCHEMA_NAME_SHORT)) {
      obj = jsonObject.get(SCHEMA_NAME_SHORT);
    }
    return obj;
  }

  @Nullable
  private static Object getCodeValue(JSONObject jsonObject) {
    Object obj = null;
    if (jsonObject.has(SCHEMA_CODE_VALUE)) {
      obj = jsonObject.get(SCHEMA_CODE_VALUE);
    } else if (jsonObject.has(SCHEMA_CODE_VALUE_SHORT)) {
      obj = jsonObject.get(SCHEMA_CODE_VALUE_SHORT);
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

  private static List<String> getSchemaAdditionalType(JSONObject jsonObject) {
    List<String> types = Lists.newArrayList();
    if (jsonObject.has(SCHEMA_ADDITIONAL_TYPE)) {
      Object obj = jsonObject.get(SCHEMA_ADDITIONAL_TYPE);
      collectTypes(obj, types);
    } else if (jsonObject.has(SCHEMA_ADDITIONAL_TYPE_SHORT)) {
      Object obj = jsonObject.get(SCHEMA_ADDITIONAL_TYPE_SHORT);
      collectTypes(obj, types);
    }
    return types;
  }

  private static void collectTypes(Object object, List<String> types) {
    if (object instanceof String) {
      types.add((String) object);
    } else if (object instanceof JSONArray) {
      for (Object arrayItem : (JSONArray) object) {
        collectTypes(arrayItem, types);
      }
    }
  }
}
