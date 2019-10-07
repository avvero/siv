package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Scenario;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SingleHitScenarioWalkerFactory implements WalkerFactory<Scenario> {

    private Consumer<SingleHitScenarioTracker> onScenarioStart;
    private BiConsumer<SingleHitScenarioTracker, SentenceMatcherResult> onScenarioHit;
    private Consumer<SingleHitScenarioTracker> onScenarioFinish;

    public SingleHitScenarioWalkerFactory(Consumer<SingleHitScenarioTracker> onScenarioStart,
                                          BiConsumer<SingleHitScenarioTracker, SentenceMatcherResult> onScenarioHit,
                                          Consumer<SingleHitScenarioTracker> onScenarioFinish) {
        this.onScenarioStart = onScenarioStart;
        this.onScenarioHit = onScenarioHit;
        this.onScenarioFinish = onScenarioFinish;
    }

    @Override
    public Walker<Scenario> getInstance(Scenario scenario) {
        return new SingleHitScenarioWalker(scenario, onScenarioStart, onScenarioHit, onScenarioFinish);
    }

}
