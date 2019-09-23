grammar Gherkin;

feature     : 'Feature:' sentence NEWLINE (scenario | comment)+;
scenario    : 'Scenario:' sentence NEWLINE (step | comment)+;
step        :  ('Given' | 'When' | 'Then' | 'And')+ ':'? sentence NEWLINE;
comment     : '#' sentence NEWLINE;
sentence       : (variable | WORD)+ ;
variable    : VARIABLE;

WORD       : [a-zA-Z0-9"']+ ;
VARIABLE   : '<'[a-zA-Z0-9]+'>' ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;
WHITESPACE : (' ' | '\t')+ -> skip;