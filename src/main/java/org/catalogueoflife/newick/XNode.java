package org.catalogueoflife.newick;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public abstract class XNode<T extends XNode<T>> extends Node<T> {

  abstract public String getValue(String key);

  abstract public void setValue(String key, String value);

  @Override
  protected void writeComments(Writer w) throws IOException {
    boolean started = false;
    for (String key : listKeys()) {
      String val = getValue(key);
      if (val != null) {
        if (!started) {
          w.append("[&&NHX");
          started=true;
        }
        w.append(":");
        w.append(key.toUpperCase().trim());
        w.append("=");
        w.append(escape(val));
      }
    }
    if (started) {
      w.append("]");
    }
  }

  abstract protected List<String> listKeys();
}
