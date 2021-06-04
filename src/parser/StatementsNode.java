package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * holds a list of StatementNode (the statements, right now either a print or an assignment)
 */
public class StatementsNode extends Node{
    private List<StatementNode> statementNodeList = null;

    public StatementsNode(List<StatementNode> statementNodeList){
        this.statementNodeList = statementNodeList;
    }

    public List<StatementNode> getStatementNodeList() {
        return statementNodeList;
    }

    @Override
    public String toString() {
        return statementNodeList + "";
    }
}
