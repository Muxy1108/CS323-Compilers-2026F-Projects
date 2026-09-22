grammar IPV4;

expr  	:  
		expr DOT expr
	 	| factor
        ;

factor  : Number
        ;

Number : 
		[01][0-9][0-9] 
		| [2][0-4][0-9]
		| [2][5][0-5]
		;

ERROR : [0-9]+ ;

DOT : '.';