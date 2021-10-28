package org.catalogueoflife.newick;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.stream.Collectors;

public class SimpleParserTest extends ParserTestBase{

  @Test
  public void testParse() throws IOException {
    for (int i=1; i<=2; i++) {
      System.out.println("\n *** SIMPLE TREE "+i+" ***");
      String file = "/trees/simple"+i+".txt";
      final String expected = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)))
          .lines().collect(Collectors.joining("\n"));;
      //System.out.println(expected);

      SimpleParser p = new SimpleParser(getClass().getResourceAsStream(file));
      var res = p.parse();
      StringWriter writer = new StringWriter();
      res.printTree(writer);
      //System.out.println(writer);

      Assert.assertEquals(norm(expected), norm(writer.toString()));
      System.out.println("OK");
    }
  }

  @Override
  ParserBase<?> newParser(InputStream in) {
    return new SimpleParser(in);
  }

  String norm(String x) {
    return x.replaceAll("\\s+", "");
  }
}