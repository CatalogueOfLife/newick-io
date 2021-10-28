package org.catalogueoflife.newick;

import junit.framework.TestCase;

public class NHXNodeTest extends TestCase {

  public void testGetValue() {
    NHXNode n = new NHXNode();
    for (String k : n.listKeys()) {
      System.out.print(k + " --> ");
      System.out.println(n.getValue(k));
    }
  }

}