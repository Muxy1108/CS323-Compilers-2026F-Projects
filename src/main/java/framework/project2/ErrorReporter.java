package framework.project2;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ErrorReporter {

    private final List<SyntaxDiagnostic> diagnostics = new ArrayList<>();

    private void add(SyntaxDiagnostic diagnostic) {
        // Project 2 guarantees at most one source-level syntax error per testcase.
        // Keep only the first diagnostic so recovery does not produce cascaded output.
        if (diagnostics.isEmpty()) {
            diagnostics.add(diagnostic);
        }
    }

    /**
     * Report a missing token.
     *
     * @param tokenType symbolic token type, e.g. SplcParser.SEMI
     * @param line      0-indexed line defined by the Project 2 specification
     */
    public void reportMissing(int tokenType, int line) {
        add(new SyntaxDiagnostic(
                ErrorKind.MISSING,
                tokenType,
                Math.max(0, line),
                null));
    }

    /**
     * Report an extraneous token.
     *
     * @param tokenType symbolic token type of the actual offending token
     * @param line      0-indexed line of that token
     */
    public void reportExtraneous(int tokenType, int line) {
        add(new SyntaxDiagnostic(
                ErrorKind.EXTRANEOUS,
                tokenType,
                Math.max(0, line),
                null));
    }

    /**
     * Fallback path for lexer errors or parser errors not covered by the
     * Project 2 missing/extraneous-token contract.
     *
     * ANTLR error-listener line numbers are 1-indexed.
     */
    public void reportOther(int oneBasedLine, String message) {
        add(new SyntaxDiagnostic(
                ErrorKind.OTHER,
                Token.INVALID_TYPE,
                Math.max(0, oneBasedLine - 1),
                message));
    }

    public boolean hasError() {
        return !diagnostics.isEmpty();
    }

    public List<SyntaxDiagnostic> getDiagnostics() {
        return Collections.unmodifiableList(diagnostics);
    }

    public void print(Vocabulary vocabulary) {
        for (SyntaxDiagnostic diagnostic : diagnostics) {
            switch (diagnostic.kind()) {
                case MISSING -> System.out.printf(
                        "Missing Symbol '%s' at Line %d%n",
                        tokenName(vocabulary, diagnostic.tokenType()),
                        diagnostic.line());
                case EXTRANEOUS -> System.out.printf(
                        "Extraneous Symbol '%s' at Line %d%n",
                        tokenName(vocabulary, diagnostic.tokenType()),
                        diagnostic.line());
                case OTHER -> System.out.printf(
                        "Syntax Error at Line %d: %s%n",
                        diagnostic.line(),
                        diagnostic.message());
            }
        }
    }

    private static String tokenName(Vocabulary vocabulary, int tokenType) {
        String symbolic = vocabulary.getSymbolicName(tokenType);
        return symbolic != null ? symbolic : vocabulary.getDisplayName(tokenType);
    }
}
