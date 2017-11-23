package org.metadatacenter.schemaorg.pipeline.mapping.model;

import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import static com.google.common.base.Preconditions.checkNotNull;

public class PairNode extends MapNode {

  private final String value1;
  private final String value2;

  public PairNode(@Nonnull String value1, @Nonnull String value2) {
    this.value1 = checkNotNull(value1);
    this.value2 = checkNotNull(value2);
  }

  @Override
  public boolean isPairNode() {
    return true;
  }

  public String getFirstValue() {
    return value1;
  }

  public String getSecondValue() {
    return value2;
  }

  @Override
  public String getValue() {
    return String.format("(%s,%s)", value1, value2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value1, value2);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PairNode)) {
      return false;
    }
    PairNode other = (PairNode) obj;
    return Objects.equals(value1, other.value1)
        && Objects.equals(value2, other.value2);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("value1", value1)
        .add("value2", value2)
        .toString();
  }
}
