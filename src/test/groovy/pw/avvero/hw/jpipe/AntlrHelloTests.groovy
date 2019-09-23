package pw.avvero.hw.jpipe

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import pw.avvero.hw.jpipe.antlr.HelloLexer
import pw.avvero.hw.jpipe.antlr.HelloParser
import pw.avvero.hw.jpipe.antlr.HelloWalker
import spock.lang.Specification

class AntlrHelloTests extends Specification {

    def "Hello test"() {
        expect:
        HelloLexer lexer = new HelloLexer(CharStreams.fromString("hello world"))
        CommonTokenStream tokens = new CommonTokenStream(lexer)
        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.r();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new HelloWalker(), tree);

    }

}
