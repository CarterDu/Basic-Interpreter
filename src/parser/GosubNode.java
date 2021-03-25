package parser;

/**
 * Syntax:
 * GoSub label
 */
public class GosubNode extends StatementNode{
    private VariableNode label;

    public GosubNode(VariableNode label) {
        this.label = label;
    }
    public VariableNode getLabel() {
        return label;
    }

    public String toString(){
        return "GoSubNode(" + label + ")";
    }
}
