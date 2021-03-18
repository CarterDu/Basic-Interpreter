package parser;

import java.util.Arrays;
import java.util.List;

/**
 * Syntax:
 * INPUT "How old are you?" age$
 */
public class InputNode extends StatementNode{
    private StringNode stringNode;
    private VariableNode varNode;
    private List<VariableNode> list;

    public InputNode(StringNode strNode, List<VariableNode> list){
        this.stringNode = strNode;
        this.list = list;
    }

    public InputNode(VariableNode varNode, List<VariableNode> list){
        this.varNode = varNode;
        this.list = list;
    }


    @Override
    public String toString() {
        return "InputNode(" + stringNode + ", " + Arrays.toString(list.toArray())  + ")";
    }

    public StringNode getStringNode() {
        return stringNode;
    }

    public List<VariableNode> getList() {
        return list;
    }

    public VariableNode getVarNode(){
        return varNode;
    }


}
