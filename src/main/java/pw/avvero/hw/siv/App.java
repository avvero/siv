package pw.avvero.hw.siv;

import org.apache.commons.lang3.exception.ExceptionUtils;
import pw.avvero.hw.siv.gherkin.Feature;
import pw.avvero.hw.siv.gherkin.Scenario;
import pw.avvero.hw.siv.walker.FeatureWalker;
import pw.avvero.hw.siv.walker.SingleHitScenarioWalkerFactory;
import pw.avvero.hw.siv.walker.Walker;
import pw.avvero.hw.siv.walker.WalkerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class App {
    
    private static ConsoleWriter console = new ConsoleWriter();

    public static void main(String... args) throws Exception {
        console.newLine("App is started with args: " + String.join(", ", args));
        if (args.length == 0) {
            throw new IllegalArgumentException("Please specify feature file");
        }
        String featureFile = args[0];
        console.newLine("Feature file is: " + featureFile);
        Feature feature = new FeatureParser().parseFromFile(featureFile);
        FeatureWriter featureWriter = new FeatureWriter();
        console.newLineBlueBold("------------------------------------------------------------------------------");
        console.newLineBlue(featureWriter.toString(feature));
        console.newLineBlueBold("------------------------------------------------------------------------------");
        WalkerFactory<Scenario> walkerFactory = new SingleHitScenarioWalkerFactory(t -> {}, (t, s) -> {},
                t -> {
                    console.newLineGreenBold(String.format("FINISHED in %s second(s):", secondsBetween(
                            t.getFinishedDate(), t.getStartedDate())));
                    console.newLineGreen(featureWriter.toString(t.getScenario(), t.getContext()));
                });
        Walker walker = new FeatureWalker(feature, walkerFactory);
        final AtomicLong linePassed = new AtomicLong();
        final AtomicLong lastAffectionTimeNanos = new AtomicLong();
        new Progress(p -> {
            console.bottomLine(String.format(">Line passed: %s, last flow impact: %s nanos", linePassed.get(),
                    lastAffectionTimeNanos));
        }, 100);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (true) {
                if ((line = reader.readLine()) != null) {
                    linePassed.incrementAndGet();
                    long startTime = System.nanoTime();
                    walker.pass(line);
                    long timeElapsed = System.nanoTime() - startTime;
                    lastAffectionTimeNanos.set(timeElapsed);
                } else {
                    //input finishes
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(ExceptionUtils.getStackTrace(e));
        }
    }

    private static int secondsBetween(Date finishedDate, Date startedDate) {
        return (int) ((finishedDate.getTime() - startedDate.getTime()) / 1000);
    }

}
