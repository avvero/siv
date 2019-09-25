package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Sentence;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceMatcher {

    public static class Result {
        public boolean matches;
        public Map<String, String> attributes = Collections.emptyMap();

        public Result(boolean matches) {
            this.matches = matches;
        }
    }

    private static final SentenceMatcher sentenceMatcher = new SentenceMatcher();

    public static SentenceMatcher getInstance() {
        return sentenceMatcher;
    }

    public Result match(Sentence sentence, String s) {
        Matcher matcher = sentence.getPattern().matcher(s);
        return new Result(matcher.find());
    }

    public Result match(Sentence sentence, String s, Map<String, String> context) {
        if (sentence.getVariables().size() == 0) {
            return match(sentence, s);
        }
        if (context.size() > 0) {
            String patternString = sentence.getOriginal();
            for (String variable : sentence.getVariables()) {
                String contextValue = context.get(variable);
                String subst = contextValue != null ? contextValue : "\\w+";
                patternString = patternString.replace(variable, subst);
            }
            Matcher matcher = Pattern.compile(patternString).matcher(s);
            // TODO extract variables
            return new Result(matcher.find());
        } else {
            Matcher matcher = sentence.getPattern().matcher(s);
            // TODO extract variables
            return new Result(matcher.find());
        }
    }

    public Map<String, String> extractVariables(String string, String pattern) {
        return Collections.emptyMap();
    }

}
