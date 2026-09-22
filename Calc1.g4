grammar Calc1;

expr  	: expr MUL expr
	 	| expr DIV expr
	 	| expr PLUS expr
	 	| expr MINUS expr
	 	| factor
        ;

factor  : INT
        | ID
        ;

INT : [0-9]+ ;
PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
ID : [a-zA-Z]+ ;
WS  : [ \t\r\n]+ -> skip ;
