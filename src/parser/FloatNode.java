package parser;

public class FloatNode extends Node{

    private float floatValue;

    public FloatNode(){}

    public FloatNode(float floatValue){
        this.floatValue = floatValue;
    }

    public float getFloatValue(){return floatValue;}

    @Override
    public String toString() {
        return "FLOAT(" + floatValue + ")";
    }
}
