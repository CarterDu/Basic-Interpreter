package parser;

/**
 * Currently either Print or Assign
 */
public class StatementNode extends Node{
    public StatementNode next; //linked to the next statement

    public StatementNode getTheNextStatement() {
        return next;
    }

    public void setTheNextStatement(StatementNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return null;
    }
}
