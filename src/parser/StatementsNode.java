package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * holds a list of StatementNode (the statements, right now either a print or an assignment)
 */
public class StatementsNode extends Node{
    private List<Node> statementNodeList = null;

    public StatementsNode(List<Node> statementNodeList){
        this.statementNodeList = statementNodeList;
    }


    @Override
    public String toString() {
        return this.statementNodeList + "";
    }
}
