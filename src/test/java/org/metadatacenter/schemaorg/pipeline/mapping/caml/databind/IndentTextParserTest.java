package org.metadatacenter.schemaorg.pipeline.mapping.caml.databind;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapping.caml.databind.IndentTextParser;
import org.metadatacenter.schemaorg.pipeline.mapping.caml.databind.Section;

public class IndentTextParserTest {

  @Test
  public void shouldParseSingleLine() {
    final String text = "line";
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

    // Assertions
    List<Section> subSections = root.getSubSections();
    assertThat(subSections.size(), equalTo(1));
    assertThat(subSections.get(0).getText(), equalTo("line"));
    assertThat(subSections.get(0).getDepth(), equalTo(0));
    assertThat(subSections.get(0).getParent(), equalTo(root));
    assertThat(subSections.get(0).getSubSections().isEmpty(), equalTo(true));
  }

  @Test
  public void shouldParseFlatHierarchy() {
    final String text = 
          "line1\n"
        + "line2\n"
        + "line3\n"
        + "line4\n";
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

    // Assertions
    List<Section> subSections = root.getSubSections();
    assertThat(subSections.size(), equalTo(4));
    
    Section subSection1 = subSections.get(0);
    assertThat(subSection1.getText(), equalTo("line1"));
    assertThat(subSection1.getDepth(), equalTo(0));
    assertThat(subSection1.getParent(), equalTo(root));
    assertThat(subSection1.getSubSections().size(), equalTo(0));

    Section subSection2 = subSections.get(1);
    assertThat(subSection2.getText(), equalTo("line2"));
    assertThat(subSection2.getDepth(), equalTo(0));
    assertThat(subSection2.getParent(), equalTo(root));
    assertThat(subSection2.getSubSections().size(), equalTo(0));

    Section subSection3 = subSections.get(2);
    assertThat(subSection3.getText(), equalTo("line3"));
    assertThat(subSection3.getDepth(), equalTo(0));
    assertThat(subSection3.getParent(), equalTo(root));
    assertThat(subSection3.getSubSections().size(), equalTo(0));

    Section subSection4 = subSections.get(3);
    assertThat(subSection4.getText(), equalTo("line4"));
    assertThat(subSection4.getDepth(), equalTo(0));
    assertThat(subSection4.getParent(), equalTo(root));
    assertThat(subSection4.getSubSections().size(), equalTo(0));
  }
  
  @Test
  public void shouldParseStairsHierarchy() {
    final String text = 
          "line1\n"
        + "  line2\n"
        + "    line3\n"
        + "      line4\n";
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

 // Assertions
    List<Section> subSections = root.getSubSections();
    assertThat(subSections.size(), equalTo(1));
    
    Section subSection = subSections.get(0);
    assertThat(subSection.getText(), equalTo("line1"));
    assertThat(subSection.getDepth(), equalTo(0));
    assertThat(subSection.getParent(), equalTo(root));
    assertThat(subSection.getSubSections().size(), equalTo(1));

    Section subSubSection = subSection.getSubSections().get(0);
    assertThat(subSubSection.getText(), equalTo("line2"));
    assertThat(subSubSection.getDepth(), equalTo(2));
    assertThat(subSubSection.getParent(), equalTo(subSection));
    assertThat(subSubSection.getSubSections().size(), equalTo(1));

    Section subSubSubSection = subSubSection.getSubSections().get(0);
    assertThat(subSubSubSection.getText(), equalTo("line3"));
    assertThat(subSubSubSection.getDepth(), equalTo(4));
    assertThat(subSubSubSection.getParent(), equalTo(subSubSection));
    assertThat(subSubSubSection.getSubSections().size(), equalTo(1));

    Section subSubSubSubSection = subSubSubSection.getSubSections().get(0);
    assertThat(subSubSubSubSection.getText(), equalTo("line4"));
    assertThat(subSubSubSubSection.getDepth(), equalTo(6));
    assertThat(subSubSubSubSection.getParent(), equalTo(subSubSubSection));
    assertThat(subSubSubSubSection.getSubSections().size(), equalTo(0));
  }

  @Test
  public void shouldParsePyramidHierarchy() {
    final String text = 
          "line1\n"
        + "  line2\n"
        + "    line3\n"
        + "      line4\n"
        + "    line5\n"
        + "  line6\n"
        + "line7";
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

    // Assertions
    List<Section> subSections = root.getSubSections();
    assertThat(subSections.size(), equalTo(2));
    
    Section subSection1 = subSections.get(0);
    assertThat(subSection1.getText(), equalTo("line1"));
    assertThat(subSection1.getDepth(), equalTo(0));
    assertThat(subSection1.getParent(), equalTo(root));
    assertThat(subSection1.getSubSections().size(), equalTo(2));

    Section subSubSection1 = subSection1.getSubSections().get(0);
    assertThat(subSubSection1.getText(), equalTo("line2"));
    assertThat(subSubSection1.getDepth(), equalTo(2));
    assertThat(subSubSection1.getParent(), equalTo(subSection1));
    assertThat(subSubSection1.getSubSections().size(), equalTo(2));

    Section subSubSubSection1 = subSubSection1.getSubSections().get(0);
    assertThat(subSubSubSection1.getText(), equalTo("line3"));
    assertThat(subSubSubSection1.getDepth(), equalTo(4));
    assertThat(subSubSubSection1.getParent(), equalTo(subSubSection1));
    assertThat(subSubSubSection1.getSubSections().size(), equalTo(1));

    Section subSubSubSubSection1 = subSubSubSection1.getSubSections().get(0);
    assertThat(subSubSubSubSection1.getText(), equalTo("line4"));
    assertThat(subSubSubSubSection1.getDepth(), equalTo(6));
    assertThat(subSubSubSubSection1.getParent(), equalTo(subSubSubSection1));
    assertThat(subSubSubSubSection1.getSubSections().size(), equalTo(0));

    Section subSubSubSection2 = subSubSection1.getSubSections().get(1);
    assertThat(subSubSubSection2.getText(), equalTo("line5"));
    assertThat(subSubSubSection2.getDepth(), equalTo(4));
    assertThat(subSubSubSection2.getParent(), equalTo(subSubSection1));
    assertThat(subSubSubSection2.getSubSections().size(), equalTo(0));

    Section subSubSection2 = subSection1.getSubSections().get(1);
    assertThat(subSubSection2.getText(), equalTo("line6"));
    assertThat(subSubSection2.getDepth(), equalTo(2));
    assertThat(subSubSection2.getParent(), equalTo(subSection1));
    assertThat(subSubSection2.getSubSections().size(), equalTo(0));

    Section subSection2 = subSections.get(1);
    assertThat(subSection2.getText(), equalTo("line7"));
    assertThat(subSection2.getDepth(), equalTo(0));
    assertThat(subSection2.getParent(), equalTo(root));
    assertThat(subSection2.getSubSections().size(), equalTo(0));
  }

  @Test
  public void shouldParseMixedHierarchy() {
    final String text = 
          "line1\n"
        + "  line2\n"
        + "  line3\n"
        + "    line4\n"
        + "  line5";
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

 // Assertions
    List<Section> subSections = root.getSubSections();
    assertThat(subSections.size(), equalTo(1));
    
    Section subSection = subSections.get(0);
    assertThat(subSection.getText(), equalTo("line1"));
    assertThat(subSection.getDepth(), equalTo(0));
    assertThat(subSection.getParent(), equalTo(root));
    assertThat(subSection.getSubSections().size(), equalTo(3));

    Section subSubSection1 = subSection.getSubSections().get(0);
    assertThat(subSubSection1.getText(), equalTo("line2"));
    assertThat(subSubSection1.getDepth(), equalTo(2));
    assertThat(subSubSection1.getParent(), equalTo(subSection));
    assertThat(subSubSection1.getSubSections().size(), equalTo(0));

    Section subSubSection2 = subSection.getSubSections().get(1);
    assertThat(subSubSection2.getText(), equalTo("line3"));
    assertThat(subSubSection2.getDepth(), equalTo(2));
    assertThat(subSubSection2.getParent(), equalTo(subSection));
    assertThat(subSubSection2.getSubSections().size(), equalTo(1));
    
    Section subSubSubSection = subSubSection2.getSubSections().get(0);
    assertThat(subSubSubSection.getText(), equalTo("line4"));
    assertThat(subSubSubSection.getDepth(), equalTo(4));
    assertThat(subSubSubSection.getParent(), equalTo(subSubSection2));
    assertThat(subSubSubSection.getSubSections().size(), equalTo(0));

    Section subSubSection3 = subSection.getSubSections().get(2);
    assertThat(subSubSection3.getText(), equalTo("line5"));
    assertThat(subSubSection3.getDepth(), equalTo(2));
    assertThat(subSubSection3.getParent(), equalTo(subSection));
    assertThat(subSubSection3.getSubSections().size(), equalTo(0));
  }
}
