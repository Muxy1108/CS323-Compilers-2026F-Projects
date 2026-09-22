import generated.IPV4.IPV4Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class Main {
    public static void main(String[] args) {
        CharStream charStream =
                CharStreams.fromString("257.016.299.233");

        IPV4Lexer lexer =
                new IPV4Lexer(charStream);

        CommonTokenStream tokens =
                new CommonTokenStream(lexer);

        tokens.fill();

        for (Token token : tokens.getTokens()) {
            // 不输出 EOF
            if (token.getType() == Token.EOF) {
                continue;
            }

            String tokenType =
                    lexer.getVocabulary()
                            .getSymbolicName(token.getType());

            System.out.printf(
                    "TokenType: %s, Lexeme: %s%n",
                    tokenType,
                    token.getText()
            );
        }
    }
}