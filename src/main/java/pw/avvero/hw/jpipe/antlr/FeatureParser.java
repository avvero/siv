package pw.avvero.hw.jpipe.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import pw.avvero.hw.jpipe.gherkin.Feature;
import pw.avvero.hw.jpipe.gherkin.Scenario;
import pw.avvero.hw.jpipe.gherkin.Step;

import java.util.Objects;
import java.util.StringJoiner;
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
            if (child instanceof GherkinParser.FrazeContext) {
                feature.setLabel(parse((GherkinParser.FrazeContext) child));
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
            if (child instanceof GherkinParser.FrazeContext) {
                scenario.setLabel(parse((GherkinParser.FrazeContext) child));
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
            if (child instanceof GherkinParser.FrazeContext) {
                step.setLabel(parse((GherkinParser.FrazeContext) child));
            }
        }
        return step;
    }

    private static String parse(GherkinParser.FrazeContext frazeContext) {
        return frazeContext.children.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(" "));
    }

}
