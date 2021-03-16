package parser;

public class VariableNode extends Node {
    private String value;

    public VariableNode(String value){
        this.value = value;
    }


    @Override
    public String toString() {
        return "VariableNode(" + value + ")";
    }
}
