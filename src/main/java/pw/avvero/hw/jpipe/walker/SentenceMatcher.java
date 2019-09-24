package pw.avvero.hw.jpipe.walker;

import pw.avvero.hw.jpipe.gherkin.Sentence;

import java.util.regex.Matcher;

public class SentenceMatcher {

    private static final SentenceMatcher sentenceMatcher = new SentenceMatcher();

    public static SentenceMatcher getInstance() {
        return sentenceMatcher;
    }

    public boolean matches(Sentence sentence, String s) {
        Matcher matcher = sentence.getPattern().matcher(s);
        return matcher.find();
    }

}
