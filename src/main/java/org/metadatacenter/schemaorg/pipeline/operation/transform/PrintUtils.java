package org.metadatacenter.schemaorg.pipeline.operation.transform;

import org.json.JSONObject;

public class PrintUtils {

  public static String toPrettyPrint(String jsonLdString) {
    JSONObject jsonObject = new JSONObject(jsonLdString);
    return jsonObject.toString(3);
  }
}
