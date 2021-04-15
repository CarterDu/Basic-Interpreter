package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * AST Interpreter
 */
public class Interpreter{
    private HashMap<String, Integer> integerVariables = new HashMap<String, Integer>();
    private HashMap<String, Float> floatVariables = new HashMap<String, Float>();
    private HashMap<String, String> stringVariables = new HashMap<String, String>();
    private HashMap<String, StatementNode> labelDestination = new HashMap<String, StatementNode>();     //store labels

    private List<Node> statementList = new ArrayList<>();
    private List<Node> dataElement = new ArrayList<>();   //store data from DataStatement

    public Interpreter(List<Node> statementList){
        this.statementList = statementList;
    }

    public enum VariableType{
        STRING, INTEGER, FLOAT;
    }


    /**
     * allowing the visitor pattern over the statements from parser
     */
    public void initialize(){
        extractLabel();
        extractForAndNextStatement();
        extractDataStatement();
    }

    /**
     *
     * @param
     */
    public void interpret() throws Exception {
        Node currentStatement = statementList.get(0);   //represent the head(firstStatement)
        while(currentStatement != null){
            if(currentStatement instanceof PrintNode)
                extractPrintStatement();
            else if(currentStatement instanceof AssignmentNode)
                extractAssignment();
            else if(currentStatement instanceof InputNode)
                extractInputStatement();
            else if(currentStatement instanceof ReadNode)
                extractReadStatement();

            ((StatementNode)currentStatement).setTheNextStatement(((StatementNode) currentStatement).getTheNextStatement());    //go to the next statement
        }
    }

    /**
     * example:
     * 1. PRINT "Float Multiplication w/ Variable Test: ", 4.4 * 9.5
     * 2. READ c$, s$, z
     * DATA "Albany", "NY", 11222
     * PRINT "ReadNode Test: ", c$, s$, z
     * 3. PRINT 5
     * 4. PRINT 3+6*6 (MathOp)
     * Elements toBePrinted will consider the {int, float, string, mathOp}
     * @throws Exception
     */
    public void extractPrintStatement() throws Exception {
        for(Node statement: statementList){
            if(statement instanceof PrintNode){
                PrintNode printStatement = (PrintNode) statement;
                for(Node n: printStatement.getNodeList()){
                    if(n instanceof IntegerNode)    //ex: PRINT 4
                        System.out.println(((IntegerNode) n).getIntValue());
                    else if(n instanceof FloatNode)
                        System.out.println(((FloatNode) n).getFloatValue());
                    else if(n instanceof StringNode)    //ex: PRINT "HI"
                        System.out.println(((StringNode) n).getValue());
                    else if(n instanceof VariableNode){     //ex: PRINT a, $s, f%
                        if(getType(n) == VariableType.STRING)  //ex: $s
                            System.out.println(stringVariables.get(((VariableNode) n).getValue()));
                        else if(getType(n) == VariableType.INTEGER) //ex: a
                            System.out.println(integerVariables.get(((VariableNode) n).getValue()));
                        else if(getType(n) == VariableType.FLOAT) //ex: %f
                            System.out.println(floatVariables.get(((VariableNode) n).getValue()));
                        else
                            System.out.println("NULL"); //default value (maybe 0)
                    }
                    else if(n instanceof MathOpNode)   //PRINT 3+6*6
                        System.out.println(evaluateIntMathOp(n));
                    else if(n instanceof FunctionNode){ //PRINT RANDOM()

                    }

                }
            }
        }
    }

    /**
     * handle the assignmentStatement
     * By assign the value to each specific variable
     * syntax: a=b+1 => AssignmentNode(a, MathOpNode(+, b, 1))
     * var$ = "hello"  var = 7
     */
    public void extractAssignment() throws Exception {
        for(Node statement: statementList){
            if(statement instanceof AssignmentNode){
                AssignmentNode assignmentNode = (AssignmentNode) statement;
                Node rightSide = assignmentNode.getNode();  //rightSide should be expression()
                //var: strType || value: str
                if(getType(assignmentNode.getVarNode())==VariableType.STRING && getType(assignmentNode.getNode())==VariableType.STRING){
                    String varName = assignmentNode.getVarNode().getValue();
                    String value = ((StringNode)rightSide).getValue();
                    stringVariables.put(varName, value);
                }
                //var: intType || value: int
                else if(getType(assignmentNode.getVarNode())==VariableType.INTEGER && getType(assignmentNode.getNode())==VariableType.INTEGER){
                    String varName = assignmentNode.getVarNode().getValue();
                    int value = ((IntegerNode)rightSide).getIntValue();
                    integerVariables.put(varName, value);
                }
                //var: floatType || value: float
                else if(getType(assignmentNode.getVarNode())==VariableType.FLOAT && getType(assignmentNode.getNode())==VariableType.FLOAT){
                    String varName = assignmentNode.getVarNode().getValue();
                    float value = ((FloatNode)rightSide).getFloatValue();
                    floatVariables.put(varName, value);
                }
                else
                    throw new Exception("VARIABLE TYPE DOES NOT MATCH WITH THE VALUE!");
            }
        }
    }

    /**
     * Check for node's type
     * @param node
     * @return  STRING INT FLOAT
     */
    public VariableType getType(Node node){
        if(node instanceof StringNode)
            return VariableType.STRING;
        else if(node instanceof IntegerNode)
            return VariableType.INTEGER;
        else if(node instanceof FloatNode)
            return VariableType.FLOAT;
        else if(node instanceof VariableNode){
            String lastChar = ((VariableNode) node).getValue().substring(((VariableNode) node).getValue().length() - 1);    //last character of the string(varName)
            if(lastChar.equals("$"))    //variable is a string
                return VariableType.STRING;
            else if(lastChar.equals("%"))   //variable is a float
                return VariableType.FLOAT;
            else                            //variable is an integer
                return VariableType.INTEGER;
        }
        return null;
    }

    /**
     * Retrieve the elements from the dataElements
     * And put the corresponding variable into the corresponding hashmap
     * syntax:
     * READ c$, s%, z
     * DATA "Albany", 0.7, 12203
     */
    public void extractReadStatement(){
        for(Node statement: statementList){
            if(statement instanceof ReadNode){
                ReadNode readStatement = (ReadNode) statement;
                for(VariableNode readVariable: readStatement.getList()){    //search readVariables inside of the ReadStatement
                    for(Node node: dataElement){    //search in the dataElements
                        if(getType(readVariable)==getType(node)){
                            if(getType(readVariable)==VariableType.STRING){ //ex: c$="Albany"
                                StringNode strNode = (StringNode) node;
                                stringVariables.put(readVariable.getValue(), strNode.getValue());
                            }
                            else if(getType(readVariable) == VariableType.INTEGER){ //ex: z=12203
                                IntegerNode intNode = (IntegerNode) node;
                                integerVariables.put(readVariable.getValue(), intNode.getIntValue());
                            }
                            else if(getType(readVariable) == VariableType.FLOAT){   //s%=0.7
                                FloatNode floatNode = (FloatNode) node;
                                floatVariables.put(readVariable.getValue(), floatNode.getFloatValue());
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * syntax:
     * INPUT “What is your name and age?”, name$, age
     * PRINT “Hi “, name$, “ you are “, age, “ years old!”
     */
    public void extractInputStatement() throws Exception {
        for(Node statement: statementList){
            if(statement instanceof InputNode){
                InputNode inputNode = (InputNode) statement;
                System.out.println(inputNode.getStringNode().getValue());//print the quote on the console
                for(VariableNode var: inputNode.getList()){
                    if(getType(var) == VariableType.STRING){    //ready to store string to the var in hashmap
                        Scanner scanner = new Scanner(System.in);
                        String result1 = scanner.next();
                        stringVariables.put(var.getValue(), result1);
                    }
                    else if(getType(var) == VariableType.INTEGER){  //ready to store int to the var in hashmap
                        Scanner scanner = new Scanner(System.in);
                        int result2 = scanner.nextInt();
                        integerVariables.put(var.getValue(), result2);
                    }
                    else if(getType(var) == VariableType.FLOAT){  //ready to store float to the var in hashmap
                        Scanner scanner = new Scanner(System.in);
                        float result3 = scanner.nextFloat();
                        floatVariables.put(var.getValue(), result3);
                    }
                    else
                        throw new Exception("INVALID TYPE OF VARIABLE FOR INPUT STATEMENT!");
                }
            }
        }
    }


    public Node extractFunction(Node node) throws Exception {
        if(node instanceof FunctionNode){
            FunctionNode funNode = (FunctionNode) node;
            String funName = funNode.getFunctionName();
            switch (funName){
                case "RANDOM":
                    int value = (int) Math.random();
                    return new IntegerNode(value);

                case "LEFT$":   //LEFT$(string, int) – returns the leftmost N characters from the string
                    StringNode firstParameter = (StringNode) funNode.getParamList().get(0);   //get the string
                    IntegerNode secondParameter = (IntegerNode) funNode.getParamList().get(1);   //get the index
                    String str = firstParameter.getValue().substring(0, secondParameter.getIntValue());
                    return new StringNode(str);

                case "RIGHT$":  //RIGHT$(string, int) – returns the rightmost N characters from the string
                    StringNode firstPara = (StringNode) funNode.getParamList().get(0);   //get the string
                    IntegerNode secondPara = (IntegerNode) funNode.getParamList().get(1);   //get the index
                    String returnValue = firstPara.getValue().substring(secondPara.getIntValue(), firstPara.getValue().length()-1);
                    return new StringNode(returnValue);

                case "MID$":    //MID$(string,int, int), MID$(“Albany”,2,3) = “ban”
                    StringNode strValue = (StringNode) funNode.getParamList().get(0);   //get the string
                    IntegerNode secondIndex = (IntegerNode) funNode.getParamList().get(1);   //get the index
                    IntegerNode thirdIndex = (IntegerNode) funNode.getParamList().get(2);   //get the index
                    String newValue = strValue.getValue().substring(secondIndex.getIntValue(), thirdIndex.getIntValue());
                    return new StringNode(newValue);

                case "NUM$":    //NUM$(int or float) – converts a number to a string
                    Node number = funNode.getParamList().get(0);//parameter be either int or float
                    if(number instanceof IntegerNode){
                        int numValue = ((IntegerNode) number).getIntValue();
                        String numToBeConverted = String.valueOf(numValue);
                        return new StringNode(numToBeConverted);
                    }
                    else if(number instanceof FloatNode){
                        float numValue = ((FloatNode) number).getFloatValue();
                        String numToBeConverted = String.valueOf(numValue);
                        return new StringNode(numToBeConverted);
                    }
                    else
                        throw new Exception("INVALID PARAMETER FOR NUM$() FUNCTION! PARAMETER SHOULD BE EITHER INT OR FLOAT!");

                case "VAL":     //VAL(string) – converts a string to an integer
                    StringNode stringNode = (StringNode) funNode.getParamList().get(0);
                    String strNum = stringNode.getValue();  //get the string value
                    try{
                        int integerValue = Integer.parseInt(strNum);
                        return new IntegerNode(integerValue);
                    }
                    catch (NumberFormatException e){
                        e.printStackTrace();
                    }

                case "VAL%":    //VAL%(string) – converts a string to a float
                    StringNode strNode = (StringNode) funNode.getParamList().get(0);
                    String stringNodeValue = strNode.getValue();  //get the string value
                    try{
                        float floatValue = Float.parseFloat(stringNodeValue);
                        return new FloatNode(floatValue);
                    }
                    catch (NumberFormatException e){
                        e.printStackTrace();
                    }

                default:
                    throw new Exception("INVALID FUNCTION'S SYNTAX! (CHECK FOR NAME OR PARAMETER!)");
            }
        }
        return null;
    }

    /**
     * node:int/float =>  value
     * node:int/float variable => value of variable
     * node:MathOp =>   call evaluateIntMathOp() on left and right,
     * then do the op (add, subtract, multiply or divide) on the results of each side. Return the result.
     * EX: PRINT 3+6*6
     * @param node
     * @return IntegerNode||FloatNode
     */
    public int evaluateIntMathOp(Node node) throws Exception {
        int result = 0;
        if(node instanceof MathOpNode){
            MathOpNode mathOpNode = (MathOpNode) node;
            Node left = mathOpNode.getLeft();
            Node right = mathOpNode.getRight();
            if(left instanceof IntegerNode && right instanceof MathOpNode){ //ex: x = 3+7*7
                result = ((IntegerNode) left).getIntValue() + evaluateIntMathOp(right);
            }
            else if(left instanceof MathOpNode && right instanceof IntegerNode){  //ex: x = 4*7+9
                result = ((IntegerNode) right).getIntValue() + evaluateIntMathOp(left);
            }
            else if(left instanceof MathOpNode && right instanceof MathOpNode){    //ex: 5*7+8*8
                try{
                    int leftOperand = evaluateIntMathOp(left);
                    int rightOperand = evaluateIntMathOp(right);
                    result = leftOperand + rightOperand;
                }
                catch (NumberFormatException e){
                    float leftOperand = evaluateFloatMathOp(left);
                    float rightOperand = evaluateFloatMathOp(right);
                    result = (int) (leftOperand + rightOperand);
                    e.printStackTrace();
                }
            }
            else if(left instanceof IntegerNode && right instanceof IntegerNode){
                int leftValue = ((IntegerNode) left).getIntValue();
                int rightValue = ((IntegerNode) right).getIntValue();
                if(mathOpNode.getOpcode() == MathOpNode.Opcode.ADD)
                    result = leftValue + rightValue;
                else if(mathOpNode.getOpcode() == MathOpNode.Opcode.MINUS)
                    result = leftValue - rightValue;
                else if(mathOpNode.getOpcode() == MathOpNode.Opcode.MULTI)
                    result = leftValue * rightValue;
                else if(mathOpNode.getOpcode() == MathOpNode.Opcode.DIVIDE)
                    result = leftValue / rightValue;
                else
                    throw new Exception("INVALID OPERATOR WHEN EVALUATE MATH OPERATION!");
            }
        }
        return result;
    }

    public float evaluateFloatMathOp(Node node) throws Exception {
        float result = 0;
        if(node instanceof MathOpNode){
            MathOpNode mathOpNode = (MathOpNode) node;
            Node left = mathOpNode.getLeft();
            Node right = mathOpNode.getRight();
            if(left instanceof FloatNode && right instanceof MathOpNode){ //ex: x = 3.6+7.7*7.8
                result = ((FloatNode) left).getFloatValue() + evaluateFloatMathOp(right);
            }
            else if(left instanceof MathOpNode && right instanceof FloatNode){  //ex: x = 4*7+9
                result = ((FloatNode) right).getFloatValue() + evaluateFloatMathOp(left);
            }
            else if(left instanceof MathOpNode && right instanceof MathOpNode){    //ex: 5*7+8*8
                try{
                    float leftOperand = evaluateFloatMathOp(left);
                    float rightOperand = evaluateFloatMathOp(right);
                    result = leftOperand + rightOperand;
                }
                catch (NumberFormatException e){
                    int leftOperand = evaluateIntMathOp(left);
                    int rightOperand = evaluateIntMathOp(right);
                    result = leftOperand + rightOperand;
                    e.printStackTrace();
                }
            }
            else if(left instanceof FloatNode && right instanceof FloatNode){
                float leftValue = ((FloatNode) left).getFloatValue();
                float rightValue = ((FloatNode) right).getFloatValue();
                if(mathOpNode.getOpcode() == MathOpNode.Opcode.ADD)
                    result = leftValue + rightValue;
                else if(mathOpNode.getOpcode() == MathOpNode.Opcode.MINUS)
                    result = leftValue - rightValue;
                else if(mathOpNode.getOpcode() == MathOpNode.Opcode.MULTI)
                    result = leftValue * rightValue;
                else if(mathOpNode.getOpcode() == MathOpNode.Opcode.DIVIDE)
                    result = leftValue / rightValue;
                else
                    throw new Exception("INVALID OPERATOR WHEN EVALUATE MATH OPERATION!");
            }
        }
        return result;
    }

    /**
     * Syntax:
     * FtoC: C = 5*(F-32)/9
     * fetch the labelName and corresponding statement to labelDestination
     */
    public void extractLabel(){
        for(Node n: statementList){
            if(n instanceof LabelStatementNode){
                String labelName = ((LabelStatementNode) n).getLabelName();
                StatementNode statement = ((LabelStatementNode) n).getLabelStatement();
                labelDestination.put(labelName, statement);
            }
            statementList.remove(n); //remove the label statement after fetch its content
        }
    }

    /**
     * syntax:
     * 	FOR A = 0 TO 10 STEP 2
     * 	PRINT A
     * 	NEXT A
     * @param
     */
    public void extractForAndNextStatement(){
        boolean forFound = false;   //if forStatement is found
        ForNode forNode = null;   //store the forNode
        NextNode nextNode = null;   //store the nextNode
        for (int i = 0; i < statementList.size(); i++) {
            Node currentStatement = statementList.get(i);
            if(currentStatement instanceof ForNode){
                forFound = true;
                forNode = (ForNode) currentStatement;
                int beginIndex = ((ForNode) currentStatement).getBegin();
                int endIndex = ((ForNode) currentStatement).getEnd();
                if(beginIndex == endIndex) {  //end of the loop
                    forNode.setNextStatement(nextNode); //link to the statement after nextStatement
                }
            }
            else if(currentStatement instanceof NextNode && forFound){
                nextNode = (NextNode) currentStatement;
                VariableNode varNode = nextNode.getVariableNode();
                if(getType(varNode) == VariableType.STRING)
                    //
                ((NextNode) currentStatement).setNextStatementMatchedToForStatement(forNode);
            }
        }
    }


    /**
     * syntax:
     * DATA 10,”mphipps”
     * READ a, a$
     * 1. process READ first
     */
    public void extractDataStatement(){
        for (int i = 0; i < statementList.size(); i++) {
            Node currentStatement = statementList.get(i);
            if(currentStatement instanceof DataNode){
                for(Node n: ((DataNode) currentStatement).getList())
                    dataElement.add(n);     //store element from dataStatement
                statementList.remove(currentStatement);  //remove dataStatement from AST
            }
        }
    }

}


