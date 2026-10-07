import framework.project2.ConstExprResult;
import framework.project2.ErrorReporter;
import framework.project2.Grader;
import framework.project2.Project2ErrorListener;
import framework.project2.Project2ErrorStrategy;
import generated.SplcLexer;
import generated.SplcParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);

        ErrorReporter reporter = new ErrorReporter();

        SplcLexer lexer = new SplcLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new Project2ErrorListener(reporter));

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SplcParser parser = new SplcParser(tokens);

        parser.setErrorReporter(reporter);
        parser.setErrorHandler(new Project2ErrorStrategy(reporter));
        parser.removeErrorListeners();
        parser.addErrorListener(new Project2ErrorListener(reporter));

        ParseTree tree = parser.program();

        List<ConstExprResult> results = Collections.emptyList();
        if (!reporter.hasError()) {
            ConstExprVisitor visitor = new ConstExprVisitor();
            visitor.visit(tree);
            results = visitor.getResults();
        }

        Grader.print(parser, tree, reporter, results);
    }
}
