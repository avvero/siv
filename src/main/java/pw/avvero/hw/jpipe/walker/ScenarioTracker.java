package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Scenario;
import pw.avvero.hw.jpipe.gherkin.Step;

import java.util.HashMap;
import java.util.function.BiConsumer;
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

    public boolean hit(String s,
                       Consumer<ScenarioTracker> onStart,
                       BiConsumer<ScenarioTracker, SentenceMatcherResult> onHit,
                       Consumer<ScenarioTracker> onFinish) {
        if (completed) return false;

        Step step = scenario.getSteps().get(currentStepIndex);
        SentenceMatcherResult matchResult = SentenceMatcher.getInstance().match(step.getSentence(), s, context);
        if (matchResult.isMatches()) {
            // fill the context
            if (matchResult.getAttributes() != null && matchResult.getAttributes().size() > 0) {
                matchResult.getAttributes().forEach((k, v) -> context.putIfAbsent(k, v));
            }
            int hitIndex = currentStepIndex;
            currentStepIndex++;
            // process step
            this.stepsHits[hitIndex] = 1;
            onHit.accept(this, matchResult);
            if (hitIndex == 0) {
                onStart.accept(this);
            } else if (hitIndex == scenario.getSteps().size() - 1) {
                completed = true;
                onFinish.accept(this);
            }
        }
        return matchResult.isMatches();
    }

    public int[] getStepsHits() {
        return stepsHits;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isStarted() {
        return currentStepIndex > 0;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public HashMap<String, String> getContext() {
        return context;
    }
}
