package pw.avvero.hw.jpipe

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import pw.avvero.hw.jpipe.antlr.FeatureParser
import pw.avvero.hw.jpipe.antlr.GherkinLexer
import pw.avvero.hw.jpipe.antlr.GherkinParser
import spock.lang.Specification

class AntlrGherkinTests extends Specification {

    def "Gherkin test"() {
        expect:
        GherkinLexer lexer = new GherkinLexer(CharStreams.fromFileName("src/test/resources/example_2.feature"))
        CommonTokenStream tokens = new CommonTokenStream(lexer)
        GherkinParser parser = new GherkinParser(tokens);
        ParseTree tree = parser.feature();
        ParseTreeWalker walker = new ParseTreeWalker();
//        walker.walk(new HelloWalker(), tree);

    }

    def "Features is parsed from file 1"() {
        when:
        def feature = FeatureParser.parse("src/test/resources/example_1.feature")
        then:
        feature.label == "Guess the word"
        feature.scenarios[0].label == "Maker starts a game"
        feature.scenarios[0].steps[0].label == "the Maker starts a game"
        feature.scenarios[0].steps[1].label == "the Maker waits for a Breaker to join"
        feature.scenarios[1].label == "Breaker joins a game"
        feature.scenarios[1].steps[0].label == "the Maker has started a game with the word \"silky\""
        feature.scenarios[1].steps[1].label == "the Breaker joins the Maker's game"
        feature.scenarios[1].steps[2].label == "the Breaker must guess a word with 5 characters"
    }


    def "Features is parsed from file 2"() {
        when:
        def feature = FeatureParser.parse("src/test/resources/example_2.feature")
        then:
        feature.label == "first feature"
        feature.scenarios[0].label == "first scenario"
        feature.scenarios[0].steps[0].label == "first when"
        feature.scenarios[0].steps[1].label == "then1"
        feature.scenarios[1].label == "scenario2"
        feature.scenarios[1].steps[0].label == "when2"
        feature.scenarios[1].steps[1].label == "then2"
    }

}
