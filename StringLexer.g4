lexer grammar StringLexer;

channels {
    WHITESPACE,
    COMMENTS
}

fragment ESCAPED_QUOTE
    : '\\' '"'
    ;


NUMBER: [0-9]+;
STRING
    : '"' (ESCAPED_QUOTE | ~["])* '"'
    ;

IF: 'if';
MAIN: 'main';
INT: 'int';
EQUAL: '=';
PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
STRING: 'String';
LEFTBRACET: '(';
RIGHTBRACET: ')';
LEFTBRACE: '{';
RIGHTBRACE: '}';
SEMICOLON: ';';
GTR: '>';




IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]* ;




WS  : [ \t\r\n]+     -> channel(WHITESPACE);
SL_COMMENT : '//' .*? '\n' -> channel(COMMENTS);
