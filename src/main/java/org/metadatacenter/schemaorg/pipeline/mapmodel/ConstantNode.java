package org.metadatacenter.schemaorg.pipeline.mapmodel;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;

public class ConstantNode extends MapNode {

  private final String value;

  public ConstantNode(@Nonnull String value) {
    this.value = checkNotNull(value);
  }

  @Override
  public boolean isConstantNode() {
    return true;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ConstantNode)) {
      return false;
    }
    ConstantNode other = (ConstantNode) obj;
    return Objects.equals(value, other.value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("value", value)
        .toString();
  }
}
