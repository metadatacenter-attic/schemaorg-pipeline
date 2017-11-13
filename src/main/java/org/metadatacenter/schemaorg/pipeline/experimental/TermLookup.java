package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;

public interface TermLookup {

  Optional<String> find(String name);
}
