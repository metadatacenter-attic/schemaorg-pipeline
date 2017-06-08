package org.metadatacenter.sandbox;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class PipelineTest {

  public static void main(String[] args) throws Exception {
    String webPageTemplateFileName = args[0];
    String metadataTemplateFileName = args[1];
    String contentTemplateFileName = args[2];
    String dataDirectoryPathName = args[3];
    String contentTemplateName = Files.getNameWithoutExtension(contentTemplateFileName);
    String precompiledFileName = HandlebarsUtils.compileTemplate(contentTemplateFileName, contentTemplateName);

    File dataDirectory = getFile(dataDirectoryPathName);
    for (String dataFileName : dataDirectory.list()) {
      try {
        System.out.print("Processing " + dataFileName + "... ");
        dataFileName = dataDirectoryPathName + "/" + dataFileName;
        String metadata = JqUtils.transform(metadataTemplateFileName, dataFileName);
        String content = HandlebarsUtils.getHandlebarsSnippet(
            precompiledFileName,
            contentTemplateName,
            dataFileName);

        String webPageTemplate = Files.toString(getFile(webPageTemplateFileName), Charsets.UTF_8);

        String outputFileName = getOutputFileName(dataFileName);
        Writer writer = new PrintWriter(outputFileName, "UTF-8");
        MustacheUtils.compile(writer, webPageTemplate, new HashMap<String, Object>() {{
          put("metadata", metadata);
          put("content", content);
        }});
        writer.flush();
        writer.close();
        System.out.println("Success");
      } catch (Exception e) {
        System.out.println("Failed: " + e.getMessage());
      }
    }
  }

  private static String getOutputFileName(String inputFileName) {
    String outputName = Files.getNameWithoutExtension(inputFileName);
    return outputName + ".html";
  }

  private static File getFile(String pathname) {
    URL resourceUrl = PipelineTest.class.getClassLoader().getResource(pathname);
    try {
      return new File(resourceUrl.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
