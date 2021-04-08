package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AST Interpreter
 */
public class Interpreter{
    private HashMap<String, Integer> intBox = new HashMap<String, Integer>();
    private HashMap<String, Float> floatBox = new HashMap<String, Float>();
    private HashMap<String, String> strBox = new HashMap<String, String>();
    private HashMap<String, StatementNode> labelBox = new HashMap<String, StatementNode>();     //store labels

    public Interpreter(){
    }

    /**
     * allowing the visitor pattern over the statements from parser
     * @param nodeList
     */
    public void initialize(List<Node> nodeList){
        for (int i = 0; i < nodeList.size(); i++) {
            extractReadAndDataStatement(nodeList.get(i));
            extractLabel(nodeList.get(i));
            extractForAndNextStatement(nodeList.get(i), nodeList.get(i+1));
        }
    }

    /**
     * Syntax:
     * FtoC: C = 5*(F-32)/9
     * fetch the labelName and corresponding statement to labelBox
     */
    public void extractLabel(Node node){
        if(node.getClass().equals(LabelStatementNode.class)){
            String labelName = ((LabelStatementNode) node).getLabelName();
            StatementNode statement = ((LabelStatementNode) node).getLabelStatement();
            labelBox.put(labelName, statement);
        }
    }

    /**
     * syntax:
     * 	FOR A = 0 TO 10 STEP 2
     * 	PRINT A
     * 	NEXT A
     * @param node
     */
    public void extractForAndNextStatement(Node node, Node nextNode){
        boolean isForStatement = false;
        NextNode next;
        //check for next Statement and point to the next Statement
        if(node.getClass().equals(ForNode.class)){
            isForStatement = true;
            ForNode forNode = (ForNode) node;
            String varName = forNode.getName().getValue();
            int beginValue = forNode.getBegin();
            int endValue = forNode.getEnd();
            int stepValue = forNode.getStepValue(); //default is 1
            intBox.put(varName, beginValue); //store the value of the forVariable (the default value)
            beginValue += stepValue;
            if(beginValue == endValue){ //terminate the loop and point to the next statement
                forNode.setNextStatement(nextNode); //linked
            }
        }
        if(isForStatement && nextNode.getClass().equals(NextNode.class)){  //process the next statement
            next = (NextNode) nextNode;
            String varName = next.getNextElement().getValue();
            next.setNext(node);
        }

    }


    /**
     * syntax:
     * DATA 10,”mphipps”
     * READ a, a$
     * 1. process READ first
     */
    public void extractReadAndDataStatement(Node node) {
        List<Node> dataElement = new ArrayList<>();
        List<Node> readElement = new ArrayList<>();
        if(node.getClass().equals(ReadNode.class)){
            ReadNode readNode = (ReadNode) node;
            for(Node n: readNode.getList())
                readElement.add(n); //store elements from ReadStatement
        }

        if(node.getClass() == DataNode.class){
            for(Node n: ((DataNode) node).getList())
                dataElement.add(n);
        }
    }

}
