package org.metadatacenter.schemaorg.pipeline.mapping.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import static com.google.common.base.Preconditions.checkNotNull;

public class FunctionNode extends MapNode {

  private final String name;
  private final List<String> arguments;

  public FunctionNode(@Nonnull String name, @Nonnull List<String> arguments) {
    this.name = checkNotNull(name);
    this.arguments = Lists.newArrayList(checkNotNull(arguments));
  }

  public String getName() {
    return name;
  }

  public List<String> getArguments() {
    return Collections.unmodifiableList(arguments);
  }

  @Override
  public String getValue() {
    return getName();
  }

  @Override
  public boolean isFunctionNode() {
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, arguments);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof FunctionNode)) {
      return false;
    }
    FunctionNode other = (FunctionNode) obj;
    return Objects.equals(name, other.name)
        && Objects.equals(arguments, other.arguments);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("arguments", arguments)
        .toString();
  }
}
