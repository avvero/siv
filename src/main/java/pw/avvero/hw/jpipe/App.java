package pw.avvero.hw.jpipe;

import pw.avvero.hw.jpipe.gherkin.Feature;
import pw.avvero.hw.jpipe.walker.SingleDirectOrderScenarioWalker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    
    private static ConsoleWriter console = new ConsoleWriter();

    public static void main(String... args) throws Exception {
        console.common("App is started with args: " + String.join(", ", args));
        if (args.length == 0) {
            throw new Exception("Please specify feature file");
        }
        String featureFile = args[0];
        console.common("Feature file is: " + featureFile);
        console.common("");
        Feature feature = new FeatureParser().parseFromFile(featureFile);
        FeatureWriter featureWriter = new FeatureWriter();
        console.blueBold("------------------------------------------------------------------------------");
        console.blue(featureWriter.toString(feature));
        console.blueBold("------------------------------------------------------------------------------");
        SingleDirectOrderScenarioWalker walker = new SingleDirectOrderScenarioWalker(feature.getScenarios().get(0),
                t -> {
//                    console.common("New scenario is started: " + t.getScenario().getSentence().getOriginal());
                },
                (t, s) -> {
//                    console.common(String.format("Hit sentence: \n    >%s\n    >%s",
//                            s.getSentence().getOriginal(), s.getString()));
                },
                t -> {
                    console.greenBold("\nFINISHED:");
                    console.green(featureWriter.toString(t.getScenario(), t.getContext()));
//                    console.greenBold("\n");
                });

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (true) {
                if ((line = reader.readLine()) != null) {
                    walker.pass(line);
//                    console.common("echo>> " + line);
                } else {
                    //input finishes
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        console.common("\n");
    }

}
