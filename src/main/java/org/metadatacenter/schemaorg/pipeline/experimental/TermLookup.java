package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;
import java.util.Properties;

public interface TermLookup {

  Optional<String> find(String name);

  Optional<String> find(String name, Properties additionalParameters);
}
