grammar Gherkin;

feature     : 'Feature:' sentence NEWLINE (scenario | comment)+;
scenario    : 'Scenario:' sentence NEWLINE (step | comment)+;
step        :  (given | when | then | and);
given       : 'Given' ':'? sentence NEWLINE;
when        : 'When' ':'? sentence NEWLINE;
then        : 'Then' ':'? sentence NEWLINE;
and         : 'And' ':'? sentence NEWLINE;
comment     : '#' sentence NEWLINE;
sentence    : (variable | WORD)+ ;
variable    : VARIABLE;

WORD       : [a-zA-Z0-9"']+ ;
VARIABLE   : '<'[a-zA-Z0-9]+'>' ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;
WHITESPACE : (' ' | '\t')+ -> skip;