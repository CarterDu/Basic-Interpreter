package parser;

import java.util.Arrays;
import java.util.List;

/**
 * Syntax:
 * INPUT "How old are you?" age$
 */
public class InputNode extends StatementNode{
    private StringNode stringNode;
    private List<VariableNode> list;

    public InputNode(StringNode strNode, List<VariableNode> list){
        this.stringNode = strNode;
        this.list = list;
    }

    //constructor for var and list<Var>



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


}
