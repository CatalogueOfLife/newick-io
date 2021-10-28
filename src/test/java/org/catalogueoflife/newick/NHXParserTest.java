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
  public void testParse() throws IOException {
    for (int i=1; i<=8; i++) {
      System.out.println("\n *** TREE "+i+" ***");
      String file = "/trees/tree"+i+".txt";
      final String expected = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file)))
          .lines().collect(Collectors.joining("\n"));;
      //System.out.println(expected);

      NHXParser p = new NHXParser(getClass().getResourceAsStream(file));
      var res = p.parse();
      StringWriter writer = new StringWriter();
      res.printTree(writer);
      //System.out.println(writer);

      Assert.assertEquals(norm(expected), norm(writer.toString()));
    }
  }

  String norm(String x) {
    return x.replaceAll("\\s+", "").replaceAll(",", ",\n");
  }
}