package parser;

/**
 * Format: a=b+1 => AssignmentNode(a, MathOpNode(+, b, 1))
 */
public class AssignmentNode extends StatementNode{
    private VariableNode varNode;
    private Node node;

    public AssignmentNode(VariableNode varNode, Node node){
        this.varNode = varNode;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString(){
        return "AssignmentNode(" + varNode + "," + node + ")";
    }
}
