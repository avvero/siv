package pw.avvero.hw.siv.walker;

public interface WalkerFactory<T> {

    Walker<T> getInstance(T t);

}
