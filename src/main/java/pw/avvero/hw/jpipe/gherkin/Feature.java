package pw.avvero.hw.jpipe.gherkin;

import java.util.ArrayList;
import java.util.List;

public class Feature {

    private String label;
    private List<Scenario> scenarios = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}
