package framework.project2;

import generated.SplcParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public final class Grader {

    private Grader() {
    }

    public static void print(
            SplcParser parser,
            ParseTree tree,
            ErrorReporter reporter,
            List<ConstExprResult> results) {

        if (reporter.hasError()) {
            reporter.print(parser.getVocabulary());
            return;
        }

        System.out.println(tree.toStringTree(parser));

        for (ConstExprResult result : results) {
            System.out.printf(
                    "Expression: `%s` evaluates to %d%n",
                    result.expression(),
                    result.value());
        }
    }
}
