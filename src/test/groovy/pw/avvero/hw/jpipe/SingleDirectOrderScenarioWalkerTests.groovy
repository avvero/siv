package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.walker.ScenarioTracker
import pw.avvero.hw.jpipe.walker.SingleDirectOrderScenarioWalker
import spock.lang.Specification

import java.util.function.Consumer

class SingleDirectOrderScenarioWalkerTests extends Specification {

    def "Feature hits successfully with single case"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.finished == [true]
        finishedBucket.list.stepsHits == [[1, 1] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id <clientId> is registered
                Then: account <accountId> is created for client <clientId>
        """
        log = """
        2019-09-24 INFO client with id 1000 is registered
        2019-09-24 INFO account 40 is created for client 1000
        """
    }

    def "Feature hits unsuccessfully with single case"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        walker.scenarioTrackers.finished == [false, false]
        walker.scenarioTrackers.stepsHits == [[1, 0] as int[], [0, 0] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id <clientId> is registered
                Then: account <accountId> is created for client <clientId>
        """
        log = """
        2019-09-24 INFO client with id 1000 is registered
        2019-09-24 INFO foo
        """
    }

    def "Feature hits successfully with double case"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def finishedBucket = new FinishedTrackersBucket()
        def walker = new SingleDirectOrderScenarioWalker(feature.scenarios.first(), finishedBucket)
        log.split("\n").each { l -> walker.pass(l)}
        then:
        finishedBucket.list.finished == [true, true]
        finishedBucket.list.stepsHits == [[1, 1] as int[], [1, 1] as int[]]
        where:
        featureString = """
            Feature: Client registration
              Scenario: Client is registered with account
                When: client with id <clientId> is registered
                Then: account <accountId> is created for client <clientId>
        """
        log = """
        2019-09-24 INFO client with id 1000 is registered
        2019-09-24 INFO account 40 is created for client 1000
        2019-09-24 INFO client with id 1001 is registered
        2019-09-24 INFO account 41 is created for client 1001
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
