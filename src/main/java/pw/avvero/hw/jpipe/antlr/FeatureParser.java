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

public class FeatureParser extends GherkinBaseListener {

    /**
     * Parsers feature from file
     * @param file
     * @return
     * @throws Exception
     */
    public Feature parse(String file) throws Exception {
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
            if (child instanceof GherkinParser.SentenceContext) {
                feature.setSentence(parse((GherkinParser.SentenceContext) child));
            } else if (child instanceof GherkinParser.ScenarioContext) {
                Scenario scenario = parse((GherkinParser.ScenarioContext) child);
                feature.getScenarios().add(scenario);
            }
        }
        return feature;
    }

    /**
     * Parses Scenario
     *
     * @param tree
     * @return
     */
    private Scenario parse(GherkinParser.ScenarioContext tree) {
        Scenario scenario = new Scenario();
        for (int i = 0; i < tree.getChildCount(); i ++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof GherkinParser.SentenceContext) {
                scenario.setSentence(parse((GherkinParser.SentenceContext) child));
            } else if (child instanceof GherkinParser.StepContext) {
                Step step = parse((GherkinParser.StepContext) child);
                scenario.getSteps().add(step);
            }
        }
        return scenario;
    }

    /**
     * Parses Step
     *
     * @param tree
     * @return
     */
    private Step parse(GherkinParser.StepContext tree) {
        Step step = new Step();
        for (ParseTree child : tree.children) {
            if (child instanceof GherkinParser.SentenceContext) {
                step.setSentence(parse((GherkinParser.SentenceContext) child));
            }
        }
        return step;
    }

    /**
     * Parsers phrase
     *
     * @param SentenceContext
     * @return
     */
    private Sentence parse(GherkinParser.SentenceContext SentenceContext) {
        Sentence sentence = new Sentence();
        List<String> originalPhraseParts = new ArrayList<>();
        List<String> templatePhraseParts = new ArrayList<>();
        for (ParseTree child : SentenceContext.children) {
            originalPhraseParts.add(child.getText());
            if (child instanceof GherkinParser.VariableContext) {
                templatePhraseParts.add("\\w+");

                if (sentence.getVariables() == null) {
                    sentence.setVariables(new ArrayList<>());
                }
                sentence.getVariables().add(child.getText());
            } else {
                templatePhraseParts.add(child.getText());
            }
        }

        sentence.setOriginal(String.join(" ", originalPhraseParts));
        if (sentence.getVariables() != null && sentence.getVariables().size() > 0) {
            sentence.setTemplate(String.join(" ", templatePhraseParts));
        }
        return sentence;
    }
}
