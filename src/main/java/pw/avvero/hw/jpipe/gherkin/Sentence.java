package pw.avvero.hw.jpipe.gherkin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Sentence {

    private List<SentenceChunk> chunks = new ArrayList<>();

    public String getOriginal() {
        String value = chunks.stream()
                .map(it -> it instanceof Variable ? "<" + it.value + ">" : it.value)
                .map(Objects::toString)
                .collect(Collectors.joining());
        return value;
    }

    public Pattern getPattern() {
        String patternValue = chunks.stream()
                .map(it -> it instanceof Variable ? "(?<" + it.value + ">\\w+)" : it.value)
                .collect(Collectors.joining());
        return Pattern.compile(patternValue);
    }

    public Pattern getPattern(Map<String, String> context) {
        String patternValue = chunks.stream()
                .map(it -> {
                    if (it instanceof Variable) {
                        String value = context.get(it.value);
                        return value != null ? value : "(?<" + it.value + ">\\w+)";
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

}
