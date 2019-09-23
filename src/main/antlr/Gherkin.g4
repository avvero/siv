grammar Gherkin;

feature     : 'Feature:' phrase NEWLINE (scenario | comment)+;
scenario    : 'Scenario:' phrase NEWLINE (step | comment)+;
step        :  ('Given' | 'When' | 'Then' | 'And')+ ':'? phrase NEWLINE;
comment     : '#' phrase NEWLINE;
phrase       : (variable | WORD)+ ;
variable    : VARIABLE;

WORD       : [a-zA-Z0-9"']+ ;
VARIABLE   : '<'[a-zA-Z0-9]+'>' ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;
WHITESPACE : (' ' | '\t')+ -> skip;