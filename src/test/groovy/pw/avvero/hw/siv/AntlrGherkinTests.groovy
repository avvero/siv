package pw.avvero.hw.siv

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import pw.avvero.hw.siv.antlr.GherkinLexer
import pw.avvero.hw.siv.antlr.GherkinParser
import pw.avvero.hw.siv.gherkin.RegExp
import pw.avvero.hw.siv.gherkin.Space
import pw.avvero.hw.siv.gherkin.Variable
import pw.avvero.hw.siv.gherkin.Word
import spock.lang.Specification

class AntlrGherkinTests extends Specification {

    def "Gherkin test"() {
        expect:
        GherkinLexer lexer = new GherkinLexer(CharStreams.fromFileName("src/test/resources/feature/example_2.feature"))
        CommonTokenStream tokens = new CommonTokenStream(lexer)
        GherkinParser parser = new GherkinParser(tokens);
        ParseTree tree = parser.feature();
        ParseTreeWalker walker = new ParseTreeWalker();
//        walker.walk(new HelloWalker(), tree);

    }

    def "Simple feature is parsed from file"() {
        when:
        def feature = new FeatureParser().parseFromFile("src/test/resources/feature/example_2.feature")
        then:
        feature.sentence.original == "first feature"
        feature.scenarios[0].sentence.original == "first scenario"
        feature.scenarios[0].steps[0].sentence.original == "first when"
        feature.scenarios[0].steps[0].sentence.pattern.toString() == "first when"
        feature.scenarios[0].steps[1].sentence.original == "then1, 2"
        feature.scenarios[0].steps[1].sentence.pattern.toString() == "then1, 2"
        feature.scenarios[1].sentence.original == "scenario2"
        feature.scenarios[1].steps[0].sentence.original == "when2"
        feature.scenarios[1].steps[0].sentence.pattern.toString() == "when2"
        feature.scenarios[1].steps[1].sentence.original == "then2"
        feature.scenarios[1].steps[1].sentence.pattern.toString() == "then2"
    }

    def "Complicated feature is parsed from file"() {
        when:
        def feature = new FeatureParser().parseFromFile("src/test/resources/feature/example_1.feature")
        then:
        feature.sentence.original == "Guess the word"
        feature.scenarios[0].sentence.original == "Maker starts a game"
        feature.scenarios[0].steps[0].sentence.original == "the Maker starts a game"
        feature.scenarios[0].steps[1].sentence.original == "the Maker waits for a Breaker to join"
        feature.scenarios[1].sentence.original == "Breaker joins a game"
        feature.scenarios[1].steps[0].sentence.original == "the Maker has started a game with the word \"silky\""
        feature.scenarios[1].steps[1].sentence.original == "the Breaker joins the Maker's game"
        feature.scenarios[1].steps[2].sentence.original == "the Breaker must guess a word with 5 characters"
    }

    def "Feature with variables is parsed from file"() {
        when:
        def f = new FeatureParser().parseFromFile("src/test/resources/feature/example_3.feature")
        then:
        f.sentence.original == "Client registration"
        f.scenarios[0].sentence.original == "Client is registered with account"
        f.scenarios[0].steps[0].sentence.original == "client with id <clientId>"
        f.scenarios[0].steps[0].sentence.pattern.toString() == "client with id (?<clientId>[\\w-]+)"
        f.scenarios[0].steps[0].sentence.variables == [new Variable("clientId")]
        f.scenarios[0].steps[1].sentence.original == "account <accountId> is created for client <clientId>"
        f.scenarios[0].steps[1].sentence.pattern.toString() == "account (?<accountId>[\\w-]+) is created for client (?<clientId>[\\w-]+)"
        f.scenarios[0].steps[1].sentence.variables == [new Variable("accountId"), new Variable("clientId")]
    }

    def "Feature with complicated sentence"() {
        when:
        def f = new FeatureParser().parseFromString(featureString)
        then:
        f.sentence.original == "DB call"
        f.scenarios[0].sentence.original == "DB call"
        f.scenarios[0].steps[0].sentence.original == "(,91508063b)  qtp2092956823-487  params (repository.add)"
        where:
        featureString = """Feature: DB call
  Scenario: DB call
    When: (,91508063b)  qtp2092956823-487  params (repository.add)
        """
    }

    def "Feature with very complicated sentence"() {
        when:
        def f = new FeatureParser().parseFromString(featureString)
        then:
        f.sentence.original == "DB call"
        f.scenarios[0].sentence.original == "DB call"
        f.scenarios[0].steps[0].sentence.original == "(,9766fe0fcd059,) qtp1709629705-418 jpa:log:1164 - ******* InParams (repository.user_add) *******"
        where:
        featureString = """
        Feature: DB call
          Scenario: DB call
            When: (,9766fe0fcd059,) qtp1709629705-418 jpa:log:1164 - ******* InParams (repository.user_add) *******"""
    }

    def "Feature with regex"() {
        when:
        def f = new FeatureParser().parseFromString(featureString)
        then:
        f.sentence.original == "DB call"
        f.scenarios[0].sentence.original == "DB call"
        f.scenarios[0].steps[0].sentence.original == "foo \\w+ bar"
        where:
        featureString = """
        Feature: DB call
          Scenario: DB call
            When: foo /\\w+/ bar"""
    }

    def "Parses rises exception if feature has no scenarios"() {
        when:
        new FeatureParser().parseFromString(featureString)
        then:
        thrown(Exception)
        where:
        featureString = """
            Feature: Client registration
        """
    }

    def "Parses rises exception if scenario has no steps"() {
        when:
        new FeatureParser().parseFromString(featureString)
        then:
        thrown(Exception)
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
        """
    }

}
