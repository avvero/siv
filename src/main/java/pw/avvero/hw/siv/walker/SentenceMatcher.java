package pw.avvero.hw.siv.walker;

import pw.avvero.hw.siv.gherkin.Sentence;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Matcher for the sentence
 */
public class SentenceMatcher {

    private static final SentenceMatcher sentenceMatcher = new SentenceMatcher();

    public static SentenceMatcher getInstance() {
        return sentenceMatcher;
    }

    /**
     * Matches sentence with the string for context
     * @param sentence
     * @param string
     * @param context
     * @return
     */
    public SentenceMatcherResult match(Sentence sentence, String string, Map<String, String> context) {
        Matcher matcher = sentence.getPattern(context).matcher(string);
        if (!matcher.find()) return new SentenceMatcherResult(sentence, string, false);

        SentenceMatcherResult result = new SentenceMatcherResult(sentence, string, true);
        if (sentence.getVariables() != null && sentence.getVariables().size() > 0) {
            sentence.getVariables().forEach(v -> {
                if (context.get(v.getValue()) == null) {
                    String value = matcher.group(v.getValue());
                    result.getAttributes().put(v.getValue(), value);
                }
            });
        }
        return result;
    }

    public SentenceMatcherResult match(Sentence sentence, String string) {
        return match(sentence, string, Collections.emptyMap());
    }
}
