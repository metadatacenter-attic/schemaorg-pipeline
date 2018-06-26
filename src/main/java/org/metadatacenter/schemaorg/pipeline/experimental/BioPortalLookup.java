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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BioPortalLookup implements TermLookup {

  private static final String SERVICE_ENDPOINT = "http://data.bioontology.org/search?";

  private final String apiKey;

  public BioPortalLookup() {
    this.apiKey = checkNotNull(System.getenv("BIOPORTAL_APIKEY"));
  }

  public BioPortalLookup(@Nonnull String apiKey) {
    this.apiKey = checkNotNull(apiKey);
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
    JSONObject responseObject = new JSONObject(text);
    JSONArray results = responseObject.getJSONArray("collection");
    for (int i = 0; i < results.length(); i++) {
      Map<String, String> map = Maps.newHashMap();
      String conceptIri = results.getJSONObject(i).getString("@id");
      map.put(TermLookup.CONCEPT_IRI, conceptIri);
      map.put(TermLookup.CONCEPT_CODE, getCode(results.getJSONObject(i)));
      map.put(TermLookup.SOURCE_ONTOLOGY, getSourceOntology(conceptIri));
      map.put(TermLookup.CONCEPT_LABEL, results.getJSONObject(i).getString("prefLabel"));
      toReturn.add(map);
    }
    return toReturn;
  }

  private static String getCode(JSONObject item) {
    if (item.has("notation")) {
      return item.getString("notation");
    } else {
      String conceptId = item.getString("@id");
      int startPos = conceptId.lastIndexOf("#");
      if (startPos == -1) {
        startPos = conceptId.lastIndexOf("/");
      }
      return conceptId.substring(startPos+1);
    }
  }

  @Nullable
  private static String getSourceOntology(String conceptIri) {
    Pattern purlPattern = Pattern.compile("http://purl.bioontology.org/ontology/(.+)/.+");
    Matcher m = purlPattern.matcher(conceptIri);
    if (m.find()) {
      return m.group(0);
    }
    Pattern ncitPattern = Pattern.compile("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#.+");
    m = ncitPattern.matcher(conceptIri);
    if (m.matches()) {
      return "NCIT";
    }
    Pattern efoPattern = Pattern.compile("http://www.ebi.ac.uk/efo/.+");
    m = efoPattern.matcher(conceptIri);
    if (m.matches()) {
      return "EFO";
    }
    Pattern radlexPattern = Pattern.compile("http://www.radlex.org/.+");
    m = radlexPattern.matcher(conceptIri);
    if (m.matches()) {
      return "RADLEX";
    }
    return null;
  }

  private static String getServiceAddress(String serviceEndpoint, String paramName)
      throws UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder(serviceEndpoint);
    sb.append("q=").append(URLEncoder.encode(paramName, "UTF-8"));
    sb.append("&exact_match=true");
    sb.append("&display_context=false");
    sb.append("&display_links=false");
    sb.append("&pagesize=1");
    sb.append("&include=prefLabel,definition,notation");
    return sb.toString();
  }
}
