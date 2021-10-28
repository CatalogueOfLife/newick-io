package org.catalogueoflife.newick;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

public class NHXNodeTest {

  @Test
  public void testGetValue() {
    NHXNode n = new NHXNode();
    for (String k : n.listKeys()) {
      System.out.print(k + " --> ");
      System.out.println(n.getValue(k));
    }
  }

  @Test
  public void print() throws IOException {
    NHXNode root = new NHXNode("Abies alba");
    SimpleNodeTest.assertTree("Abies_alba", root);

    root.addChild(new NHXNode("C1"));
    root.addChild(new NHXNode("C2'1"));
    var n = new NHXNode("Carla");
    n.setSpeciesName("Carla magna");
    root.addChild(n);

    SimpleNodeTest.assertTree("(C1,'C2''1',Carla[&&NHX:S=Carla_magna])Abies_alba", root);
  }

}