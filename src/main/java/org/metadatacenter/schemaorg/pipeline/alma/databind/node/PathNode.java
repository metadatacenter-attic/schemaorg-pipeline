package org.metadatacenter.schemaorg.pipeline.alma.databind.node;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Objects;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;

public class PathNode extends MapNode {

  private final String parent;
  private final String path;

  public PathNode(@Nonnull String parent, @Nonnull String path) {
    this.parent = checkNotNull(parent);
    this.path = checkNotNull(path);
  }

  public PathNode(@Nonnull String path) {
    this("", path);
  }

  public String getRelativePath() {
    return path;
  }

  public String getAbsolutePath() {
    return parent + path;
  }

  @Override
  public boolean isPathNode() {
    return true;
  }

  @Override
  public String getValue() {
    return getRelativePath();
  }

  @Override
  public int hashCode() {
    return Objects.hash(parent, path);
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
    return Objects.equals(parent, other.parent) && Objects.equals(path, other.path);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("parent", parent)
        .add("path", path)
        .toString();
  }
}
