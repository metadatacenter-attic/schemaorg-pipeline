package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;

public interface IdExpander {

  Optional<String> expand(String id, String namespace);
}
