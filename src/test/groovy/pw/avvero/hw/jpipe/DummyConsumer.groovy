package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.walker.SingleHitScenarioTracker

import java.util.function.Consumer

class DummyConsumer implements Consumer<SingleHitScenarioTracker> {
    @Override
    void accept(SingleHitScenarioTracker scenarioTracker) {
    }
}