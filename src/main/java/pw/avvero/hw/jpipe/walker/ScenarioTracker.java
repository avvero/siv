package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Scenario;
import pw.avvero.hw.jpipe.gherkin.Step;

import java.util.HashMap;
import java.util.function.Consumer;

public class ScenarioTracker {

    private Scenario scenario;
    private int currentStepIndex = 0;
    private int[] stepsHits;
    private boolean completed;
    private HashMap<String, String> context = new HashMap<>();

    public ScenarioTracker(Scenario scenario) {
        this.scenario = scenario;
        this.stepsHits = new int[scenario.getSteps().size()];
    }

    public boolean hit(String s, Consumer<ScenarioTracker> onStart, Consumer<ScenarioTracker> onFinish) {
        if (completed) return false;

        Step step = scenario.getSteps().get(currentStepIndex);
        boolean matches = SentenceMatcher.getInstance().match(step.getSentence(), s).matches;
        if (matches) {
            // process step
            this.stepsHits[currentStepIndex] = 1;
            if (currentStepIndex == 0) {
                onStart.accept(this);
            }
            if (currentStepIndex == scenario.getSteps().size() - 1) {
                completed = true;
                onFinish.accept(this);
            }
            currentStepIndex++;
        }
        return matches;
    }

    public int[] getStepsHits() {
        return stepsHits;
    }

    public boolean isCompleted() {
        return completed;
    }
}
