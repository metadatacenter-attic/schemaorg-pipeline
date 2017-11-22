package org.metadatacenter.schemaorg.pipeline.caml.databind;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapmodel.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.ObjectNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.PathNode;

public class AttributeMapperTest {

  @Test
  public void shouldParsePathMap() {
    final String text = "a: /path";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(true));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("a").isPairNode(), equalTo(false));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParseConstantMap() {
    final String text = "a: 'constant'";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(false));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(true));
    assertThat(mapNode.get("a").isPairNode(), equalTo(false));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParsePairMap() {
    final String text = "a: ('constant1', 'constant2')";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(false));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("a").isPairNode(), equalTo(true));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParseObjectMap() {
    final String text = 
          "a: /path\n"
        + "  b: /path\n"
        + "  c: 'constant'";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(false));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("a").isPairNode(), equalTo(false));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(true));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParsePathArrayMap() {
    final String text = 
          "a: /path1\n"
        + "a: /path2";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(false));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("a").isPairNode(), equalTo(false));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(true));
  }

  @Test
  public void shouldParseConstantArrayMap() {
    final String text = 
          "a: 'constant1'\n"
        + "a: 'constant2'";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(false));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("a").isPairNode(), equalTo(false));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(true));
  }

  @Test
  public void shouldParsePairArrayMap() {
    final String text = 
          "a: ('constant1', 'constant2')\n"
        + "a: ('constant3', 'constant4')";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(false));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("a").isPairNode(), equalTo(false));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(true));
  }

  @Test
  public void shouldParseObjectArrayMap() {
    final String text = 
          "a: /parent1\n"
        + "  b: /path1\n"
        + "  c: 'constant1'\n"
        + "a: /parent2\n"
        + "  b: /path2\n"
        + "  c: 'constant2'";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a").isPathNode(), equalTo(false));
    assertThat(mapNode.get("a").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("a").isPairNode(), equalTo(false));
    assertThat(mapNode.get("a").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("a").isArrayNode(), equalTo(true));
  }

  @Test
  public void shouldParseMappingText_CheckValue() {
    final String text =
          "a1: /path1\n"
        + "a2: 'constant1'\n"
        + "a3: ('key1', 'value1')\n"
        + "a4: /parent1\n"
        + "  a5: /path2\n"
        + "  a6: 'constant2'\n"
        + "  a7: ('key2', 'value2')";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    assertThat(mapNode.get("a1").getValue(), equalTo("/path1"));
    assertThat(mapNode.get("a2").getValue(), equalTo("constant1"));
    assertThat(mapNode.get("a3").getValue(), equalTo("(key1,value1)"));
    assertThat(mapNode.get("a4").getValue(), equalTo("/parent1"));
    assertThat(mapNode.get("a4").get("a5").getValue(), equalTo("/path2"));
    assertThat(mapNode.get("a4").get("a6").getValue(), equalTo("constant2"));
    assertThat(mapNode.get("a4").get("a6").getValue(), equalTo("constant2"));
    assertThat(mapNode.get("a4").get("a7").getValue(), equalTo("(key2,value2)"));
  }

  @Test
  public void shouldParseMappingText_CheckPathHierarchy() {
    final String text =
          "a1: /path1\n"
        + "a2: 'constant1'\n"
        + "a3: /parent1\n"
        + "  a4: /path2\n"
        + "  a5: 'constant2'\n"
        + "  a6: /parent2\n"
        + "    a7: /path3\n"
        + "    a8: /path4";
    AttributeMapper mapper = new AttributeMapper();
    MapNode mapNode = mapper.readText(text);
    // Assertions
    PathNode a1 = (PathNode) mapNode.get("a1");
    assertThat(a1.getRelativePath(), equalTo("/path1"));
    assertThat(a1.getAbsolutePath(), equalTo("/path1"));
    
    ObjectNode a3 = (ObjectNode) mapNode.get("a3");
    assertThat(a3.getRelativePath(), equalTo("/parent1"));
    assertThat(a3.getAbsolutePath(), equalTo("/parent1"));
    
    PathNode a4 = (PathNode) mapNode.get("a3").get("a4");
    assertThat(a4.getRelativePath(), equalTo("/path2"));
    assertThat(a4.getAbsolutePath(), equalTo("/parent1/path2"));
    
    ObjectNode a6 = (ObjectNode) mapNode.get("a3").get("a6");
    assertThat(a6.getRelativePath(), equalTo("/parent2"));
    assertThat(a6.getAbsolutePath(), equalTo("/parent1/parent2"));
    
    PathNode a7 = (PathNode) mapNode.get("a3").get("a6").get("a7");
    assertThat(a7.getRelativePath(), equalTo("/path3"));
    assertThat(a7.getAbsolutePath(), equalTo("/parent1/parent2/path3"));
    
    PathNode a8 = (PathNode) mapNode.get("a3").get("a6").get("a8");
    assertThat(a8.getRelativePath(), equalTo("/path4"));
    assertThat(a8.getAbsolutePath(), equalTo("/parent1/parent2/path4"));
  }
}
