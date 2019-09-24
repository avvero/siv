package pw.avvero.hw.jpipe

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import pw.avvero.hw.jpipe.antlr.GherkinLexer
import pw.avvero.hw.jpipe.antlr.GherkinParser
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
        feature.scenarios[0].steps[1].sentence.original == "then1"
        feature.scenarios[1].sentence.original == "scenario2"
        feature.scenarios[1].steps[0].sentence.original == "when2"
        feature.scenarios[1].steps[1].sentence.original == "then2"
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
        def feature = new FeatureParser().parseFromFile("src/test/resources/feature/example_3.feature")
        then:
        feature.sentence.original == "Client registration"
        feature.scenarios[0].sentence.original == "Client is registered with account"
        feature.scenarios[0].steps[0].sentence.original == "client with id <clientId>"
        feature.scenarios[0].steps[0].sentence.pattern.toString() == "client with id \\w+"
        feature.scenarios[0].steps[0].sentence.variables == ["<clientId>"]
        feature.scenarios[0].steps[1].sentence.original == "account <accountId> is created for client <clientId>"
        feature.scenarios[0].steps[1].sentence.pattern.toString() == "account \\w+ is created for client \\w+"
        feature.scenarios[0].steps[1].sentence.variables == ["<accountId>", "<clientId>"]
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
