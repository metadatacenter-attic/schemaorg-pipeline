package org.metadatacenter.schemaorg.pipeline.mapping;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import com.google.common.collect.Lists;

public abstract class Parameters {

  private final Properties properties = new Properties();

  public void set(String key, String value) {
    properties.put(key, value);
  }

  @SuppressWarnings("unchecked")
  public void add(String key, String value) {
    List<String> list = (List<String>) properties.get(key);
    if (list == null) {
      list = Lists.newArrayList();
      properties.put(key, list);
    }
    list.add(value);
  }

  public String get(String key) {
    return properties.get(key).toString();
  }

  @SuppressWarnings("unchecked")
  public Iterator<String> iterate(String key) {
    final List<String> listOfValues = Lists.newArrayList();
    Object value = properties.get(key);
    if (value instanceof String) {
      listOfValues.add((String) value);
    } else if (value instanceof List) {
      listOfValues.addAll((List<String>) value);
    }
    return listOfValues.iterator();
  }
}
