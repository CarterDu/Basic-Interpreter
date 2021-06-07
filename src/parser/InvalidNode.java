package parser;

/**
 * Represent the invalid node
 * This node will not be used for the program
 * Similiar to Exception
 */
public class InvalidNode extends StatementNode{
    private String message;
    public InvalidNode(){

    }
    public InvalidNode(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return "ERROR MESSAGE: " + message;
    }
}
