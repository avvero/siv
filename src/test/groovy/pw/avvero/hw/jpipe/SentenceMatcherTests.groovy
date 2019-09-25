package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.gherkin.Sentence
import pw.avvero.hw.jpipe.walker.SentenceMatcher
import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Pattern

class SentenceMatcherTests extends Specification {

    @Unroll
    def "String `#string` matches(#matches) `#pattern`"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = new Sentence(pattern: Pattern.compile(pattern))
        then:
        matcher.match(sentence, string).matches == matches
        where:
        pattern        | string        | matches
        "foo \\w+"     | "foo bar"     | true
        "foo \\w+ foo" | "foo bar foo" | true
    }

    @Unroll
    def "String `#string` matches(#matches) `#pattern` with variables"() {
        when:
        def matcher = new SentenceMatcher()
        def sentence = new Sentence(original: original, variables: variables, pattern: Pattern.compile(pattern))
        then:
        matcher.match(sentence, string, context as Map).matches == matches
        matcher.match(sentence, string, context as Map).attributes == attributes
        where:
        original             | variables         | pattern             | string            | context                         | matches | attributes
        "id"                 | []                | "id"                | "id"              | [:]                             | true    | [:]
        "id"                 | []                | "id"                | "foo"             | [:]                             | false   | [:]
        "id <id>"            | ["<id>"]          | "id \\w+"           | "id 100"          | [:]                             | true    | ["<id>": "100"]
        "id <id>"            | ["<id>"]          | "id \\w+"           | "id 100"          | ["<id>": "100"]                 | true    | [:]
        "id <id>"            | ["<id>"]          | "id \\w+"           | "id 100"          | ["<id>": "200"]                 | false   | [:]
        "id <id>, key <key>" | ["<id>", "<key>"] | "id \\w+, key \\w+" | "id 100, key foo" | ["<id>": "100"]                 | true    | ["<key>": "foo"]
        "id <id>, key <key>" | ["<id>", "<key>"] | "id \\w+, key \\w+" | "id 100, key foo" | ["<id>": "100", "<key>": "foo"] | true    | [:]
        "id <id>, key <key>" | ["<id>", "<key>"] | "id \\w+, key \\w+" | "id 100, key foo" | ["<id>": "100", "<key>": "bar"] | false   | [:]
    }

    @Unroll
    def "Variables #variables are extracted from string `#string` by pattern `pattern`"() {
        when:
        def matcher = new SentenceMatcher()
        then:
        matcher.extractVariables(string, pattern) == variables
        where:
        string           | pattern              | variables
        null             | null                 | [:]
        "id 10"          | "id <id>"            | ["<id>": 10]
        "id 10, key foo" | "id <id>, key <key>" | ["<id>": "10", "key": "foo"]
    }

}
