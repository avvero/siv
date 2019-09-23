grammar Gherkin;

file        : (feature | scenario)+ EOF;
feature     : 'Feature:' fraze NEWLINE scenario+;
scenario    : 'Scenario:' fraze NEWLINE step+;
step        :  ('When:' | 'Then:' | 'And:')+ fraze NEWLINE;
comment     : '#' fraze NEWLINE;
fraze       : WORD+ ;

WORD       : [a-zA-Z0-9]+ ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;
WHITESPACE : (' ' | '\t')+ -> skip;