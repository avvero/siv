package pw.avvero.hw.siv

import pw.avvero.hw.siv.walker.SingleHitScenarioTracker

import java.util.function.Consumer

class DummyConsumer implements Consumer<SingleHitScenarioTracker> {
    @Override
    void accept(SingleHitScenarioTracker scenarioTracker) {
    }
}