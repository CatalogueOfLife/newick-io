package org.catalogueoflife.newick;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

public class SimpleNode extends Node<SimpleNode> {
  private String comment;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  protected void writeComments(Writer w) throws IOException {
    if (comment != null) {
      w.append("[");
      w.append(escape(comment));
      w.append("]");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SimpleNode)) return false;
    if (!super.equals(o)) return false;
    SimpleNode that = (SimpleNode) o;
    return Objects.equals(comment, that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), comment);
  }
}
