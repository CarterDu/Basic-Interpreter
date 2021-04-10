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


    public Node getNext() {
        return matchForLoop;
    }

    /**
     * Point to the for statement
     * @param next
     */
    public void setNext(Node next) {
        this.matchForLoop = matchForLoop;
    }

    public NextNode(VariableNode nextElement){
        this.nextElement = nextElement;
    }

    public VariableNode getNextElement(){
        return nextElement;
    }

    @Override
    public String toString() {
        return "NextNode(" + nextElement + ")";

    }
}
