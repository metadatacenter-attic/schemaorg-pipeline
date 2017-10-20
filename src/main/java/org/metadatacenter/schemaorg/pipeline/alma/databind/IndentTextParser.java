package org.metadatacenter.schemaorg.pipeline.alma.databind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.annotation.Nonnull;

public class IndentTextParser {

  public Section parse(@Nonnull String text) {
    return parse(new StringReader(text));
  }

  public Section parse(@Nonnull Reader reader) {
    final Section root = Section.createRootSection();
    try (BufferedReader buffer = new BufferedReader(reader)) {
      Section prev = root;
      String line = buffer.readLine();
      while (line != null) {
        int depth = countIndentWhitespaces(line);
        if (depth > prev.getDepth()) {
          Section section = new Section(line.trim(), depth, prev);
          prev.getChildren().add(section);
          prev = section;
        } else if (depth == prev.getDepth()) {
          Section section = new Section(line.trim(), depth, prev.getParent());
          prev.getParent().getChildren().add(section);
          prev = section;
        } else {
          while (depth < prev.getDepth()) {
            prev = prev.getParent();
          }
          Section section = new Section(line.trim(), depth, prev.getParent());
          prev.getParent().getChildren().add(section);
          prev = section;
        }
        line = buffer.readLine();
      }
      return root;
    } catch (IOException e) {
      // TODO: Do something about try-catch IOException
      return Section.createRootSection();
    }
  }

  private int countIndentWhitespaces(String str) {
    return str.indexOf(str.trim());
  }
}
