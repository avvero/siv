package pw.avvero.hw.siv


import pw.avvero.hw.siv.walker.SentenceMatcher
import spock.lang.Specification
import spock.lang.Unroll

class SentenceMatcherTests extends Specification {

    def getSentence(String sentenceString) {
        def feature = new FeatureParser().parseFromString("Feature: f\nScenario: s\nWhen: " + sentenceString)
        return feature.scenarios.first().steps.first().sentence
    }

    @Unroll
    def "String `#string` matches(#matches) `#pattern`"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = getSentence(pattern)
        then:
        matcher.match(sentence, string).matches == matches
        where:
        pattern                                  | string                                 | matches
//        "foo <v>"                                | "foo bar"                              | true
        "foo <v>"                                | "foo  bar"                             | true
        "foo <v>"                                | "foo   bar"                            | true
        "foo <v> foo"                            | "foo bar foo"                          | true
        "foo <v> foo"                            | "foo  bar  foo"                        | true
        "foo <v> foo"                            | "foobarfoo"                            | false
        "foo <v> foo"                            | "foo bar foo"                          | true
        "foo <v> foo"                            | "foo bar foo too moo"                  | true
        "foo \\w+ <v> \\w+ foo"                  | "foo ANY bar ANY foo"                  | false
        "foo"                                    | "foo"                                  | true
        "foo"                                    | "foo"                                  | true
        "a.b:c{d}(n) <id>"                       | "a.b:c{d}(n) 12"                       | true
        "a.b:c{d}(n)?\\s#^!~%^&*( <id>"          | "a.b:c{d}(n)?\\s#^!~%^&*( 11"          | true
        "a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q <id>" | "a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q 12" | true
        "a:b <id>"                               | "a:b 12"                               | true
        "a#b <id>"                               | "a#b 12"                               | true
        "a.bc{d}(n)?s#^!~%^&*( <id>"             | "a.bc{d}(n)?s#^!~%^&*( 10"             | true
        "a.bc{d}(n)?s#^!~%^&*( <id>"             | "a.bc{d}(n)?s#^!~%^&*( 10"             | true
    }

    @Unroll
    def "String `#string` matches(#matches) itself"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = getSentence(string)
        then:
        matcher.match(sentence, string).matches
        where:
        string                      | _
        "a.bc"                      | _
        "a.bc{d}"                   | _
        "a.bc{d}(n)"                | _
        "a.bc{d}(n)?"               | _
        "a.bc{d}(n)?s#"             | _
        "a.bc{d}(n)?s#^!"           | _
        "a.bc{d}(n)?s#^!~%"         | _
        "a.bc{d}(n)?s#^!~%^&"       | _
        "a.bc{d}(n)?s#^!~%^&*"      | _
        "a.bc{d}(n)?s#^!~%^&*("     | _
        "a.bc{d}(n)?s#^!~%^&*( "    | _
        "a.b:c{d}(n)?s#^!~%^&*( "   | _
        "a.b:c{d}(n)?\\s#^!~%^&*( " | _
    }

    @Unroll
    def "Complicated string `#string` matches(#matches) `#pattern`"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = getSentence(pattern)
        then:
        matcher.match(sentence, string).matches
        where:
        string                                              | pattern
        "(,91508063b)  qtp209-487  params (repository.add)" | "(,91508063b)  qtp209-487  params (repository.add)"
        "(,91508063b)  qtp209-487  params (repository.add)" | "(,<sessionId>)  qtp209-487  params (repository.add)"
        "(,91508063b)  qtp209-487  params (repository.add)" | "(,91508063b)  <thread>  params (repository.add)"
        "(,91508063b)  qtp209-487  params (repository.add)" | "(,<sessionId>)  <thread>  params (repository.add)"
    }

    @Unroll
    def "Very complicated string `#string` matches(#matches) `#pattern`"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = getSentence(pattern)
        then:
        matcher.match(sentence, string).matches
        where:
        string                                                                                            | pattern
        "TRACE (,25412,) qtp126 j.pa:log:1164 - ******* PARAMS (repository.frontend_clients.add) *******" | "TRACE (,25412,) qtp126 j.pa:log:1164 - ******* PARAMS (repository.frontend_clients.add) *******"
        "TRACE (,25412,) qtp126 j.pa:log:1164 - ******* PARAMS (repository.frontend_clients.add) *******" | "TRACE (,<session>,) <thread> j.pa:log:1164 - ******* PARAMS (repository.frontend_clients.add) *******"
    }

    @Unroll
    def "String `#pattern` matches(#matches) `#string` with variables"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = getSentence(pattern)
        then:
        matcher.match(sentence, string, context as Map).matches == matches
        matcher.match(sentence, string, context as Map).attributes == attributes
        where:
        pattern              | string            | context       | matches | attributes
        "id"                 | "id"              | [:]           | true    | [:]
        "id"                 | "foo"             | [:]           | false   | [:]
        "id <id>"            | "id 100"          | ["id": "100"] | true    | [:]
        "id <id>"            | "id 100"          | [:]           | true    | ["id": "100"]
        "id <id>"            | "id 100"          | ["id": "200"] | false   | [:]
        "id <id>, key <key>" | "id 100, key foo" | ["id": "100"] | true    | ["key": "foo"]
        "id <id>, key <key>" | "id 100, key foo" | ["id": "100", "key": "foo"] | true    | [:]
        "id <id>, key <key>" | "id 100, key foo" | ["id": "100", "key": "bar"] | false   | [:]
    }
}
