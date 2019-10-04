package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Scenario;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SingleDirectOrderScenarioWalker {

    private Scenario scenario;
    private List<ScenarioTracker> scenarioTrackers = new LinkedList<>();
    private Consumer<ScenarioTracker> onScenarioStart;
    private BiConsumer<ScenarioTracker, SentenceMatcherResult> onScenarioHit;
    private Consumer<ScenarioTracker> onScenarioFinish;

    public SingleDirectOrderScenarioWalker(Scenario scenario,
                                           Consumer<ScenarioTracker> onScenarioStart,
                                           BiConsumer<ScenarioTracker, SentenceMatcherResult> onScenarioHit,
                                           Consumer<ScenarioTracker> onScenarioFinish) {
        this.scenario = scenario;
        this.onScenarioStart = onScenarioStart;
        this.onScenarioHit = onScenarioHit;
        this.onScenarioFinish = onScenarioFinish;
        trackNewScenario();
    }

    /**
     * Start new scenario tracker
     */
    private void trackNewScenario() {
        if (scenarioTrackers.stream().allMatch(ScenarioTracker::isStarted)) {
            scenarioTrackers.add(new ScenarioTracker(this.scenario));
        }
    }

    public void pass(String s) {
        if (s == null || "".equals(s.trim())) return;
        for (ScenarioTracker scenarioTracker : scenarioTrackers) {
            boolean hit = scenarioTracker.hit(
                    s,
                    this::onScenarioStart,
                    this::onScenarioHit,
                    this::onScenarioFinish
            );
            if (hit) return;
        }
    }

    private void onScenarioStart(ScenarioTracker scenarioTracker) {
        trackNewScenario();
        onScenarioStart.accept(scenarioTracker);
    }

    private void onScenarioHit(ScenarioTracker scenarioTracker, SentenceMatcherResult result) {
        onScenarioHit.accept(scenarioTracker, result);
    }

    private void onScenarioFinish(ScenarioTracker scenarioTracker) {
        scenarioTrackers.remove(scenarioTracker);
        // redundant, need to check is there any not started trackers
        trackNewScenario();
        onScenarioFinish.accept(scenarioTracker);
    }
}
