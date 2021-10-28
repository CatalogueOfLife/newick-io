package org.catalogueoflife.newick;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class NHXParser {
  private static final char QUOTE = '\'';
  final private BufferedReader r;
  final private StringBuilder text = new StringBuilder();
  final private LinkedList<SimpleNode> parents = new LinkedList<>();
  private SimpleNode curr;
  private boolean inQuote;
  private boolean inLength;
  private boolean stop;
  private int position = 0;
  private char last;
  private char c = '^';

  public NHXParser(InputStream stream) {
    this(new InputStreamReader(stream, StandardCharsets.UTF_8));
  }

  public NHXParser(Reader reader) {
    this.r = new BufferedReader(reader);
  }

  // (B,(A,C,E)G,D)F;
  public SimpleNode parse() throws IOException, IllegalArgumentException {
    try {
      int intch;
      curr = new SimpleNode(); // root node
      while (!stop && (intch = r.read()) != -1) {
        position++;
        last = c;
        c = (char) intch;

        // single quote
        if (c == QUOTE) {
          if (inQuote) {
            // escaped quote ?
            if (last == QUOTE) {
              // two subsequent quotes are the escape sequence for a single quote
              text.append(QUOTE);
              c=' '; // reset to avoid another pair with next quote
            }
            continue;
          } else if (text.length()==0) {
            inQuote = true; // start new quoting
            c = ' '; // hide that we just had a quote
            continue;
          }
        } else if (inQuote) {
          if (last == QUOTE) {
            // the last quote was an ending quote
            inQuote = false;
          } else {
            appendText();
            continue;
          }
        }

        // whitespace
        if (Character.isWhitespace(c)) {
          continue;
        }

        // we deal with all reserved chars but quote & whitespace here
        switch (c) {
          case '(':
            parents.add(curr);
            newCurr();
            break;
          case ')':
            labelCurr();
            curr = parents.removeLast();
            break;
          case ',':
            labelCurr();
            newCurr();
            break;
          case ':':
            labelCurr();
            inLength = true;
            break;
          case '[':
            break;
          case ']':
            break;
          case ';':
            // we stop parsing at the end of tree marker, even if there is more text which we will ignore
            stop = true;
            labelCurr();
            break;
          default:
            appendText();
        }
      }
    } catch (IllegalArgumentException e){
      throw e;
    } catch (RuntimeException e){
      throw new IllegalArgumentException("Failed to parse on position "+position, e);
    } finally {
      r.close();
    }

    // make sure we have an empty stack - otherwise the file was invalid!
    if (!parents.isEmpty()) {
      throw new IllegalArgumentException("Missing closing tag (");
    }
    return curr;
  }

  public void appendText() {
    if (last == QUOTE && text.length() > 0) {
      throw new IllegalArgumentException("Quoted string not closed properly at position " + position);
    }
    text.append(c);
  }

  public void newCurr() {
    curr = new SimpleNode();
    parents.getLast().addChild(curr);
  }

  private void labelCurr() {
    if (text.length() > 0) {
      if (inLength) {
        Double len = Double.parseDouble(text.toString());
        curr.setLength(len);
      } else {
        curr.setLabel(text.toString());
        last=' '; //TODO: needed?
      }
      text.setLength(0);
    }
    inLength = false;
  }
}
