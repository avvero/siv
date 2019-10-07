package pw.avvero.hw.jpipe;

import java.util.Date;
import java.util.function.Consumer;

public class Progress {

    private Consumer<Progress> consumer;
    private Date started = new Date();

    public Progress(Consumer<Progress> consumer, int stepMillis) {
        this.consumer = consumer;
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

    public Date getStarted() {
        return started;
    }
}
