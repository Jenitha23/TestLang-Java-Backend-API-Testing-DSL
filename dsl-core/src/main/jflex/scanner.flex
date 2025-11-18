package com.testlang.lexer;

import java_cup.runtime.*;
import com.testlang.parser.sym;

%%

%class Lexer
%unicode
%cup
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }

  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

Comment = "//" [^\r\n]*

Identifier = [A-Za-z_][A-Za-z0-9_]*
Number = [0-9]+
String = \"([^\"\\]|\\.)*\"

%%

<YYINITIAL> {
  "config"                 { return symbol(sym.CONFIG); }
  "base_url"               { return symbol(sym.BASE_URL); }
  "header"                 { return symbol(sym.HEADER); }
  "let"                    { return symbol(sym.LET); }
  "test"                   { return symbol(sym.TEST); }
  "GET"                    { return symbol(sym.GET); }
  "POST"                   { return symbol(sym.POST); }
  "PUT"                    { return symbol(sym.PUT); }
  "DELETE"                 { return symbol(sym.DELETE); }
  "expect"                 { return symbol(sym.EXPECT); }
  "status"                 { return symbol(sym.STATUS); }
  "body"                   { return symbol(sym.BODY); }
  "contains"               { return symbol(sym.CONTAINS); }

  "{"                      { return symbol(sym.LBRACE); }
  "}"                      { return symbol(sym.RBRACE); }
  "="                      { return symbol(sym.EQUALS); }
  ";"                      { return symbol(sym.SEMICOLON); }

  {Identifier}             { return symbol(sym.IDENTIFIER, yytext()); }
  {Number}                 { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }
  {String}                 {
                              String str = yytext().substring(1, yytext().length() - 1);
                              str = str.replace("\\\"", "\"").replace("\\\\", "\\");
                              return symbol(sym.STRING, str);
                            }

  {Comment}                { /* ignore */ }
  {WhiteSpace}             { /* ignore */ }

  [^]                      { throw new Error("Illegal character <" + yytext() + "> at line " + (yyline+1) + ", column " + (yycolumn+1)); }
}