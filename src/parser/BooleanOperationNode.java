package parser;

/**
 * syntax:
 * IF x<5 || x<y THEN xIsSmall
 * Expression Operator Expression
 */
public class BooleanOperationNode extends Node{
    private Node expr1, expr2;
    private Token.Type symbol;

    public BooleanOperationNode(Node expr1, Token.Type symbol, Node expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.symbol = symbol;
    }

    public Node getExpr1() {
        return expr1;
    }

    public Node getExpr2() {
        return expr2;
    }

    public Token.Type getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return expr1 + " " + symbol + " " + expr2;
    }
}
