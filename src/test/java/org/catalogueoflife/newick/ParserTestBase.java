package org.catalogueoflife.newick;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.stream.Collectors;

public abstract class ParserTestBase {

  abstract ParserBase<?> newParser(InputStream in);

  @Test
  public void testTreeWithoutComments() throws IOException {
    for (int i=1; i<=8; i++) {
      System.out.println("\n *** TREE "+i+" ***");
      String file = "/trees/tree"+i+".txt";
      final String expected = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)))
          .lines().collect(Collectors.joining("\n"));;
      //System.out.println(expected);

      ParserBase<?> p = newParser(getClass().getResourceAsStream(file));
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