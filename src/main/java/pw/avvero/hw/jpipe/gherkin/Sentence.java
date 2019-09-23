package pw.avvero.hw.jpipe.gherkin;

import java.util.List;

public class Sentence {

    private String original;
    private List<String> variables;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }
}
