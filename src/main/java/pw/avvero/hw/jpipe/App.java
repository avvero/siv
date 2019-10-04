package pw.avvero.hw.jpipe;

import pw.avvero.hw.jpipe.gherkin.Feature;
import pw.avvero.hw.jpipe.walker.SingleDirectOrderScenarioWalker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

    public static void main(String... args) throws Exception {
        System.out.println("----- jpipe started with args: " + String.join(", ", args));
        if (args.length == 0) {
            throw new Exception("Please specify feature file");
        }
        String featureFile = args[0];
        System.out.println("----- feature file is: " + featureFile);
        Feature feature = new FeatureParser().parseFromFile(featureFile);

        FeatureWriter featureWriter = new FeatureWriter();

        System.out.println("----- feature is: \n" + featureWriter.toString(feature));
        System.out.println("------------------------------------------------------------------------------");
        SingleDirectOrderScenarioWalker walker = new SingleDirectOrderScenarioWalker(feature.getScenarios().get(0),
                t -> {
                    System.out.println("Scenario is started: " + t.getScenario().getSentence().getOriginal());
                },
                (t, s) -> {
                    System.out.println(String.format("Hit sentence: \n    >%s\n    >%s",
                            s.getSentence().getOriginal(), s.getString()));
                },
                t -> {
                    System.out.println("Scenario is finished: \n" + featureWriter.toString(feature, t.getContext()));
                });

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (true) {
                if ((line = reader.readLine()) != null) {
                    walker.pass(line);
//                    System.out.println("echo>> " + line);
                } else {
                    //input finishes
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
