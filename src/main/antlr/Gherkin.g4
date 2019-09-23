grammar Gherkin;

feature     : 'Feature:' fraze NEWLINE (scenario | comment)+;
scenario    : 'Scenario:' fraze NEWLINE (step | comment)+;
step        :  ('Given' | 'When' | 'Then' | 'And')+ ':'? fraze NEWLINE;
comment     : '#' fraze NEWLINE;
fraze       : WORD+ ;

WORD       : [a-zA-Z0-9"']+ ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;
WHITESPACE : (' ' | '\t')+ -> skip;