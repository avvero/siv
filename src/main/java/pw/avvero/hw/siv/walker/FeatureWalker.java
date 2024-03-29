package pw.avvero.hw.siv.walker;

import pw.avvero.hw.siv.gherkin.Feature;
import pw.avvero.hw.siv.gherkin.Scenario;

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
