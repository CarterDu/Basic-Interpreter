package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AST Interpreter
 */
public class Interpreter{
    private HashMap<String, Integer> integerVariables = new HashMap<String, Integer>();
    private HashMap<String, Float> floatVariables = new HashMap<String, Float>();
    private HashMap<String, String> stringVariables = new HashMap<String, String>();
    private HashMap<String, StatementNode> labelDestination = new HashMap<String, StatementNode>();     //store labels

    private List<Node> list = new ArrayList<>();
    private List<Node> dataElement = new ArrayList<>();   //store data from DataStatement

    public Interpreter(List<Node> list){
        this.list = list;
    }

    /**
     * allowing the visitor pattern over the statements from parser
     * @param nodeList
     */
    public void initialize(List<Node> nodeList){
        extractLabel();
        extractForAndNextStatement();
        extractReadAndDataStatement();
    }

    /**
     * Syntax:
     * FtoC: C = 5*(F-32)/9
     * fetch the labelName and corresponding statement to labelDestination
     */
    public void extractLabel(){
        for(Node n: list){
            if(n instanceof LabelStatementNode){
                String labelName = ((LabelStatementNode) n).getLabelName();
                StatementNode statement = ((LabelStatementNode) n).getLabelStatement();
                labelDestination.put(labelName, statement);
            }
            list.remove(n); //remove the label statement after fetch its content
        }                                       @// TODO: 4/10/21 Am i on the right track for this method? And is this the way to remove?
    }

    /**
     * syntax:
     * 	FOR A = 0 TO 10 STEP 2
     * 	PRINT A
     * 	NEXT A
     * @param
     */
    public void extractForAndNextStatement(){
        boolean forFound = false;
        Node forNode = null;   //store the forNode
        for (int i = 0; i < list.size(); i++) {
            Node currentStatement = list.get(i);
            if(currentStatement instanceof ForNode){
                forFound = true;
                forNode = currentStatement;             @// TODO: 4/10/21 I am not sure how to connect the ForStatement to the statement after nextStatement :(
            }
            else if(currentStatement instanceof NextNode && forFound){
                ((NextNode) currentStatement).setNext(forNode);         @// TODO: 4/10/21 I am not this is the way to linked NextNode to ForNode by doing such?
            }
        }
    }


    /**
     * syntax:
     * DATA 10,”mphipps”
     * READ a, a$
     * 1. process READ first
     */
    public void extractReadAndDataStatement(){
        for (int i = 0; i < list.size(); i++) {
            Node currentStatement = list.get(i);
            if(currentStatement instanceof DataNode){
                for(Node n: ((DataNode) currentStatement).getList())
                    dataElement.add(n);     //store element from dataStatement
                list.remove(currentStatement);  //remove dataStatement from AST
            }
            else if(currentStatement instanceof ReadNode){     @// TODO: 4/10/21 Do i need to set the linked readStatement to DataStatement?
                for(VariableNode varNode: ((ReadNode) currentStatement).getList())
                    for(Node n: dataElement){
                        if(n instanceof StringNode){
                            stringVariables.put(varNode.getValue(), ((StringNode) n).getValue());   //store the value from dataStatement & readStatement
                        }
                    }
                list.remove(currentStatement);  //remove readStatement from AST         
            }
        }
    }           @// TODO: 4/10/21 Am i on the right track in general for this method?

}
