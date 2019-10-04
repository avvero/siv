package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.walker.ScenarioTracker
import pw.avvero.hw.jpipe.walker.SingleDirectOrderScenarioWalker
import spock.lang.Specification

import java.util.function.Consumer

class SingleDirectOrderScenarioWalkerTests extends Specification {

    def "Scenario is completed successfully with single case without context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def completedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), completedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        completedBucket.list.completed == [true]
        completedBucket.list.stepsHits == [[1, 1] as int[]]
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

    def "Scenario is not completed successfully with single case without context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        walker.scenarioTrackers.completed == [false, false]
        walker.scenarioTrackers.stepsHits == [[1, 0] as int[], [0, 0] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id is registered
                Then: account is created for client
        """
        log = """
        2019-09-24 INFO client with id is registered
        2019-09-24 INFO foo
        """
    }

    def "Scenario is not started successfully with single case without context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        walker.scenarioTrackers.completed == [false]
        walker.scenarioTrackers.stepsHits == [[0, 0] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id is registered
                Then: account is created for client
        """
        log = """
        2019-09-24 INFO bar
        2019-09-24 INFO foo
        """
    }

    def "Scenario is not completed successfully with single case with context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def completedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), completedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        completedBucket.list.completed == []
        completedBucket.list.stepsHits == []
        walker.scenarioTrackers.completed == [false, false]
        walker.scenarioTrackers.stepsHits == [[1, 0] as int[], [0, 0] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id <clientId> is registered
                Then: account is created for client <clientId>
        """
        log = """
        2019-09-24 INFO client with id 100 is registered
        2019-09-24 INFO account is created for client 200
        """
    }

    def "Scenario is completed successfully with single case with context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def completedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), completedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        completedBucket.list.completed == [true]
        completedBucket.list.stepsHits == [[1, 1] as int[]]
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
        """
    }

    def "Scenario is completed successfully with double case without context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.completed == [true, true]
        finishedBucket.list.stepsHits == [[1, 1] as int[], [1, 1] as int[]]
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
        2019-09-24 INFO client with id is registered
        2019-09-24 INFO account is created for client
        """
    }

    class FinishedTrackersBucket implements Consumer<ScenarioTracker> {
        List<ScenarioTracker> list = new ArrayList<>()
        @Override
        void accept(ScenarioTracker scenarioTracker) {
            list << scenarioTracker
        }
    }

}
