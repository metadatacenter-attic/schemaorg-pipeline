package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;
import java.util.Properties;

public interface IdExpander {

  Optional<String> expand(String id, String namespace);

  Optional<String> expand(String id, String namespace, Properties additionalParameters);
}
