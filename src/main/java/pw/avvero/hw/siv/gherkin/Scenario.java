package pw.avvero.hw.siv.gherkin;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private Sentence sentence;
    private List<Step> steps = new ArrayList<>();

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
