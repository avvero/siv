package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.walker.SentenceMatcherResult
import pw.avvero.hw.jpipe.walker.SingleHitScenarioTracker

import java.util.function.BiConsumer

class DummyBiConsumer implements BiConsumer<SingleHitScenarioTracker, SentenceMatcherResult> {

    @Override
    void accept(SingleHitScenarioTracker scenarioTracker, SentenceMatcherResult sentenceMatcherResult) {

    }
}