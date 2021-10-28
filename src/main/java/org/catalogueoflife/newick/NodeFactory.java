package org.catalogueoflife.newick;

import org.antlr.v4.runtime.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class NodeFactory {

  /**
   * Uses an UTF8 input stream
   */
  public static SimpleNode parse(InputStream stream) throws IOException {
    return parse(new InputStreamReader(stream, StandardCharsets.UTF_8));
  }

  public static SimpleNode parse(Reader reader) throws IOException {
    NewickLexer l = new NewickLexer(CharStreams.fromReader(reader));
    NewickParser p = new NewickParser(new CommonTokenStream(l));
    p.addErrorListener(new BaseErrorListener() {
      @Override
      public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
      }
    });

    SimpleTreeVisitor visitor = new SimpleTreeVisitor();
    SimpleNode res = visitor.visit(p.tree());
    return res;
  }

  static class SimpleTreeVisitor extends NewickBaseVisitor<SimpleNode> {
    @Override
    public SimpleNode visitTree(NewickParser.TreeContext ctx) {
      if (ctx.leaf() != null) {
        return visitLeaf(ctx.leaf());
      }
      return visitInternal(ctx.internal());
    }

    @Override
    public SimpleNode visitLeaf(NewickParser.LeafContext ctx) {
      SimpleNode n = new SimpleNode();
      n.setLabel(ctx.name().getText());
      if (ctx.comment() != null && ctx.comment().getText().length()>0) {
        n.setComment(ctx.comment().getText());
      }
      return n;
    }

    @Override
    public SimpleNode visitInternal(NewickParser.InternalContext ctx) {
      SimpleNode n = new SimpleNode();
      if (ctx.name() != null) {
        n.setLabel(ctx.name().getText());
      }
      if (ctx.comment() != null && ctx.comment().getText().length()>0) {
        n.setComment(ctx.comment().getText());
      }
      if (ctx.NUMBER() != null) {
        n.setLength(Float.parseFloat(ctx.NUMBER().getText()));
      }
      List<SimpleNode> children = ctx.branch()
                                    .stream()
                                    .map(b -> b.accept(this))
                                    .collect(Collectors.toList());
      n.setChildren(children);
      return n;
    }
  }

}
