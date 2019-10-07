package pw.avvero.hw.siv

import pw.avvero.hw.siv.walker.FeatureWalker
import pw.avvero.hw.siv.walker.SingleHitScenarioWalkerFactory
import spock.lang.Specification

class FeatureWalkerTests extends Specification {

    def "Single scenario is completed successfully with single case without context"() {
        when:
        def finishedBucket = new FinishedTrackersBucket()

        def feature = new FeatureParser().parseFromString(featureString)
        def factory = new SingleHitScenarioWalkerFactory(new DummyConsumer(), new DummyBiConsumer(), finishedBucket)
        def walker = new FeatureWalker(feature, factory)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.finished == [true]
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
        """
    }

    def "Several scenarios are completed successfully with single case without context"() {
        when:
        def finishedBucket = new FinishedTrackersBucket()

        def feature = new FeatureParser().parseFromString(featureString)
        def factory = new SingleHitScenarioWalkerFactory(new DummyConsumer(), new DummyBiConsumer(), finishedBucket)
        def walker = new FeatureWalker(feature, factory)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.finished == [true, true]
        finishedBucket.list.stepsHits == [[1, 1] as int[], [1, 1, 1] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id is registered
                Then: account is created for client
              Scenario: Client is deposited
                When: client set amount
                When: client set currency
                Then: payment successful
        """
        log = """
        2019-09-24 INFO client with id is registered
        2019-09-24 INFO client set amount
        2019-09-24 INFO client set currency
        2019-09-24 INFO account is created for client
        2019-09-24 INFO payment successful
        """
    }

    def "Single Scenario is completed successfully with single case with context"() {
        when:
        def finishedBucket = new FinishedTrackersBucket()

        def feature = new FeatureParser().parseFromString(featureString)
        def factory = new SingleHitScenarioWalkerFactory(new DummyConsumer(), new DummyBiConsumer(), finishedBucket)
        def walker = new FeatureWalker(feature, factory)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.finished == [true, true]
        finishedBucket.list.stepsHits == [[1, 1] as int[], [1, 1, 1] as int[]]
        walker.walkers.scenarioTrackers.finished == [[false], [false, false]]
        walker.walkers.scenarioTrackers.stepsHits == [[[0, 0] as int[]], [[1, 0, 0] as int[], [0, 0, 0] as int[]]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id <clientId> is registered
                Then: account is created for client <clientId>
              Scenario: Client is deposited
                When: client with id <clientId> set amount <amount>
                When: client with id <clientId> set currency
                Then: payment on <amount> successful for client <clientId> successful
        """
        log = """
        2019-09-24 INFO client with id 100 is registered
        2019-09-24 INFO client with id 100 set amount 50
        2019-09-24 INFO client with id 200 set amount 70
        2019-09-24 INFO client with id 100 set currency
        2019-09-24 INFO account is created for client 100
        2019-09-24 payment on 50 successful for client 100 successful
        """
    }

}
