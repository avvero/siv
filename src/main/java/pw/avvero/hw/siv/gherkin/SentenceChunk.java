package pw.avvero.hw.siv.gherkin;

import java.util.Objects;

public class SentenceChunk {

    protected String value;

    public SentenceChunk(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SentenceChunk)) return false;
        SentenceChunk that = (SentenceChunk) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
