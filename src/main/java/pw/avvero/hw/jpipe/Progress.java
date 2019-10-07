package pw.avvero.hw.jpipe;

import java.util.function.Consumer;

public class Progress {

    public Progress(Consumer<Progress> consumer, int stepMillis) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(stepMillis);
                } catch (InterruptedException ignored) {}
                consumer.accept(this);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
