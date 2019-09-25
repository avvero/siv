package pw.avvero.hw.jpipe

import pw.avvero.hw.jpipe.walker.ScenarioTracker
import spock.lang.Specification

class ScenarioTrackerTests extends Specification {

    def "Scenario is completed successfully with single case without context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def tracker = new ScenarioTracker(feature.scenarios.first())
        log.split("\n").each { l -> tracker.hit(l, new NopConsumer(), new NopConsumer())}
        then:
        tracker.completed
        tracker.stepsHits == [1, 1] as int[]
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
        def tracker = new ScenarioTracker(feature.scenarios.first())
        log.split("\n").each { l -> tracker.hit(l, new NopConsumer(), new NopConsumer())}
        then:
        !tracker.completed
        tracker.stepsHits == [1, 0] as int[]
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
        def tracker = new ScenarioTracker(feature.scenarios.first())
        log.split("\n").each { l -> tracker.hit(l, new NopConsumer(), new NopConsumer())}
        then:
        !tracker.completed
        tracker.stepsHits == [0, 0] as int[]
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
        def tracker = new ScenarioTracker(feature.scenarios.first())
        log.split("\n").each { l -> tracker.hit(l, new NopConsumer(), new NopConsumer())}
        then:
        !tracker.completed
        tracker.stepsHits == [1, 0] as int[]
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

    def "Scenario is completed successfully with double case without context"() {
        when:
        def feature = new FeatureParser().parseFromString(featureString)
        def tracker = new ScenarioTracker(feature.scenarios.first())
        log.split("\n").each { l -> tracker.hit(l, new NopConsumer(), new NopConsumer())}
        then:
        tracker.completed
        tracker.stepsHits == [1, 1] as int[]
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

}
