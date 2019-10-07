package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.walker.SingleHitScenarioWalker
import spock.lang.Ignore
import spock.lang.Specification

@Ignore //Have no idea how to do it right now
class MultiHitScenarioTrackerTests extends Specification {

    def "Scenario is completed successfully with single case without context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleHitScenarioWalker(feature.scenarios.first(), new DummyConsumer(),
                new DummyBiConsumer(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.completed == [true]
        finishedBucket.list.stepsHits == [[1, 1] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id is registered
                Then: account is created for client
        """
        log = """
        2019-09-24 INFO client with id is registered
        2019-09-24 INFO account is created for client
        2019-09-24 INFO account is created for client
        """
    }

    def "Scenario is completed successfully with multi case with context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleHitScenarioWalker(feature.scenarios.first(), new DummyConsumer(),
                new DummyBiConsumer(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.completed == [true]
        finishedBucket.list.stepsHits == [[1, 2] as int[]]
        walker.scenarioTrackers.completed == [false]
        walker.scenarioTrackers.stepsHits == [[0, 0] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id <clientId> is registered
                Then: account is created for client <clientId>
        """
        log = """
        2019-09-24 INFO client with id 100 is registered
        2019-09-24 INFO account is created for client 100
        2019-09-24 INFO account is created for client 100
        """
    }
}
