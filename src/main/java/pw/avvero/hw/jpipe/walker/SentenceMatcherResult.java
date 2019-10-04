package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Sentence;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains result data after matching
 */
public class SentenceMatcherResult {
    private final Sentence sentence;
    private final String string;

    private final boolean matches;
    private final Map<String, String> attributes = new HashMap<>();

    public SentenceMatcherResult(Sentence sentence, String string, boolean matches) {
        this.sentence = sentence;
        this.string = string;

        this.matches = matches;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public String getString() {
        return string;
    }

    public boolean isMatches() {
        return matches;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
