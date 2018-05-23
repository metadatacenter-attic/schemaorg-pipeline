package org.metadatacenter.schemaorg.pipeline.experimental;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DBpediaLookup implements TermLookup {

  private static final String SERVICE_ENDPOINT = "http://lookup.dbpedia.org/api/search/KeywordSearch?QueryString=";

  @Override
  public Collection<Map<String, String>> find(String name) {
    HttpURLConnection conn = null;
    try {
      String get = SERVICE_ENDPOINT + URLEncoder.encode(name, "UTF-8");
      URL url = new URL(get);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty ("Accept", "application/json");
      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed: HTTP error code " + conn.getResponseCode());
      }
      String response = ResponseUtils.getString(conn.getInputStream());
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

  private static List<Map<String, String>> findId(String text) {
    List<Map<String, String>> toReturn = Lists.newArrayList();
    JSONObject responseObject = new JSONObject(text);
    JSONArray jsonArray = responseObject.getJSONArray("results");
    for (int i = 0; i < jsonArray.length(); i++) {
      Map<String, String> map = Maps.newHashMap();
      map.put(TermLookup.CONCEPT_IRI, jsonArray.getJSONObject(i).getString("uri"));
      toReturn.add(map);
    }
    return toReturn;
  }
}
