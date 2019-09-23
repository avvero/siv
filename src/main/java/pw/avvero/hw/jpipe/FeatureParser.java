package pw.avvero.hw.jpipe;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import pw.avvero.hw.jpipe.antlr.GherkinBaseListener;
import pw.avvero.hw.jpipe.antlr.GherkinLexer;
import pw.avvero.hw.jpipe.antlr.GherkinParser;
import pw.avvero.hw.jpipe.gherkin.*;

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
     * @param scenarioContext
     * @return
     */
    private Scenario parse(GherkinParser.ScenarioContext scenarioContext) {
        Scenario scenario = new Scenario();
        for (int i = 0; i < scenarioContext.getChildCount(); i++) {
            ParseTree child = scenarioContext.getChild(i);
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
     * @param stepContext
     * @return
     */
    private Step parse(GherkinParser.StepContext stepContext) {
        Step step = new Step();
        for (ParseTree type : stepContext.children) {
            if (type instanceof GherkinParser.GivenContext) {
                return update(new Given(), (GherkinParser.GivenContext) type);
            } else if (type instanceof GherkinParser.WhenContext) {
                return update(new When(), (GherkinParser.WhenContext) type);
            } else if (type instanceof GherkinParser.ThenContext) {
                return update(new Then(), (GherkinParser.ThenContext) type);
            } else if (type instanceof GherkinParser.AndContext) {
                return update(new And(), (GherkinParser.AndContext) type);
            }
        }
        return step;
    }

    private Step update(Step step, ParserRuleContext givenContext) {
        for (ParseTree child : givenContext.children) {
            if (child instanceof GherkinParser.SentenceContext) {
                step.setSentence(parse((GherkinParser.SentenceContext) child));
            }
        }
        return step;
    }

    /**
     * Parsers phrase
     *
     * @param sentenceContext
     * @return
     */
    private Sentence parse(GherkinParser.SentenceContext sentenceContext) {
        Sentence sentence = new Sentence();
        List<String> originalPhraseParts = new ArrayList<>();
        List<String> templatePhraseParts = new ArrayList<>();
        for (ParseTree child : sentenceContext.children) {
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
