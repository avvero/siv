package pw.avvero.hw.jpipe.gherkin;

import java.util.ArrayList;
import java.util.List;

public class Feature {

    private Sentence sentence;
    private List<Scenario> scenarios = new ArrayList<>();

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}
