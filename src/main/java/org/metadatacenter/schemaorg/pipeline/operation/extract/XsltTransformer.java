package org.metadatacenter.schemaorg.pipeline.operation.extract;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import com.google.common.base.Charsets;

public class XsltTransformer {

  private final Transformer transformer;

  private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

  // Avoid external instantiation
  private XsltTransformer(@Nonnull Transformer transformer) {
    this.transformer = checkNotNull(transformer);
  }

  public static XsltTransformer newTransformer(String xsltString) {
    try {
      TransformerFactory transFactory = TransformerFactory.newInstance();
      InputStream in = new ByteArrayInputStream(xsltString.getBytes(Charsets.UTF_8));
      Transformer transformer = transFactory.newTransformer(new StreamSource(in));
      return new XsltTransformer(transformer);
    } catch (TransformerConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public String transform(String xmlString) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    transform(xmlString, out);
    return out.toString();
  }

  public String transform(InputStream in) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    transform(in, out);
    return out.toString();
  }

  public void transform(String xmlString, OutputStream out) {
    InputStream in = new ByteArrayInputStream(xmlString.getBytes(Charsets.UTF_8));
    transform(in, out);
  }

  public void transform(InputStream in, OutputStream out) {
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document document = dBuilder.parse(in);
      transform(document, out);
    } catch (Exception e) {
      System.err.println(e); // TODO Throw a custom exception
    }
  }
  
  private void transform(Document document, OutputStream out) throws TransformerException {
    document.getDocumentElement().normalize();
    transformer.transform(new DOMSource(document), new StreamResult(out));
  }
}
