package org.metadatacenter.schemaorg.pipeline.experimental;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.json.JSONArray;
import org.json.JSONObject;
import static com.google.common.base.Preconditions.checkNotNull;

public class BioPortalLookup implements TermLookup {

  private static final String SERVICE_ENDPOINT = "http://data.bioontology.org/search?";

  private final String apiKey;

  public BioPortalLookup(@Nonnull String apiKey) {
    this.apiKey = checkNotNull(apiKey);
  }

  @Override
  public Optional<String> find(String name) {
    HttpURLConnection conn = null;
    try {
      String serviceAddress = getServiceAddress(SERVICE_ENDPOINT, name);
      URL url = new URL(serviceAddress);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty ("Authorization", "apikey token=" + apiKey);
      conn.setRequestProperty ("Accept", "application/json");
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
    JSONObject responseObject = new JSONObject(text);
    JSONArray results = responseObject.getJSONArray("collection");
    if (results.length() > 0) {
      JSONObject firstItem = results.getJSONObject(0);
      foundId = firstItem.getString("@id");
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

  private static String getServiceAddress(String serviceEndpoint, String paramName)
      throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder(serviceEndpoint);
    sb.append("q=").append(URLEncoder.encode(paramName, "UTF-8"));
    sb.append("&exact_match=true");
    sb.append("&include=prefLabel");
    return sb.toString();
  }
}
