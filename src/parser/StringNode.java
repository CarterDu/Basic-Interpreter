package parser;

public class StringNode extends Node{
    private String value;

    public StringNode(){}

    public StringNode(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringNode(" + value + ")";
    }

}
