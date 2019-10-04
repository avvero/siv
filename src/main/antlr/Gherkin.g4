grammar Gherkin;

feature     : leadSpace? 'Feature:' leadSpace? sentence NEWLINE (scenario | comment)+;
scenario    : leadSpace? 'Scenario:' leadSpace? sentence NEWLINE (step | comment)+;
step        : leadSpace? (given | when | then | and);
given       : 'Given' ':'? leadSpace? sentence NEWLINE?;
when        : 'When' ':'? leadSpace? sentence NEWLINE?;
then        : 'Then' ':'? leadSpace? sentence NEWLINE?;
and         : 'And' ':'? leadSpace? sentence NEWLINE?;
comment     : leadSpace? '#' sentence NEWLINE;
sentence    : (variable | WORD | sign | space)+ ;
variable    : '<' variableName '>';
variableName: WORD;
sign        : SIGN;
space       : SPACE;
leadSpace   : SPACE;

WORD       : [a-zA-Z0-9"'-]+ ;
SIGN       : [\\.,:-;(){}_] ;
SPACE      : (' ' | '\t')+ ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;