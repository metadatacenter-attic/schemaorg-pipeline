package org.metadatacenter.schemaorg.pipeline.experimental;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;

public class IdentifiersResolver {

  private static Logger logger = LoggerFactory.getLogger(IdentifiersResolver.class);

  private JSONObject dictionary;

  public IdentifiersResolver() {
    String xmlContent = readResource("registry.xml");
    String xsltContent = readResource("dictionary.xslt");
    String dictionaryXml = transformXml(xmlContent, xsltContent);
    this.dictionary = convertToJson(dictionaryXml);
    logger.debug("Successfully creating a dictionary from identifiers.org registry\n" + dictionary.toString(2));
  }

  public Optional<String> resolve(@Nonnull String id, @Nonnull String namespace) {
    String completeId = null;
    if (!Strings.isNullOrEmpty(namespace)) {
      String namespaceKey = "ns." + namespace;
      JSONArray namespaceDetails = dictionary.getJSONArray(namespaceKey);
      String pattern = namespaceDetails.getString(1); // index = 1 is the id pattern
      String normalizedId = removeNamespacePrefix(id, namespace);
      Matcher m = Pattern.compile(pattern).matcher(normalizedId);
      if (m.matches()) {
        String urischeme = namespaceDetails.getString(2); // index = 2 is the URI scheme
        completeId = urischeme + normalizedId;
      }
    }
    return Optional.ofNullable(completeId);
  }

  private static String removeNamespacePrefix(String codedId, String namespace) {
    String namespacePrefix = namespace + ":";
    return codedId.replace(namespacePrefix, "");
  }

  private static String readResource(String resource) {
    InputStream in = IdentifiersResolver.class.getClassLoader().getResourceAsStream(resource);
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

  private static String transformXml(String xmlString, String xsltString) {
    final Document xmlDoc = XmlUtils.parseToXml(xmlString);
    final StringWriter resultWriter = new StringWriter();
    try {
      Transformer transformer = createXsltTransformer(xsltString);
      transformer.transform(new DOMSource(xmlDoc), new StreamResult(resultWriter));
      return resultWriter.toString();
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  private static Transformer createXsltTransformer(String xsltString)
      throws TransformerConfigurationException {
    TransformerFactory transFactory = TransformerFactory.newInstance();
    InputStream in = new ByteArrayInputStream(xsltString.getBytes(Charsets.UTF_8));
    return transFactory.newTransformer(new StreamSource(in));
  }

  private static JSONObject convertToJson(String dictionaryXml) {
    return XML.toJSONObject(dictionaryXml).getJSONObject("dictionary");
  }
}
