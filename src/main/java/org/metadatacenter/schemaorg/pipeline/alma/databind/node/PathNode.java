package org.metadatacenter.schemaorg.pipeline.alma.databind.node;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;

public class PathNode extends MapNode {

  private final String path;
  
  public PathNode(@Nonnull String path) {
    this.path = checkNotNull(path);
  }

  @Override
  public boolean isPathNode() {
    return true;
  }

  @Override
  public String getValue() {
    return path;
  }

  @Override
  public int hashCode() {
    return Objects.hash(path);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PathNode)) {
      return false;
    }
    PathNode other = (PathNode) obj;
    return Objects.equals(path, other.path);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("path", path)
        .toString();
  }
}
