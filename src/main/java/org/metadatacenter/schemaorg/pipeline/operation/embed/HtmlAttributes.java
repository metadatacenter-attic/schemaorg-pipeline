package org.metadatacenter.schemaorg.pipeline.operation.embed;

import java.util.Map;
import com.google.common.collect.Maps;

public class HtmlAttributes {

  public static Map<String, String> id(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("id", value);
    return attributeMap;
  }

  public static Map<String, String> cssClass(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("class", value);
    return attributeMap;
  }

  public static Map<String, String> name(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("name", value);
    return attributeMap;
  }

  public static Map<String, String> style(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("style", value);
    return attributeMap;
  }

  public static Map<String, String> title(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("title", value);
    return attributeMap;
  }

  public static Map<String, String> alt(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("alt", value);
    return attributeMap;
  }

  public static Map<String, String> content(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("content", value);
    return attributeMap;
  }

  public static Map<String, String> lang(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("lang", value);
    return attributeMap;
  }

  public static Map<String, String> href(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("href", value);
    return attributeMap;
  }

  public static Map<String, String> src(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("src", value);
    return attributeMap;
  }

  public static Map<String, String> target(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("target", value);
    return attributeMap;
  }

  public static Map<String, String> rel(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("rel", value);
    return attributeMap;
  }

  public static Map<String, String> charset(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("charset", value);
    return attributeMap;
  }

  public static Map<String, String> type(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("type", value);
    return attributeMap;
  }

  public static Map<String, String> value(String value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("value", value);
    return attributeMap;
  }

  public static Map<String, String> width(int value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("width", value + "");
    return attributeMap;
  }

  public static Map<String, String> height(int value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("height", value + "");
    return attributeMap;
  }

  public static Map<String, String> colspan(int value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("colspan", value + "");
    return attributeMap;
  }

  public static Map<String, String> rowspan(int value) {
    Map<String, String> attributeMap = Maps.newHashMap();
    attributeMap.put("rowspan", value + "");
    return attributeMap;
  }
}
