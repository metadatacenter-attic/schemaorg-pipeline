package org.metadatacenter.sandbox;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class MustacheUtils {

  public static void compile(Writer writer, String template, Map<String, Object> scopes) throws IOException {
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile(new StringReader(template), "website");
    mustache.execute(writer, scopes);
  }

  private static File getFile(String pathname) {
    URL resourceUrl = MustacheUtils.class.getClassLoader().getResource(pathname);
    try {
      return new File(resourceUrl.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
