package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hold a list of nodes
 */
public class PrintNode extends StatementNode {
    private List<Node> nodeList = null;

    public PrintNode(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    @Override
    public String toString() {
        return "Print" + Arrays.toString(nodeList.toArray());
    }
}


