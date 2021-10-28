package org.catalogueoflife.newick;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class NHXParserTest {

  @Test
  public void testComments() throws IOException {
    NHXNode n = new NHXNode();
    NHXParser.parseComments("&&NHX:S=human:E=1.1.1.1", n);
    assertEquals("human", n.getSpeciesName());
    assertEquals("1.1.1.1", n.getEcNumber());
  }

  @Test
  public void testNHX() throws IOException {
    for (int i=1; i<=1; i++) {
      System.out.println("\n *** NHX "+i+" ***");
      String file = "/trees/nhx"+i+".txt";
      final String expected = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)))
          .lines().collect(Collectors.joining("\n"));;
      //System.out.println(expected);

      NHXParser p = new NHXParser(getClass().getResourceAsStream(file));
      var res = p.parse();
      StringWriter writer = new StringWriter();
      res.printTree(writer);
      //System.out.println(writer);

      Assert.assertEquals(norm(expected), norm(writer.toString()));
      System.out.println("OK");
    }
  }

  String norm(String x) {
    return x.replaceAll("\\s+", "");
  }
}