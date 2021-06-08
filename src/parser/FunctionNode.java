package parser;

import java.util.Arrays;
import java.util.List;

/**
 * Syntax:
 * FX(int, int)
 */
public class FunctionNode extends Node{
    private String functionName;
    private List<Node> paramList;

    //ex: RANDOM()
    public FunctionNode(String functionName){
        this.functionName  = functionName;
    }

    public FunctionNode(String functionName, List<Node> paramList) {
        this.functionName = functionName;
        this.paramList = paramList;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Node> getParamList() {
        return paramList;
    }

    @Override
    public String toString() {
        if(functionName.equals("RANDOM"))
            return functionName;

        else
            return functionName + "( " + Arrays.toString(paramList.toArray()) + ")";
    }
}
