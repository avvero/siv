package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Sentence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class SentenceMatcher {

    public static class Result {
        public boolean matches;
        public Map<String, String> attributes = new HashMap<>();

        public Result(boolean matches) {
            this.matches = matches;
        }

        public Result(boolean matches, Map<String, String> attributes) {
            this.matches = matches;
            this.attributes = attributes;
        }
    }

    private static final SentenceMatcher sentenceMatcher = new SentenceMatcher();

    public static SentenceMatcher getInstance() {
        return sentenceMatcher;
    }

    public Result match(Sentence sentence, String string, Map<String, String> context) {
        Matcher matcher = sentence.getPattern(context).matcher(string);
        if (!matcher.find()) return new Result(false);

        Result result = new Result(true);
        if (sentence.getVariables() != null && sentence.getVariables().size() > 0) {
            sentence.getVariables().forEach(v -> {
                if (context.get(v.getValue()) == null) {
                    String value = matcher.group(v.getValue());
                    result.attributes.put(v.getValue(), value);
                }
            });
        }
        return result;
    }

    public Result match(Sentence sentence, String string) {
        return match(sentence, string, Collections.emptyMap());
    }
}
