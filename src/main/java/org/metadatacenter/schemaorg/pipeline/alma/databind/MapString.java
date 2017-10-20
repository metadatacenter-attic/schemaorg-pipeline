package org.metadatacenter.schemaorg.pipeline.alma.databind;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Objects;
import javax.annotation.Nonnull;

public class MapString {

  private final String text;

  // Prevent external instantiation
  private MapString(@Nonnull String text) {
    this.text = checkNotNull(text);
  }

  public static MapString read(String text) {
    return new MapString(text);
  }

  public static MapString map(String key, String value) {
    return new MapString(String.format("%s: %s", key, value));
  }

  public String key() {
    return text.split(":", 2)[0].trim();
  }

  public String value() {
    return text.split(":", 2)[1].trim();
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof MapString)) {
      return false;
    }
    MapString other = (MapString) obj;
    return Objects.equals(text, other.text);
  }

  @Override
  public String toString() {
    return text;
  }
}
