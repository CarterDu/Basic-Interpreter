package parser;

/**
 * syntax:
 * IF x<5 THEN xIsSmall
 */
public class IfNode extends StatementNode{
    private BooleanOperationNode booleanOperationNode;
    private String labelName;

    public IfNode(BooleanOperationNode booleanOperationNode, String labelName) {
        this.booleanOperationNode = booleanOperationNode;
        this.labelName = labelName;
    }

    public BooleanOperationNode getBooleanOperationNode() {
        return booleanOperationNode;
    }

    public String getLabelName() {
        return labelName;
    }

    public String toString(){
        return "IF " + booleanOperationNode + " THEN " + labelName + ")";
    }
}
