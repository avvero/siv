package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Scenario;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class SingleDirectOrderScenarioWalker {

    private Scenario scenario;
    private List<ScenarioTracker> scenarioTrackers = new LinkedList<>();
    private Consumer<ScenarioTracker> onTrackerFinish;

    public SingleDirectOrderScenarioWalker(Scenario scenario, Consumer<ScenarioTracker> onTrackerFinish) {
        this.scenario = scenario;
        this.onTrackerFinish = onTrackerFinish;
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
            boolean hit = scenarioTracker.hit(s, this::onScenarioTrackerStart, this::onScenarioTrackerFinish);
            if (hit) return;
        }
    }

    private void onScenarioTrackerStart(ScenarioTracker scenarioTracker) {
        trackNewScenario();
    }

    private void onScenarioTrackerFinish(ScenarioTracker scenarioTracker) {
        scenarioTrackers.remove(scenarioTracker);
        // redundant, need to check is there any not started trackers
        trackNewScenario();
        onTrackerFinish.accept(scenarioTracker);
    }
}
