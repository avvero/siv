package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Feature;
import pw.avvero.hw.jpipe.gherkin.Scenario;

import java.util.List;
import java.util.stream.Collectors;

public class FeatureWalker implements Walker<Feature> {

    private List<Walker> walkers;

    public FeatureWalker(Feature feature, WalkerFactory<Scenario> walkerFactory) {
        walkers = feature.getScenarios().stream()
                .map(walkerFactory::getInstance)
                .collect(Collectors.toList());
    }

    public void pass(String s) {
        walkers.forEach(w -> w.pass(s));
    }
}
