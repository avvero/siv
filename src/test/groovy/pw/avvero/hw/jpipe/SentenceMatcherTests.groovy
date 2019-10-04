package pw.avvero.hw.jpipe


import pw.avvero.hw.jpipe.walker.SentenceMatcher
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
        pattern       | string        | matches
        "foo <v>"     | "foo bar"     | true
        "foo <v> foo" | "foo bar foo" | true
        "foo"         | "foo"         | true
        "foo"         | "foo"         | true
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
