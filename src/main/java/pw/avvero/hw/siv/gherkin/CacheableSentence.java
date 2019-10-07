package pw.avvero.hw.siv.gherkin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Sentence heir who caches values that calculates in original one
 */
public class CacheableSentence extends Sentence {

    private static final String EMPTY = "EMPTY";
    private Map<String, String> originalCache = new HashMap<>();
    private Map<String, Pattern> patternCache = new HashMap<>();
    private List<SentenceChunk> variablesCache;

    public String getOriginal() {
        return originalCache.computeIfAbsent(EMPTY, s -> super.getOriginal());
    }

    public String getOriginal(Map<String, String> context) {
        if (context == null || context.size() == 0) return getOriginal();

        String cacheKey = getCacheKeyFrom(context);
        return originalCache.computeIfAbsent(cacheKey, s-> super.getOriginal(context));
    }

    public Pattern getPattern() {
        return patternCache.computeIfAbsent(EMPTY, s-> super.getPattern());
    }

    public Pattern getPattern(Map<String, String> context) {
        String cacheKey = getCacheKeyFrom(context);
        return patternCache.computeIfAbsent(cacheKey, s -> super.getPattern(context));
    }

    public List<SentenceChunk> getVariables() {
        if (variablesCache == null) {
            variablesCache = super.getVariables();
        }
        return variablesCache;
    }

    private String getCacheKeyFrom(Map<String, String> map) {
        return map.entrySet().stream().map(e -> e.getKey() + "_" + e.getValue()).collect(Collectors.joining("_"));
    }

    @Override
    public String toString() {
        return "Sentence{" + "original=" + getOriginal() + ", pattern=" + getPattern() + '}';
    }
}
