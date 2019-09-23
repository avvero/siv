package pw.avvero.hw.jpipe;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import pw.avvero.hw.jpipe.antlr.HelloLexer;
import pw.avvero.hw.jpipe.antlr.HelloParser;
import pw.avvero.hw.jpipe.antlr.HelloWalker;

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

                    HelloLexer lexer = new HelloLexer(CharStreams.fromString("hello world"));
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    HelloParser parser = new HelloParser(tokens);
                    ParseTree tree = parser.r();
                    ParseTreeWalker walker = new ParseTreeWalker();
                    walker.walk(new HelloWalker(), tree);
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
