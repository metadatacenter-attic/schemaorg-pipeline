package org.metadatacenter.schemaorg.pipeline.mapping;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import org.junit.Test;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.SparqlConstructConverter;
import org.metadatacenter.schemaorg.pipeline.mapping.converter.XsltConverter;

public class MapConverterTest {

  @Test
  public void shouldReturnSparqlConstructConverter() throws ConverterNotFoundException {
    MapConverter mapConverter = MapConverter.newInstance();
    MapNodeConverter converter = mapConverter.use("sparql-construct");
    // Assertions
    assertThat(converter, is(instanceOf(SparqlConstructConverter.class)));
  }

  @Test
  public void shouldReturnXsltConverter() throws ConverterNotFoundException {
    MapConverter mapConverter = MapConverter.newInstance();
    MapNodeConverter converter = mapConverter.use("xslt");
    // Assertions
    assertThat(converter, is(instanceOf(XsltConverter.class)));
  }
  
  @Test(expected = ConverterNotFoundException.class)
  public void shouldFailReturnConverter() throws ConverterNotFoundException {
    MapConverter mapConverter = MapConverter.newInstance();
    mapConverter.use("random-converter");
  }
}
