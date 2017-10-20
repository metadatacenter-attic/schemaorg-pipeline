package org.metadatacenter.schemaorg.pipeline.alma.databind;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

public class Section {
  private final String text;
  private final int depth;
  private final List<Section> children;
  private final Section parent;

  public Section(@Nonnull String text, int depth) {
    this(text, depth, null);
  }

  public Section(@Nonnull String text, int depth, @Nullable Section parent) {
    this.text = checkNotNull(text);
    this.depth = depth;
    this.parent = parent;
    this.children = Lists.newArrayList();
  }

  public static Section createRootSection() {
    return new Section("", -1);
  }

  public String getText() {
    return text;
  }

  public int getDepth() {
    return depth;
  }

  public List<Section> getChildren() {
    return children;
  }

  public boolean hasChildren() {
    return !children.isEmpty();
  }

  public Section getParent() {
    return parent;
  }

  public void accept(SectionVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, depth, children, parent);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Section)) {
      return false;
    }
    Section other = (Section) obj;
    return Objects.equals(this.text, other.text) && Objects.equals(this.depth, other.depth)
        && Objects.equals(this.children, other.children)
        && Objects.equals(this.parent, other.parent);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("text", text).add("depth", depth)
        .add("children", children).toString();
  }
}
