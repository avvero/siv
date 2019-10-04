package pw.avvero.hw.jpipe;

import pw.avvero.hw.jpipe.gherkin.Feature;
import pw.avvero.hw.jpipe.gherkin.Scenario;
import pw.avvero.hw.jpipe.gherkin.Step;

import java.util.Collections;
import java.util.Map;

public class FeatureWriter {

    private static final String TAB = "  ";
    private static final String NEW_LINE = "\n";

    /**
     * Writes feature to string
     * @param feature
     * @param context
     * @return
     */
    public String toString(Feature feature, Map<String, String> context) {
        StringBuilder builder = new StringBuilder();
        builder.append("Feature: ")
                .append(feature.getSentence().getOriginal(context))
                .append(NEW_LINE);
        for (Scenario scenario : feature.getScenarios()) {
            builder.append(TAB)
                    .append("Scenario: ")
                    .append(scenario.getSentence().getOriginal(context))
                    .append(NEW_LINE);
            for (Step step : scenario.getSteps()) {
                builder.append(TAB)
                        .append(TAB)
                        .append(step.getClass().getSimpleName())
                        .append(": ")
                        .append(step.getSentence().getOriginal(context))
                        .append(NEW_LINE);
            }
        }
        return builder.toString();
    }

    public String toString(Feature feature) {
        return toString(feature, Collections.emptyMap());
    }

}
