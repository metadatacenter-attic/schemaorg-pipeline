package org.metadatacenter.schemaorg.pipeline.experimental;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BioPortalRecommender implements TermLookup {

  private static final String SERVICE_ENDPOINT = "http://data.bioontology.org/recommender?";

  private final String apiKey;

  public BioPortalRecommender() {
    this.apiKey = checkNotNull(System.getenv("BIOPORTAL_APIKEY"));
  }

  @Override
  public Collection<Map<String, String>> find(String name) {
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
    JSONArray results = new JSONArray(text);
    for (int i = 0; i < results.length(); i++) {
      Map<String, String> map = Maps.newHashMap();
      String conceptIri = results.getJSONObject(i).getJSONObject("coverageResult")
          .getJSONArray("annotations")
          .getJSONObject(0)
          .getJSONObject("annotatedClass")
          .getString("@id");
      map.put(TermLookup.CONCEPT_IRI, conceptIri);
      map.put(TermLookup.CONCEPT_CODE, extractConceptCode(conceptIri));
      map.put(TermLookup.CONCEPT_LABEL, StringUtils.capitalize(
          results.getJSONObject(i).getJSONObject("coverageResult")
            .getJSONArray("annotations")
            .getJSONObject(0)
            .getString("text")));
      map.put(TermLookup.SOURCE_ONTOLOGY, results.getJSONObject(i).getJSONArray("ontologies")
          .getJSONObject(0)
          .getString("acronym"));
      toReturn.add(map);
    }
    return toReturn;
  }

  private static String extractConceptCode(String conceptIri) {
    int startPos = conceptIri.lastIndexOf("#");
    if (startPos == -1) {
      startPos = conceptIri.lastIndexOf("/");
    }
    return conceptIri.substring(startPos+1);
  }

  private static String getServiceAddress(String serviceEndpoint, String paramName)
      throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder(serviceEndpoint);
    sb.append("input=").append(URLEncoder.encode(paramName, "UTF-8"));
    sb.append("&input_type=2");
    sb.append("&display_context=false");
    sb.append("&display_links=false");
    return sb.toString();
  }
}
