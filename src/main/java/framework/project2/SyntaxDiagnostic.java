package framework.project2;

public record SyntaxDiagnostic(
        ErrorKind kind,
        int tokenType,
        int line,
        String message) {
}
