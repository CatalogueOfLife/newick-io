grammar Newick;

options {
    language=Java;
}

// Grammer based on https://github.com/antlr/grammars-v4/tree/master/newick
//
// See also:
// https://en.wikipedia.org/wiki/Newick_format#Grammar
// http://scikit-bio.org/docs/0.2.3/generated/skbio.io.newick.html
//
// for unicode codepoint classifications see https://github.com/antlr/grammars-v4/tree/master/unicode

// (B,(A,C,E)G,D)F;

tree: (leaf | internal) ';';
leaf: name (comment)?;
internal: '(' (branch (',' branch)*) ')' (name)? (':' NUMBER)? (comment)?;
branch: leaf | internal;
name: STRING;
comment: ('[' STRING ']');

STRING : [a-zA-Z_&=.?+#öüäÖÜÄ$/!"-]+ ;
NUMBER: [0-9]+ ('.' [0-9]+)?;
ESC_QUOTE: '\'\'';
WS: [ \t\n\r]+ -> skip ;
//NONRESERVED: [^()[\],:;'];
//STRING: '\'' (NONRESERVED | ESC_QUOTE)* '\'' | NONRESERVED*;
