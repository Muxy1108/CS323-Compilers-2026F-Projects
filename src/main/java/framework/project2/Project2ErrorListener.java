package framework.project2;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public final class Project2ErrorListener extends BaseErrorListener {

    private final ErrorReporter reporter;

    public Project2ErrorListener(ErrorReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {

        if (!reporter.hasError()) {
            reporter.reportOther(line, msg);
        }
    }
}
