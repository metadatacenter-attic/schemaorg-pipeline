package org.metadatacenter.schemaorg.pipeline.experimental;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class ResponseUtils {

  public static JSONObject readJsonResponse(InputStream in) {
    return new JSONObject(getString(in));
  }

  public static Document readXmlResponse(InputStream in) {
    try {
      return DocumentBuilderFactory.newInstance()
          .newDocumentBuilder()
          .parse(new InputSource(in));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getString(InputStream in) {
    try {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = in.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      return result.toString("UTF-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
