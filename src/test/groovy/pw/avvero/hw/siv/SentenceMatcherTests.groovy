package pw.avvero.hw.siv


import pw.avvero.hw.siv.walker.SentenceMatcher
import spock.lang.Ignore
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
        "foo <v>"                                | "foo bar"                              | true
        "foo <v>"                                | "foo  bar"                             | true
        "foo <v> foo"                            | "foo bar foo"                          | true
        "foo <v> foo"                            | "foo  bar  foo"                        | true
        "foo <v> foo"                            | "foobarfoo"                            | false
        "foo <v> foo"                            | "foo bar foo"                          | true
        "foo <v> foo"                            | "foo bar foo too moo"                  | true
        "foo"                                    | "foo"                                  | true
        "foo"                                    | "foo"                                  | true
        "a.b:c{d}(n) <id>"                       | "a.b:c{d}(n) 12"                       | true
        "a.b:c{d}(n)?\\s#^!~%^&*( <id>"          | "a.b:c{d}(n)?\\s#^!~%^&*( 11"          | true
        "a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q <id>" | "a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q 12" | true
        "a:b <id>"                               | "a:b 12"                               | true
        "a#b <id>"                               | "a#b 12"                               | true
        "a.bc{d}(n)?s#^!~%^&*( <id>"             | "a.bc{d}(n)?s#^!~%^&*( 10"             | true
        "a.bc{d}(n)?s#^!~%^&*( <id>"             | "a.bc{d}(n)?s#^!~%^&*( 10"             | true
        "/\\w+/"                                 | "foo"                                  | true
        "/\\w+/"                                 | "foo bar tar"                          | true
        "foo /\\w+/"                             | "foo bar tar"                          | true
        "foo /\\w+?/ tar"                        | "foo bar tar"                          | true
        "foo /\\w+?/ TAR"                        | "foo bar bar"                          | false
        "BAR /\\w+?/ tar"                        | "foo bar tar"                          | false
        "bar /\\w+/"                             | "foo bar tar"                          | true
        "foo /.+?/ tar"                          | "foo **** tar"                         | true
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
        string                                                        | pattern
        "TR (,25,) qt6 j.pa:log:1164 - **** PARAMS (rep.fr.add) ****" | "TR (,25,) qt6 j.pa:log:1164 - **** PARAMS (rep.fr.add) ****"
        "TR (,25,) qt6 j.pa:log:1164 - **** PARAMS (rep.fr.add) ****" | "TR (,<session>,) <thread> j.pa:log:1164 - **** PARAMS (rep.fr.add) ****"
    }

    @Ignore
    @Unroll
    def "String with regex `#string` matches(#matches) `#pattern`"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = getSentence(pattern)
        then:
        matcher.match(sentence, string).matches
        where:
        string                                                        | pattern
        "TR (,25,) qt6 j.pa:log:1164 - **** PARAMS (rep.fr.add) ****" | "TR (,<session>,) <thread> j.pa:log:1164 - /[\\w\\W]+?/ rep.fr.add"
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
        pattern                             | string                  | context                     | matches | attributes
        "id"                                | "id"                    | [:]                         | true    | [:]
        "id"                                | "foo"                   | [:]                         | false   | [:]
        "id <id>"                           | "id 100"                | ["id": "100"]               | true    | [:]
        "id <id>"                           | "id 100"                | [:]                         | true    | ["id": "100"]
        "id <id>"                           | "id 100"                | ["id": "200"]               | false   | [:]
        "id <id>, key <key>"                | "id 100, key foo"       | ["id": "100"]               | true    | ["key": "foo"]
        "id <id>, key <key>"                | "id 100, key foo"       | ["id": "100", "key": "foo"] | true    | [:]
        "id <id>, key <key>"                | "id 100, key foo"       | ["id": "100", "key": "bar"] | false   | [:]
        "foo /\\w+?/ tar"                   | "foo bar tar"           | [:]                         | true    | [:]
        "foo /[\\w\\s]+?/ tar"              | "foo bar bar tar"       | [:]                         | true    | [:]
        "foo /[\\w\\s]+?/ tar"              | "foo bar bar tar"       | [:]                         | true    | [:]
        "foo /[\\w\\s]+?/ tar <key>"        | "foo bar bar tar 12"    | [:]                         | true    | ["key": "12"]
        "foo <key> /[\\w\\s]+?/ tar"        | "foo 12 bar bar tar"    | [:]                         | true    | ["key": "12"]
        "foo <key> /[\\w\\s]+?/ tar"        | "foo 12 bar V23 tar"    | [:]                         | true    | ["key": "12"]
        "foo <key> /[\\w\\W]+?/ tar"        | "foo 12 bar *** tar"    | [:]                         | true    | ["key": "12"]
        "foo <key> /.+?/ tar"               | "foo 12 bar *** tar"    | [:]                         | true    | ["key": "12"]
        "foo <key> /[\\w\\s]+?/ tar"        | "foo 12 bar bar tar"    | ["key": "12"]               | true    | [:]
        "foo <key> /[\\w\\s]+?/ tar"        | "foo 12 bar bar tar"    | ["key": "13"]               | false   | [:]
        "foo <key> /[\\w\\s]+?/ tar <key2>" | "foo 12 bar bar tar 13" | ["key": "12"]               | true    | ["key2": "13"]
    }

    @Unroll
    def "String #string contains attributes #attributes"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = getSentence(pattern)
        then:
        matcher.match(sentence, string, [:] as Map).matches
        matcher.match(sentence, string, [:] as Map).attributes == attributes
        where:
        pattern                             | string                  | attributes
        "id"                                | "id"                    | [:]
        "id <id>"                           | "id 100"                | ["id": "100"]
        "id <id>, key <key>"                | "id 100, key foo"       | ["id": "100", "key": "foo"]
        "foo <key> /[\\w\\s]+?/ tar <key2>" | "foo 12 bar bar tar 13" | ["key2": "13", "key": "12"]
        "id <id>"                           | "id 100.12"             | ["id": "100.12"]
    }
}
