package parser;

import java.util.Arrays;
import java.util.List;

/**
 * Syntax:
 * DATA 10(NumberNode) "WORD"(StringNode)
 */
public class DataNode extends StatementNode{

    private List<Node> list = null;

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
