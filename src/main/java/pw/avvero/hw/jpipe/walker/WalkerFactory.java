package pw.avvero.hw.jpipe.walker;

public interface WalkerFactory<T> {

    Walker<T> getInstance(T t);

}
