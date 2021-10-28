package org.catalogueoflife.newick;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class SimpleNodeTest {

  @Test
  public void print() throws IOException {
    SimpleNode root = new SimpleNode("Abies alba");
    assertTree("Abies_alba", root);

    root.addChild(new SimpleNode("C1"));
    root.addChild(new SimpleNode("C2'1"));

    assertTree("(C1,'C2''1')Abies_alba", root);
  }

  public static void assertTree(String expected, Node<?> root) throws IOException {
    StringWriter writer = new StringWriter();
    root.printTree(writer);
    String tree = writer.toString();
    assertEquals(expected+";", tree);
  }
}