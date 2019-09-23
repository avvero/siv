package pw.avvero.hw.jpipe;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

    public static void main(String ... args) {
        System.out.println("----- jpipe started with args: " + String.join(", ", args));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (true) {
                if ((line = reader.readLine()) != null) {
                    System.out.println("echo>> " + line);
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
