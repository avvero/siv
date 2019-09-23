package pw.avvero.hw.jpipe.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import pw.avvero.hw.jpipe.gherkin.Feature;
import pw.avvero.hw.jpipe.gherkin.Scenario;
import pw.avvero.hw.jpipe.gherkin.Sentence;
import pw.avvero.hw.jpipe.gherkin.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeatureParser extends GherkinBaseListener {

    public static Feature parse(String file) throws Exception {
        GherkinLexer lexer = new GherkinLexer(CharStreams.fromFileName(file));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GherkinParser parser = new GherkinParser(tokens);
        ParseTree tree = parser.feature();
        if (tree.getChildCount() == 0) {
            throw new Exception("Can't parse feature from file " + file);
        }
        Feature feature = new Feature();
        for (int i = 0; i < tree.getChildCount(); i ++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof GherkinParser.PhraseContext) {
                feature.setSentence(parse((GherkinParser.PhraseContext) child));
            } else if (child instanceof GherkinParser.ScenarioContext) {
                Scenario scenario = parse((GherkinParser.ScenarioContext) child);
                feature.getScenarios().add(scenario);
            }
        }
        return feature;
    }

    private static Scenario parse(GherkinParser.ScenarioContext tree) {
        Scenario scenario = new Scenario();
        for (int i = 0; i < tree.getChildCount(); i ++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof GherkinParser.PhraseContext) {
                scenario.setSentence(parse((GherkinParser.PhraseContext) child));
            } else if (child instanceof GherkinParser.StepContext) {
                Step step = parse((GherkinParser.StepContext) child);
                scenario.getSteps().add(step);
            }
        }
        return scenario;
    }

    private static Step parse(GherkinParser.StepContext tree) {
        Step step = new Step();
        for (int i = 0; i < tree.getChildCount(); i ++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof GherkinParser.PhraseContext) {
                step.setSentence(parse((GherkinParser.PhraseContext) child));
            }
        }
        return step;
    }

    private static Sentence parse(GherkinParser.PhraseContext PhraseContext) {
        Sentence sentence = new Sentence();
        List<ParseTree> originalPhraseParts = new ArrayList<>();
        for (ParseTree tree : PhraseContext.children) {
            originalPhraseParts.add(tree);
            if (tree instanceof GherkinParser.VariableContext) {
                if (sentence.getVariables() == null) {
                    sentence.setVariables(new ArrayList<>());
                }
                sentence.getVariables().add(tree.getText());
            }
        }

        String original = originalPhraseParts.stream()
                .map(FeatureParser::stringify)
                .collect(Collectors.joining(" "));
        sentence.setOriginal(original);
        return sentence;
    }

    private static String stringify(ParseTree tree) {
        return tree.getText();
    }

    private static String stringify(GherkinParser.VariableContext tree) {
        return tree.getText();
    }

}
