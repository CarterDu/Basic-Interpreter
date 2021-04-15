package parser;

/**
 * Syntax:
 * FOR A = 0 TO 10 STEP 2
 * 	PRINT A
 * 	NEXT A
 */
public class NextNode extends StatementNode{

    private VariableNode nextElement;
    public Node matchForLoop;


    /**
     * Point to the for statement
     * @param next
     */
    public void setNextStatementMatchedToForStatement(Node next) {
        this.matchForLoop = next;
    }

    public NextNode(VariableNode nextElement){
        this.nextElement = nextElement;
    }

    public VariableNode getVariableNode(){
        return nextElement;
    }

    public Node getNextForStatement() { return matchForLoop; }


    @Override
    public String toString() {
        return "NextNode(" + nextElement + ")";

    }
}
