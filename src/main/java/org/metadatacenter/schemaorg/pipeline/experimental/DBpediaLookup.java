package org.metadatacenter.schemaorg.pipeline.experimental;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DBpediaLookup implements TermLookup {

  private static final String SERVICE_ENDPOINT = "http://lookup.dbpedia.org/api/search/KeywordSearch?QueryString=";

  @Override
  public Optional<String> find(String name) {
    return find(name, new Properties());
  }

  @Override
  public Optional<String> find(String name, Properties additionalParameters) {
    HttpURLConnection conn = null;
    try {
      String get = SERVICE_ENDPOINT + URLEncoder.encode(name, "UTF-8");
      URL url = new URL(get);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed: HTTP error code " + conn.getResponseCode());
      }
      String response = readResponse(conn.getInputStream());
      return findId(response);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
  }

  private static Optional<String> findId(String text) {
    String foundId = null;
    Document doc = XmlUtils.parseToXml(text);
    if (XmlUtils.containTagName(doc, "Result")) {
      Element firstResult = (Element) doc.getElementsByTagName("Result").item(0);
      Element uriElement = (Element) firstResult.getElementsByTagName("URI").item(0);
      if (uriElement != null) {
        foundId = XmlUtils.getCharacterDataFromElement(uriElement);
      }
    }
    return Optional.ofNullable(foundId);
  }

  private static String readResponse(InputStream in) {
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
