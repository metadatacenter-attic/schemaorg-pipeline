package org.metadatacenter.sandbox;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class HandlebarsUtils {

  public static String compileTemplate(String templateFileName, String templateName) {
    try {
      File templateFile = getFile(templateFileName);
      String precompiledFileName = templateName + ".js";
      String commandLine = String.format("handlebars %s -f %s -e content", templateFile.getAbsolutePath(), precompiledFileName);
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec(commandLine);
      pr.waitFor();
      return precompiledFileName;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getHandlebarsSnippet(String precompiledFileName, String templateName, String dataResource) {
    return new StringBuffer()
        .append(getIncludePrecompiledTemplate(precompiledFileName))
        .append("\n")
        .append(getTransformationScript(dataResource, templateName))
        .toString();
  }

  private static String getIncludePrecompiledTemplate(String precompiledFile) {
    return String.format("<script src=\"%s\"></script>", precompiledFile);
  }

  private static String getTransformationScript(String dataResource, String templateName) {
    return String.format("<script>\n" +
        "$.getJSON('%s', function(context) {\n" +
        "   var content = Handlebars.templates.%s(context);\n" +
        "   document.getElementById('content-first').innerHTML = content;\n" +
        "});\n" +
        "</script>", dataResource, templateName);
  }

  private static File getFile(String pathname) {
    URL resourceUrl = HandlebarsUtils.class.getClassLoader().getResource(pathname);
    try {
      return new File(resourceUrl.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
