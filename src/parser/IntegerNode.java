package parser;

public class IntegerNode extends Node{

    private int intValue;

    public IntegerNode(int intValue){
        this.intValue = intValue;
    }

    public int getIntValue(){return intValue;}

    @Override
    public String toString() {
        return "INTEGER(" + intValue + ")";
    }
}
