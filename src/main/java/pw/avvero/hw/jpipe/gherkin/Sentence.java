package pw.avvero.hw.jpipe.gherkin;

import java.util.List;
import java.util.regex.Pattern;

public class Sentence {

    private String original;
    private Pattern pattern;
    private List<String> variables;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }
}
