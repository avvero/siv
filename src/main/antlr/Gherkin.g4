grammar Gherkin;

feature     : leadSpace? 'Feature:' leadSpace? sentence NEWLINE (scenario | emptyLine)+;
scenario    : leadSpace? 'Scenario:' leadSpace? sentence NEWLINE (step | emptyLine)+;
step        : leadSpace? (given | when | then | and) NEWLINE?;
given       : 'Given:' leadSpace? sentence;
when        : 'When:' leadSpace? sentence;
then        : 'Then:' leadSpace? sentence;
and         : 'And:' leadSpace? sentence;
emptyLine   : space + NEWLINE?;

sentence    : (variable | WORD | sign | regExp | space)+ ;
variable    : '<' variableName '>';
regExp      : '/' regExpValue '/';
variableName: WORD;
sign        : SIGN;
regExpValue : REGEX;
space       : SPACE;
leadSpace   : SPACE;

WORD       : [a-zA-Z0-9'-]+ ;
SIGN       : [.,:-;(){}_*#^!~%&"?\\+] ;
REGEX      : [a-zA-Z\\+?[\]]+ ;
SPACE      : (' ' | '\t')+ ;
NEWLINE    : ('\n' | '\r' | '\n\r' | '\r\n')+ ;