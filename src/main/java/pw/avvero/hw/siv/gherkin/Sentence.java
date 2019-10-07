package pw.avvero.hw.siv.gherkin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Sentence {

    private static final String VARIABLE_T = "<%s>";
    private static final String VARIABLE_P = "(?<%s>[\\w-]+)";

    private List<SentenceChunk> chunks = new ArrayList<>();

    public String getOriginal() {
        String value = chunks.stream()
                .map(it -> it instanceof Variable ? format(VARIABLE_T, it.value) : it.value)
                .map(Objects::toString)
                .collect(Collectors.joining());
        return value;
    }

    public String getOriginal(Map<String, String> context) {
        if (context == null || context.size() == 0) return getOriginal();

        String value = chunks.stream()
                .map(it -> it instanceof Variable
                        ? context.getOrDefault(it.value, format(VARIABLE_T, it.value)) : it.value)
                .map(Objects::toString)
                .collect(Collectors.joining());
        return value;
    }

    public Pattern getPattern() {
        String patternValue = chunks.stream()
                .map(it -> it instanceof Variable ? format(VARIABLE_P, it.value) : it.value)
                .collect(Collectors.joining());
        return Pattern.compile(patternValue);
    }

    public Pattern getPattern(Map<String, String> context) {
        String patternValue = chunks.stream()
                .map(it -> {
                    if (it instanceof Variable) {
                        String value = context.get(it.value);
                        return value != null ? value : format(VARIABLE_P, it.value);
                    } else if (it instanceof Sign) {
                        return "\\" + it.value;
                    } else if (it instanceof Space) {
                        return "\\s+";
                    } else {
                        return it.value;
                    }
                })
                .collect(Collectors.joining());
        return Pattern.compile(patternValue);
    }

    public List<SentenceChunk> getVariables() {
        return chunks.stream().filter(it -> it instanceof Variable).collect(Collectors.toList());
    }

    public List<SentenceChunk> getChunks() {
        return chunks;
    }

    @Override
    public String toString() {
        return "Sentence{" + "original=" + getOriginal() + ", pattern=" + getPattern() + '}';
    }
}
