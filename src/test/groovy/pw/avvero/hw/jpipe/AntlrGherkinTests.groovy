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

    def "Features is parsed from file"() {
        when:
        def feature = FeatureParser.parse("src/test/resources/example_2.feature")
        then:
        feature.label == "feature1"
        feature.scenarios[0].label == "scenario1"
        feature.scenarios[0].steps[0].label == "when1"
        feature.scenarios[0].steps[1].label == "then1"
        feature.scenarios[1].label == "scenario2"
        feature.scenarios[1].steps[0].label == "when2"
        feature.scenarios[1].steps[1].label == "then2"
    }

}
