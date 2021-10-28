package org.catalogueoflife.newick;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.stream.Collectors;

public class NodeFactoryTest {

  @Test
  public void testParse() throws IOException {
    for (int i=1; i<=7; i++) {
      System.out.println("\n *** TREE "+i+" ***");
      String file = "/trees/tree"+i+".txt";
      final String expected = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)))
          .lines().collect(Collectors.joining("\n"));;
      System.out.println(expected);

      var res = NodeFactory.parse(getClass().getResourceAsStream(file));
      StringWriter writer = new StringWriter();
      res.printTree(writer);
      System.out.println(writer);

      Assert.assertEquals(expected, writer.toString());
    }
  }
}