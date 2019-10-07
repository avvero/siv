package pw.avvero.hw.siv.walker;

import pw.avvero.hw.siv.gherkin.Scenario;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SingleHitScenarioWalker implements Walker<Scenario> {

    private Scenario scenario;
    private List<SingleHitScenarioTracker> scenarioTrackers = new LinkedList<>();
    private Consumer<SingleHitScenarioTracker> onScenarioStart;
    private BiConsumer<SingleHitScenarioTracker, SentenceMatcherResult> onScenarioHit;
    private Consumer<SingleHitScenarioTracker> onScenarioFinish;

    public SingleHitScenarioWalker(Scenario scenario,
                                   Consumer<SingleHitScenarioTracker> onScenarioStart,
                                   BiConsumer<SingleHitScenarioTracker, SentenceMatcherResult> onScenarioHit,
                                   Consumer<SingleHitScenarioTracker> onScenarioFinish) {
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
        if (scenarioTrackers.stream().allMatch(SingleHitScenarioTracker::isStarted)) {
            scenarioTrackers.add(new SingleHitScenarioTracker(this.scenario));
        }
    }

    public void pass(String s) {
        if (s == null || "".equals(s.trim())) return;
        for (SingleHitScenarioTracker scenarioTracker : scenarioTrackers) {
            boolean hit = scenarioTracker.hit(
                    s,
                    this::onScenarioStart,
                    this::onScenarioHit,
                    this::onScenarioFinish
            );
            if (hit) return;
        }
    }

    private void onScenarioStart(SingleHitScenarioTracker scenarioTracker) {
        trackNewScenario();
        onScenarioStart.accept(scenarioTracker);
    }

    private void onScenarioHit(SingleHitScenarioTracker scenarioTracker, SentenceMatcherResult result) {
        onScenarioHit.accept(scenarioTracker, result);
    }

    private void onScenarioFinish(SingleHitScenarioTracker scenarioTracker) {
        scenarioTrackers.remove(scenarioTracker);
        // redundant, need to check is there any not started trackers
        trackNewScenario();
        onScenarioFinish.accept(scenarioTracker);
    }
}
