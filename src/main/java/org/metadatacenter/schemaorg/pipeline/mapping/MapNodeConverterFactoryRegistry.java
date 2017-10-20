package org.metadatacenter.schemaorg.pipeline.mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import com.google.common.collect.Maps;

public class MapNodeConverterFactoryRegistry {

  private final Map<String, MapNodeConverterFactory> converterFactories = Maps.newHashMap();

  public void registerConverterFactory(String converterName, MapNodeConverterFactory converterFactory) {
    converterFactories.put(converterName, converterFactory);
  }

  public void unregisterConverterFactory(String converterName) {
    converterFactories.remove(converterName);
  }

  public MapNodeConverterFactory getConverterFactory(String converterName)
      throws ConverterNotFoundException {
    MapNodeConverterFactory converterFactory = converterFactories.get(converterName);
    if (converterFactory == null) {
      throw new ConverterNotFoundException(converterName, this);
    }
    return converterFactory;
  }

  public Set<String> getConverterNames() {
    return Collections.unmodifiableSet(converterFactories.keySet());
  }

  public Collection<MapNodeConverterFactory> getConverterFactories() {
    return Collections.unmodifiableCollection(converterFactories.values());
  }
}
