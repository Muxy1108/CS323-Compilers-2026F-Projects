import framework.project2.ConstExprResult;
import generated.SplcBaseVisitor;
import generated.SplcParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstExprVisitor extends SplcBaseVisitor<Integer>{

    private final List<ConstExprResult> results = new ArrayList<>();

    public final List<ConstExprResult> getResults(){
        return Collections.unmodifiableList(results);
    }

    protected final void addResult(String expression, int value){
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

    @Override
    public Integer visitLocalVarDef(SplcParser.LocalVarDefContext ctx){
        if(ctx.expression() == null){
            return null;
        }

        Integer value = visit(ctx.expression());
        if(value != null){
            addResult(ctx.expression().getText(), value);
        }
        return null;
    }



    @Override
    public Integer visitExpression(SplcParser.ExpressionContext ctx){

        // number
        if(ctx.Number() != null){
            return Integer.parseInt(ctx.Number().getText());
        }

        // (expression)
        if(ctx.getChildCount() == 3
                && ctx.getChild(0).getText().equals("(")
                && ctx.getChild(2).getText().equals(")")) {
            return visit(ctx.expression(0));
        }

        // (+|-) expression
        if(ctx.getChildCount() == 2){
            String op = ctx.getChild(0).getText();

            if(!op.equals("+") && !op.equals("-")){
                return null;
            }

            Integer operand = visit(ctx.expression(0));

            if(operand == null){
                return null;
            }

            return op.equals("+") ? operand : -operand;
        }

        // expression (+|-|*|/|%) expression
        if(ctx.getChildCount() == 3 && ctx.expression().size() == 2){
            String op = ctx.getChild(1).getText();

            if(!op.equals("+") && !op.equals("-")
                    && !op.equals("*") && !op.equals("/")
                    && !op.equals("%")){
                return null;
            }

            Integer left = visit(ctx.expression(0));
            Integer right = visit(ctx.expression(1));

            if(left == null || right == null){
                return null;
            }

            switch(op){
                case "+": return left + right;
                case "-": return left - right;
                case "*": return left * right;
                case "/": return left / right;
                case "%": return left % right;
            }
        }
        return null;
    }
}
