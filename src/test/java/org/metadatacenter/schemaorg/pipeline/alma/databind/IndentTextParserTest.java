package org.metadatacenter.schemaorg.pipeline.alma.databind;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import java.util.List;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.alma.databind.IndentTextParser;
import org.metadatacenter.schemaorg.pipeline.alma.databind.Section;

public class IndentTextParserTest {

  @Test
  public void shouldParseSingleLine() {
    final String text = "line";
    IndentTextParser parser = new IndentTextParser();
    Section root = parser.parse(text);

    // Assertions
    List<Section> children = root.getChildren();
    assertThat(children.size(), equalTo(1));
    assertThat(children.get(0).getText(), equalTo("line"));
    assertThat(children.get(0).getDepth(), equalTo(0));
    assertThat(children.get(0).getParent(), equalTo(root));
    assertThat(children.get(0).getChildren().isEmpty(), equalTo(true));
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
    List<Section> children = root.getChildren();
    assertThat(children.size(), equalTo(4));
    
    Section child1 = children.get(0);
    assertThat(child1.getText(), equalTo("line1"));
    assertThat(child1.getDepth(), equalTo(0));
    assertThat(child1.getParent(), equalTo(root));
    assertThat(child1.getChildren().size(), equalTo(0));

    Section child2 = children.get(1);
    assertThat(child2.getText(), equalTo("line2"));
    assertThat(child2.getDepth(), equalTo(0));
    assertThat(child2.getParent(), equalTo(root));
    assertThat(child2.getChildren().size(), equalTo(0));

    Section child3 = children.get(2);
    assertThat(child3.getText(), equalTo("line3"));
    assertThat(child3.getDepth(), equalTo(0));
    assertThat(child3.getParent(), equalTo(root));
    assertThat(child3.getChildren().size(), equalTo(0));

    Section child4 = children.get(3);
    assertThat(child4.getText(), equalTo("line4"));
    assertThat(child4.getDepth(), equalTo(0));
    assertThat(child4.getParent(), equalTo(root));
    assertThat(child4.getChildren().size(), equalTo(0));
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
    List<Section> children = root.getChildren();
    assertThat(children.size(), equalTo(1));
    
    Section child1 = children.get(0);
    assertThat(child1.getText(), equalTo("line1"));
    assertThat(child1.getDepth(), equalTo(0));
    assertThat(child1.getParent(), equalTo(root));
    assertThat(child1.getChildren().size(), equalTo(1));

    Section child11 = child1.getChildren().get(0);
    assertThat(child11.getText(), equalTo("line2"));
    assertThat(child11.getDepth(), equalTo(2));
    assertThat(child11.getParent(), equalTo(child1));
    assertThat(child11.getChildren().size(), equalTo(1));

    Section child111 = child11.getChildren().get(0);
    assertThat(child111.getText(), equalTo("line3"));
    assertThat(child111.getDepth(), equalTo(4));
    assertThat(child111.getParent(), equalTo(child11));
    assertThat(child111.getChildren().size(), equalTo(1));

    Section child1111 = child111.getChildren().get(0);
    assertThat(child1111.getText(), equalTo("line4"));
    assertThat(child1111.getDepth(), equalTo(6));
    assertThat(child1111.getParent(), equalTo(child111));
    assertThat(child1111.getChildren().size(), equalTo(0));
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
    List<Section> children = root.getChildren();
    assertThat(children.size(), equalTo(2));
    
    Section child1 = children.get(0);
    assertThat(child1.getText(), equalTo("line1"));
    assertThat(child1.getDepth(), equalTo(0));
    assertThat(child1.getParent(), equalTo(root));
    assertThat(child1.getChildren().size(), equalTo(2));

    Section child11 = child1.getChildren().get(0);
    assertThat(child11.getText(), equalTo("line2"));
    assertThat(child11.getDepth(), equalTo(2));
    assertThat(child11.getParent(), equalTo(child1));
    assertThat(child11.getChildren().size(), equalTo(2));

    Section child111 = child11.getChildren().get(0);
    assertThat(child111.getText(), equalTo("line3"));
    assertThat(child111.getDepth(), equalTo(4));
    assertThat(child111.getParent(), equalTo(child11));
    assertThat(child111.getChildren().size(), equalTo(1));

    Section child1111 = child111.getChildren().get(0);
    assertThat(child1111.getText(), equalTo("line4"));
    assertThat(child1111.getDepth(), equalTo(6));
    assertThat(child1111.getParent(), equalTo(child111));
    assertThat(child1111.getChildren().size(), equalTo(0));

    Section child112 = child11.getChildren().get(1);
    assertThat(child112.getText(), equalTo("line5"));
    assertThat(child112.getDepth(), equalTo(4));
    assertThat(child112.getParent(), equalTo(child11));
    assertThat(child112.getChildren().size(), equalTo(0));

    Section child12 = child1.getChildren().get(1);
    assertThat(child12.getText(), equalTo("line6"));
    assertThat(child12.getDepth(), equalTo(2));
    assertThat(child12.getParent(), equalTo(child1));
    assertThat(child12.getChildren().size(), equalTo(0));

    Section child2 = children.get(1);
    assertThat(child2.getText(), equalTo("line7"));
    assertThat(child2.getDepth(), equalTo(0));
    assertThat(child2.getParent(), equalTo(root));
    assertThat(child2.getChildren().size(), equalTo(0));
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
    List<Section> children = root.getChildren();
    assertThat(children.size(), equalTo(1));
    
    Section child1 = children.get(0);
    assertThat(child1.getText(), equalTo("line1"));
    assertThat(child1.getDepth(), equalTo(0));
    assertThat(child1.getParent(), equalTo(root));
    assertThat(child1.getChildren().size(), equalTo(3));

    Section child11 = child1.getChildren().get(0);
    assertThat(child11.getText(), equalTo("line2"));
    assertThat(child11.getDepth(), equalTo(2));
    assertThat(child11.getParent(), equalTo(child1));
    assertThat(child11.getChildren().size(), equalTo(0));

    Section child12 = child1.getChildren().get(1);
    assertThat(child12.getText(), equalTo("line3"));
    assertThat(child12.getDepth(), equalTo(2));
    assertThat(child12.getParent(), equalTo(child1));
    assertThat(child12.getChildren().size(), equalTo(1));
    
    Section child121 = child12.getChildren().get(0);
    assertThat(child121.getText(), equalTo("line4"));
    assertThat(child121.getDepth(), equalTo(4));
    assertThat(child121.getParent(), equalTo(child12));
    assertThat(child121.getChildren().size(), equalTo(0));

    Section child13 = child1.getChildren().get(2);
    assertThat(child13.getText(), equalTo("line5"));
    assertThat(child13.getDepth(), equalTo(2));
    assertThat(child13.getParent(), equalTo(child1));
    assertThat(child13.getChildren().size(), equalTo(0));
  }
}
