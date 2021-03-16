package parser;

/**
 * The parent node of AST
 */
public class MathOpNode extends Node{

    private Node left;
    private Node right;
    private Opcode opcode;

    /**
     * opcode for mathopNode
     */
    public enum Opcode{
        ADD("+"), MINUS("-"), MULTI("*"), DIVIDE("/");

        private String value;

        private Opcode(String value){
            this.value = value;
        }
        private String toOpValue(){
            return value;
        }
    }

    public MathOpNode(){}

    public MathOpNode(Opcode opcode, Node left, Node right){
        this.left = left;
        this.right = right;
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return "MathOp(" + opcode.toOpValue() + ", " + left + ", " + right + ")";
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Opcode getOpcode() {
        return opcode;
    }


}
