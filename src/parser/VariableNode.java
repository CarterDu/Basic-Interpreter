package parser;

public class VariableNode extends Node {
    private String value;


    public VariableNode(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
            return "VariableNode(" + value + "$)";
    }
}
