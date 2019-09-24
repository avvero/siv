package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Scenario;
import pw.avvero.hw.jpipe.gherkin.Step;

import java.util.function.Consumer;

public class ScenarioTracker {

    private Scenario scenario;
    private int currentStepIndex = 0;
    private int[] stepsHits;
    private boolean finished;

    public ScenarioTracker(Scenario scenario) {
        this.scenario = scenario;
        this.stepsHits = new int[scenario.getSteps().size()];
    }

    public boolean hit(String s, Consumer<ScenarioTracker> onStart, Consumer<ScenarioTracker> onFinish) {
        Step step = scenario.getSteps().get(currentStepIndex);
        boolean matches = SentenceMatcher.getInstance().matches(step.getSentence(), s);
        if (matches) {
            this.stepsHits[currentStepIndex] = 1;
            if (currentStepIndex == 0) {
                onStart.accept(this);
            }
            if (currentStepIndex == scenario.getSteps().size() - 1) {
                finished = true;
                onFinish.accept(this);
            }
            currentStepIndex++;
        }
        return matches;
    }

    public int[] getStepsHits() {
        return stepsHits;
    }

    public boolean isFinished() {
        return finished;
    }
}
