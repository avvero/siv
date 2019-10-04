package pw.avvero.hw.jpipe

import spock.lang.Specification

class FeatureWriterTests extends Specification {

    def "FeatureWriter prints to string"() {
        when:
        def writer = new FeatureWriter()
        def feature = new FeatureParser().parseFromFile("src/test/resources/feature/example_1.feature")
        then:
        writer.toString(feature) == """Feature: Guess the word
  Scenario: Maker starts a game
    When: the Maker starts a game
    Then: the Maker waits for a Breaker to join
  Scenario: Breaker joins a game
    Given: the Maker has started a game with the word "silky"
    When: the Breaker joins the Maker's game
    Then: the Breaker must guess a word with 5 characters"""
    }

    def "FeatureWriter prints to string with the context"() {
        when:
        def writer = new FeatureWriter()
        def feature = new FeatureParser().parseFromFile("src/test/resources/feature/example_3.feature")
        then:
        writer.toString(feature, ["clientId": "12345", "accountId": "5000"] as Map) == """Feature: Client registration
  Scenario: Client is registered with account
    When: client with id 12345
    Then: account 5000 is created for client 12345"""
    }

}
