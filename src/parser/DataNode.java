package parser;

import java.util.Arrays;
import java.util.List;

/**
 * DATA takes a list of STRING, IntegerNode and FloatNode.
 * Syntax:
 * DATA 10(NumberNode) "WORD"(StringNode)
 */
public class DataNode extends StatementNode{

    private List<Node> list;

    public DataNode(List<Node> list){
        this.list = list;
    }

    public List<Node> getList(){
        return list;
    }

    @Override
    public String toString() {
        return "DataNode(" + Arrays.toString(list.toArray()) + ")";
    }
}
