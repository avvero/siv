package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.gherkin.Sentence
import pw.avvero.hw.jpipe.walker.SentenceMatcher
import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Pattern

class SentenceMatcherTests extends Specification {

    @Unroll
    def "String #string matches(#matches) #pattern"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = new Sentence()
        sentence.setPattern(Pattern.compile(pattern))
        then:
        matcher.matches(sentence, string) == matches
        where:
        pattern        | string        | matches
        "foo \\w+"     | "foo bar"     | true
        "foo \\w+ foo" | "foo bar foo" | true
    }

}
