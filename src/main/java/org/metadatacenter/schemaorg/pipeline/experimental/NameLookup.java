package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;

public interface NameLookup {

  Optional<String> find(String name);
}
