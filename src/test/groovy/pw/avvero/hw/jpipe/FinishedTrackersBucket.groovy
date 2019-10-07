package pw.avvero.hw.jpipe


import java.util.function.Consumer

class FinishedTrackersBucket<T> implements Consumer<T> {
    List<T> list = new ArrayList<>()
    @Override
    void accept(T scenarioTracker) {
        list << scenarioTracker
    }
}