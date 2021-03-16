package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Syntax:
 * READ a $a
 */
public class ReadNode extends StatementNode{

    private List<VariableNode> list;

    public ReadNode(List<VariableNode> list){
        this.list = list;
    }


    @Override
    public String toString() {
        return "ReadNode(" + Arrays.toString(list.toArray()) + ")";
    }

    public List<VariableNode> getList() {
        return list;
    }
}
