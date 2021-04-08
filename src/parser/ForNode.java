package parser;

/**
 * Syntax:
 * FOR A = 0 TO 10 STEP 2
 */
public class ForNode extends StatementNode{
    private VariableNode name;
    private int begin;
    private int end;
    private int stepValue;
    public Node nextStatement;

    /**
     * fetch the next Statement after forStatement is terminated
     * @return
     */
    public Node getNextStatement() {
        return nextStatement;
    }

    public void setNextStatement(Node nextStatement) {
        this.nextStatement = nextStatement;
    }

    public ForNode(VariableNode name, int begin, int end, int stepValue) {
        this.name = name;
        this.begin = begin;
        this.end = end;
        if(stepValue == 0){
            this.stepValue = 1;
        }
        else{
            this.stepValue = stepValue;
        }

    }

    public VariableNode getName() {
        return name;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public int getStepValue() {
        return stepValue;
    }

    /**
     * format:
     * 1. ForNode(VariableNode(a), beginIndex 0, endIndex 100, increment 4))
     * 2. ForNode(VariableNode(a), inital Value 2, Goes Up to 7, increment 1))
     * @return
     */
    public String toString(){
            return "ForNode(" + name + ", " + "beginIndex " + begin + ", " + "endIndex " + end +
                    ", " + "increment " + stepValue + ")";
    }

}
