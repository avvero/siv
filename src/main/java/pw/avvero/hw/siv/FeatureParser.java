package pw.avvero.hw.siv;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import pw.avvero.hw.siv.antlr.GherkinLexer;
import pw.avvero.hw.siv.antlr.GherkinParser;
import pw.avvero.hw.siv.gherkin.*;

/**
 * Parses features with help of antlr
 */
public class FeatureParser {

    /**
     * Parsers feature from string
     * @param string
     * @return
     * @throws Exception
     */
    public Feature parseFromString(String string) throws Exception {
        return parseFromCharStream(CharStreams.fromString(string));
    }

    /**
     * Parsers feature from file
     * @param filePath
     * @return
     * @throws Exception
     */
    public Feature parseFromFile(String filePath) throws Exception {
        return parseFromCharStream(CharStreams.fromFileName(filePath));
    }

    /**
     * Parses feature from CharStream
     * @param charStream
     * @return
     * @throws Exception
     */
    public Feature parseFromCharStream(CharStream charStream) throws Exception {
        GherkinLexer lexer = new GherkinLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GherkinParser parser = new GherkinParser(tokens);
        ParseTree tree = parser.feature();
        if (tree.getChildCount() == 0) {
            throw new Exception("Can't parse feature");
        }
        Feature feature = new Feature();
        for (int i = 0; i < tree.getChildCount(); i ++) {
            ParseTree child = tree.getChild(i);
            if (child instanceof GherkinParser.LeadSpaceContext) {
                //skip
            } else if (child instanceof GherkinParser.SentenceContext) {
                feature.setSentence(parse((GherkinParser.SentenceContext) child));
            } else if (child instanceof GherkinParser.ScenarioContext) {
                Scenario scenario = parse((GherkinParser.ScenarioContext) child);
                feature.getScenarios().add(scenario);
            }
        }
        if (feature.getScenarios().size() == 0) {
            throw new Exception("Feature parsing is unsuccessful, scenarios are not recognized");
        }
        for (Scenario scenario : feature.getScenarios()) {
            if (scenario.getSteps().size() == 0) {
                throw new Exception(String.format("Feature parsing is unsuccessful, steps are not recognized for scenario '%s'",
                        scenario.getSentence().getOriginal()));
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

    /**
     * Updates step with context
     * @param step
     * @param context
     * @return
     */
    private Step update(Step step, ParserRuleContext context) {
        for (ParseTree child : context.children) {
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
        Sentence sentence = new CacheableSentence();
        for (ParseTree child : sentenceContext.children) {
            if (child instanceof GherkinParser.VariableContext) {
                GherkinParser.VariableContext variableContext = (GherkinParser.VariableContext) child;
                String sbs = variableContext.getText().substring(1, variableContext.getText().length() - 1);
                sentence.getChunks().add(new Variable(sbs));
            } else if (child instanceof GherkinParser.SignContext) {
                sentence.getChunks().add(new Sign(child.getText()));
            } else if (child instanceof GherkinParser.SpaceContext) {
                sentence.getChunks().add(new Space(child.getText()));
            } else if (child instanceof GherkinParser.RegExpContext) {
                GherkinParser.RegExpContext regExpContext = (GherkinParser.RegExpContext) child;
                String sbs = regExpContext.getText().substring(1, regExpContext.getText().length() - 1);
                sentence.getChunks().add(new RegExp(sbs));
            } else {
                sentence.getChunks().add(new Word(child.getText()));
            }
        }
        return sentence;
    }
}
