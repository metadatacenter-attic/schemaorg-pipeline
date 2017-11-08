package org.metadatacenter.schemaorg.pipeline.experimental;

import java.util.Optional;

public interface IdResolver {

  Optional<String> resolve(String id, String namespace);
}
