package parser;

public class StringNode extends Node{
    private String value;

    public StringNode(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringNode(" + value + ")";
    }


}
