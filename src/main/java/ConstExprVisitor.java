import framework.project2.ConstExprResult;
import generated.SplcBaseVisitor;
import generated.SplcParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstExprVisitor extends SplcBaseVisitor<Integer> {

    private final List<ConstExprResult> results = new ArrayList<>();

    public final List<ConstExprResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    protected final void addResult(String expression, int value) {
        results.add(new ConstExprResult(expression, value));
    }

    /*
     * TODO:
     *
     * 1. Find the complete expression on the right-hand side of each local
     *    variable definition.
     * 2. Determine whether that complete expression is a constexpr according
     *    to the Project 2 specification.
     * 3. Evaluate it if it is a constexpr.
     * 4. Store the result with addResult(...).
     *
     * Do not add constexpr subexpressions of a non-constexpr whole expression.
     */
}
