package pw.avvero.hw.siv.walker;

import pw.avvero.hw.siv.gherkin.Scenario;
import pw.avvero.hw.siv.gherkin.Step;

import java.util.Date;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Scenario tracker which allows only a single hit for step by incoming string, i.e. if step is hit it considered
 * completed and will not be engaged in tracking after
 */
public class SingleHitScenarioTracker {

    private Scenario scenario;
    private int currentStepIndex = 0;
    private int[] stepsHits;
    private boolean finished;
    private HashMap<String, String> context = new HashMap<>();
    private Date startedDate;
    private Date finishedDate;

    public SingleHitScenarioTracker(Scenario scenario) {
        this.scenario = scenario;
        this.stepsHits = new int[scenario.getSteps().size()];
    }

    public boolean hit(String s,
                       Consumer<SingleHitScenarioTracker> onStart,
                       BiConsumer<SingleHitScenarioTracker, SentenceMatcherResult> onHit,
                       Consumer<SingleHitScenarioTracker> onFinish) {
        if (finished) return false;

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
                startedDate = new Date();
                onStart.accept(this);
            }
            if (hitIndex == scenario.getSteps().size() - 1) {
                finished = true;
                finishedDate = new Date();
                onFinish.accept(this);
            }
        }
        return matchResult.isMatches();
    }

    public int[] getStepsHits() {
        return stepsHits;
    }

    public boolean isFinished() {
        return finished;
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

    public Date getStartedDate() {
        return startedDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }
}
