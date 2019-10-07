package pw.avvero.hw.siv

import pw.avvero.hw.siv.walker.SentenceMatcherResult
import pw.avvero.hw.siv.walker.SingleHitScenarioTracker

import java.util.function.BiConsumer

class DummyBiConsumer implements BiConsumer<SingleHitScenarioTracker, SentenceMatcherResult> {

    @Override
    void accept(SingleHitScenarioTracker scenarioTracker, SentenceMatcherResult sentenceMatcherResult) {

    }
}