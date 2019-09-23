package pw.avvero.hw.jpipe.gherkin;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private String label;
    private List<Step> steps = new ArrayList<>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
