package org.metadatacenter.schemaorg.pipeline.rml.databind;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Ignore;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapmodel.ArrayNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.MapNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.ObjectNode;
import org.metadatacenter.schemaorg.pipeline.mapmodel.PathNode;
import static org.hamcrest.MatcherAssert.assertThat;

public class RmlMapperTest {

  @Test
  public void shouldParsePathMap() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rml:reference \"/path1\" ]\n" + 
        "   ].";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    // Assertions
    assertThat(mapNode.get("attribute1").isPathNode(), equalTo(true));
    assertThat(mapNode.get("attribute1").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isPairNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isArrayNode(), equalTo(false));
    
    PathNode pathNode = (PathNode) mapNode.get("attribute1");
    assertThat(pathNode.getAbsolutePath(), equalTo("/root/path1"));
    assertThat(pathNode.getRelativePath(), equalTo("/path1"));
    assertThat(pathNode.getValue(), equalTo("/path1"));
  }

  @Test
  public void shouldParseConstantMap() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rr:constant \"constant1\" ]\n" + 
        "   ].";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    // Assertions
    assertThat(mapNode.get("attribute1").isPathNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isConstantNode(), equalTo(true));
    assertThat(mapNode.get("attribute1").isPairNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParseObjectMap() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Map1> ]\n" + 
        "   ].\n" + 
        "<#Map1>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root/path1\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type2\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute2;\n" + 
        "      rr:objectMap [ rml:reference \"/path2\" ]\n" + 
        "   ].";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    // Assertions
    assertThat(mapNode.get("attribute1").isPathNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isPairNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isObjectNode(), equalTo(true));
    assertThat(mapNode.get("attribute1").isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParsePathArrayMap() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rml:reference \"/path1\" ];\n" + 
        "      rr:objectMap [ rml:reference \"/path2\" ]\n" + 
        "   ].";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    // Assertions
    assertThat(mapNode.get("attribute1").isPathNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isPairNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isArrayNode(), equalTo(true));
    
    ArrayNode prefixes = (ArrayNode) mapNode.get("attribute1");
    assertThat(prefixes.get(0).isPathNode(), equalTo(true));
    assertThat(prefixes.get(0).isConstantNode(), equalTo(false));
    assertThat(prefixes.get(0).isPairNode(), equalTo(false));
    assertThat(prefixes.get(0).isObjectNode(), equalTo(false));
    assertThat(prefixes.get(0).isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParseConstantArrayMap() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rr:constant \"constant1\" ];\n" + 
        "      rr:objectMap [ rr:constant \"constant2\" ]\n" + 
        "   ].";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    // Assertions
    assertThat(mapNode.get("attribute1").isPathNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isPairNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isArrayNode(), equalTo(true));
    
    ArrayNode prefixes = (ArrayNode) mapNode.get("attribute1");
    assertThat(prefixes.get(0).isPathNode(), equalTo(false));
    assertThat(prefixes.get(0).isConstantNode(), equalTo(true));
    assertThat(prefixes.get(0).isPairNode(), equalTo(false));
    assertThat(prefixes.get(0).isObjectNode(), equalTo(false));
    assertThat(prefixes.get(0).isArrayNode(), equalTo(false));
  }

  @Ignore
  @Test
  public void shouldParseObjectArrayMap() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Map1> ];\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Map2> ]\n" + 
        "   ].\n" + 
        "<#Map1>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root/parent1\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type2\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute2;\n" + 
        "      rr:objectMap [ rml:reference \"/path1\" ]\n" + 
        "   ].\n" +
        "<#Map2>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root/parent2\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type3\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute3;\n" + 
        "      rr:objectMap [ rml:reference \"/path2\" ]\n" + 
        "   ].";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    System.out.println(mapNode);
    // Assertions
    assertThat(mapNode.get("attribute1").isPathNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isConstantNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isPairNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isObjectNode(), equalTo(false));
    assertThat(mapNode.get("attribute1").isArrayNode(), equalTo(true));
    
    ArrayNode arrayNode = (ArrayNode) mapNode.get("attribute1");
    assertThat(arrayNode.get(0).isPathNode(), equalTo(false));
    assertThat(arrayNode.get(0).isConstantNode(), equalTo(false));
    assertThat(arrayNode.get(0).isPairNode(), equalTo(false));
    assertThat(arrayNode.get(0).isObjectNode(), equalTo(true));
    assertThat(arrayNode.get(0).isArrayNode(), equalTo(false));
  }

  @Test
  public void shouldParseMappingText_CheckValue() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rml:reference \"/path1\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute2;\n" + 
        "      rr:objectMap [ rr:constant \"constant1\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute3;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Map1> ]\n" + 
        "   ].\n" + 
        "<#Map1>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root/parent1\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type2\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute4;\n" + 
        "      rr:objectMap [ rml:reference \"/path2\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute5;\n" + 
        "      rr:objectMap [ rr:constant \"constant2\" ]\n" + 
        "   ].\n";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    // Assertions
    assertThat(mapNode.get("attribute1").getValue(), equalTo("/path1"));
    assertThat(mapNode.get("attribute2").getValue(), equalTo("constant1"));
    assertThat(mapNode.get("attribute3").get("attribute4").getValue(), equalTo("/path2"));
    assertThat(mapNode.get("attribute3").get("attribute5").getValue(), equalTo("constant2"));
  }

  @Test
  public void shouldParseMappingText_CheckPathHierarchy() {
    final String rmlMapping = 
        "@prefix rr: <http://www.w3.org/ns/r2rml#>.\n" + 
        "@prefix rml: <http://semweb.mmlab.be/ns/rml#>.\n" + 
        "@prefix schema: <http://schema.org/>.\n" + 
        "<#Root>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type1\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute1;\n" + 
        "      rr:objectMap [ rml:reference \"/path1\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute2;\n" + 
        "      rr:objectMap [ rr:constant \"constant1\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute3;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Map1> ];\n" + 
        "   ].\n" + 
        "<#Map1>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root/parent1\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type2\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute4;\n" + 
        "      rr:objectMap [ rml:reference \"/path2\" ]\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute5;\n" + 
        "      rr:objectMap [ rr:constant \"constant2\" ]\n" + 
        "   ];\n" + 
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute6;\n" + 
        "      rr:objectMap [ rr:parentTriplesMap <#Map2> ];\n" + 
        "   ].\n" + 
        "<#Map2>\n" + 
        "   rml:logicalSource [\n" + 
        "      rml:source \"\";\n" + 
        "      rml:iterator \"/root/parent1/parent2\"\n" + 
        "   ];\n" +
        "   rr:subjectMap [\n" + 
        "      rr:template \"\";\n" + 
        "      rr:class schema:Type3\n" + 
        "   ];\n" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute7;\n" + 
        "      rr:objectMap [ rml:reference \"/path3\" ]\n" + 
        "   ];" +
        "   rr:predicateObjectMap [\n" + 
        "      rr:predicate schema:attribute8;\n" + 
        "      rr:objectMap [ rml:reference \"/path4\" ]\n" + 
        "   ].";
    RmlMapper mapper = new RmlMapper();
    MapNode mapNode = mapper.readText(rmlMapping);
    // Assertions
    ObjectNode root = (ObjectNode) mapNode;
    assertThat(root.getRelativePath(), equalTo(""));
    assertThat(root.getAbsolutePath(), equalTo(""));
    
    PathNode a1 = (PathNode) mapNode.get("attribute1");
    assertThat(a1.getRelativePath(), equalTo("/path1"));
    assertThat(a1.getAbsolutePath(), equalTo("/root/path1"));
    
    ObjectNode a3 = (ObjectNode) mapNode.get("attribute3");
    assertThat(a3.getRelativePath(), equalTo("/root/parent1"));
    assertThat(a3.getAbsolutePath(), equalTo("/root/parent1"));
    
    PathNode a4 = (PathNode) mapNode.get("attribute3").get("attribute4");
    assertThat(a4.getRelativePath(), equalTo("/path2"));
    assertThat(a4.getAbsolutePath(), equalTo("/root/parent1/path2"));
    
    ObjectNode a6 = (ObjectNode) mapNode.get("attribute3").get("attribute6");
    assertThat(a6.getRelativePath(), equalTo("/root/parent1/parent2"));
    assertThat(a6.getAbsolutePath(), equalTo("/root/parent1/parent2"));
    
    PathNode a7 = (PathNode) mapNode.get("attribute3").get("attribute6").get("attribute7");
    assertThat(a7.getRelativePath(), equalTo("/path3"));
    assertThat(a7.getAbsolutePath(), equalTo("/root/parent1/parent2/path3"));
    
    PathNode a8 = (PathNode) mapNode.get("attribute3").get("attribute6").get("attribute8");
    assertThat(a8.getRelativePath(), equalTo("/path4"));
    assertThat(a8.getAbsolutePath(), equalTo("/root/parent1/parent2/path4"));
  }
}
