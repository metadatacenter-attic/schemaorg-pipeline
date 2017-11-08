package org.metadatacenter.schemaorg.pipeline.experimental;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {

  public static Document parseToXml(String text) {
    try {
      return DocumentBuilderFactory.newInstance()
          .newDocumentBuilder()
          .parse(new InputSource(new StringReader(text)));
    } catch (SAXException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean containTagName(Document doc, String tagName) {
    NodeList nodeList = doc.getElementsByTagName(tagName);
    return nodeList.getLength() > 0;
  }

  public static String getCharacterDataFromElement(Element element) {
    return element.getFirstChild().getNodeValue();
  }
}
