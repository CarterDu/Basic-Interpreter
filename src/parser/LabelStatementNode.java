package parser;

/**
 * Syntax:
 * goto: c=4+9i
 */
public class LabelStatementNode extends StatementNode{
    private String labelName;
    private StatementNode labelStatement;   //reference of statement

    public LabelStatementNode(String labelName, StatementNode labelStatement) {
        this.labelName = labelName;
        this.labelStatement = labelStatement;
    }

    public String getLabelName() {
        return labelName;
    }

    public StatementNode getLabelStatement() {
        return labelStatement;
    }

    //toString
    public String toString(){
        return "LabelNode(" + labelName + ", " + labelStatement + ")";
    }
}
