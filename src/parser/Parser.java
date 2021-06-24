package parser;


import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Parser {
    private Node node;
    private List<Token> tokenList = new ArrayList<Token>();


    public Parser(List<Token> tokenList){
        this.tokenList = tokenList;
    }

    /**
     * parse the tokenList to AST
     * @return MathOpNode
     */
    public StatementNode parse() throws Exception {
        return Statement();
    }


    /**
     * Expression: Term {PLUS || MINUS} Term
     * expression is always be the root node
     * @return
     */
    public Node parseExpression() throws Exception {
        Node exprNode = parseTerm();
        boolean isFound = true;
        Node funNode = functionInvocation();
        if(funNode != null) {
            return funNode;
        }

        if(exprNode == null)
            return null;
        do{
            if(matchAndRemove(Token.Type.PLUS) != null) {
                exprNode = new MathOpNode(MathOpNode.Opcode.ADD, exprNode, parseTerm());
            }
            else if(matchAndRemove(Token.Type.MINUS) != null) {
                exprNode = new MathOpNode(MathOpNode.Opcode.MINUS, exprNode, parseTerm());
            }
            else
                isFound = false;
        }while (isFound);

        return exprNode;
    }

    /**
     * Term: Factor {TIME || DIVIDE} Factor
     * @return
     */
    public Node parseTerm() throws Exception {
        Node termNode;
        termNode = parseFactor();
        boolean isFound;
        if(termNode == null)
            return null;
        do{
            isFound = true;
            if(matchAndRemove(Token.Type.TIME) != null)
                termNode = new MathOpNode(MathOpNode.Opcode.MULTI, termNode, parseFactor());
            else if(matchAndRemove(Token.Type.DIVIDE) != null)
                termNode = new MathOpNode(MathOpNode.Opcode.DIVIDE, termNode, parseFactor());
            else
                isFound = false;
        }while(isFound);

        return termNode;
    }

    /**
     * Factor: {-}NUM || LPAREN Expression RPAREN
     * @return
     */
    public Node parseFactor() throws Exception {
        Token token = tokenList.get(0);

        if(matchAndRemove(Token.Type.NUMBER) != null){   //if token is number
            if(isInteger(token.getTokenValue()))
                node = new IntegerNode(Integer.parseInt(token.getTokenValue()));

            else if(isFloat(token.getTokenValue()))
                node = new FloatNode(Float.parseFloat(token.getTokenValue()));
            return node;
        }
        else if(matchAndRemove(Token.Type.LPAREN) != null){   //( + expression + )
            node = parseExpression();
            if(node == null) throw new Exception("Found left Parenthesis but no expression!");
            if(matchAndRemove(Token.Type.RPAREN) != null)
                return node;
            else
                throw new Exception("Found left Parenthesis and expression but missing right Parenthesis!");
        }
        else if(matchAndRemove(Token.Type.IDENTIFIER) != null) {
            VariableNode node = new VariableNode(token.getTokenValue());
            return node;
        }
        else if(matchAndRemove(Token.Type.STRING) != null){
            StringNode stringNode = new StringNode(token.getTokenValue());
            return stringNode;
        }
        return null;
    }

    /**
     * accept any numbers of statements
     * and call Statement() until Statement() fails
     * @return
     */
    public StatementsNode Statements() throws Exception {
        List<StatementNode> list = new ArrayList<>();
        StatementNode ast = null;
        do {
            ast = Statement();
            if(ast != null) {
                list.add(ast);
            }
        }while(ast != null);
//        for(Node n: list)
//            System.out.println(n);
        return new StatementsNode(list);
    }

    /**
     * handle a single statement & return its node
     * @return
     */
    public StatementNode Statement() throws Exception {
        StatementNode statNode;
        statNode = printStatement();


        if (statNode == null)
            statNode = assignment();
        if(statNode == null)
            statNode = dataStatement();
        if(statNode == null)
            statNode = readStatement();
        if(statNode == null)
            statNode = inputStatement();
        if(statNode == null)
            statNode = forStatement();
        if(statNode == null)
            statNode = nextStatement();
        if(statNode == null)
            statNode = gosubStatement();
        if(statNode == null)
            statNode = labeledStatement();
        if(statNode == null)
            statNode = returnStatement();
        if(statNode == null)
            statNode = ifStatement();

        return statNode;

    }

    /**
     * syntax:
     * IF x<5 THEN xIsSmall
     */
    public StatementNode ifStatement() throws Exception {
        VariableNode v1;
        VariableNode v2;
        if(matchAndRemove(Token.Type.IF) != null){
            Token t1 = tokenList.get(0);    //get x
            if(matchAndRemove(Token.Type.IDENTIFIER) != null){
                v1 = new VariableNode(t1.getTokenValue());
                Token op = getOperatorForBooleanExpr();    //operator
                Token t2 = tokenList.get(0);    //get 5
                if((matchAndRemove(Token.Type.IDENTIFIER) != null || matchAndRemove(Token.Type.NUMBER) != null) &&
                        matchAndRemove(Token.Type.THEN) != null){
                    v2 = new VariableNode(t2.getTokenValue());
                    Token labelToken = tokenList.get(0);    //get the labelName
                    if(matchAndRemove(Token.Type.IDENTIFIER) != null) {
                        String labelName = labelToken.getTokenValue();
                        return new IfNode(new BooleanOperationNode(v1, op.getType(), v2), labelName);
                    }
                }
            }
        }
        return null;
    }

    public Token getOperatorForBooleanExpr() throws Exception {
        if(matchAndRemove(Token.Type.LESS) != null) {
            Token newToken = new Token(Token.Type.LESS);
            return newToken;
        }
        else if(matchAndRemove(Token.Type.GREATER) != null) {
            Token newToken = new Token(Token.Type.GREATER);
            return newToken;
        }
        else if(matchAndRemove(Token.Type.EQUAL) != null) {
            Token newToken = new Token(Token.Type.GREATER);
            return newToken;
        }
        else
            throw new Exception("INVALID BOOLEAN OPERATOR!");
    }

//    check for num || letter
//    check for operator
//    check for nums
//    return boolean()

    /**
     * By checking the keywords
     * @return the functionNode
     */
    public Node functionInvocation() throws Exception {
        Token funToken = matchAndRemove(Token.Type.FUNCTION);
        if(funToken == null)
            return null;
        else{
            List<Node> paramList = new ArrayList<>();
            boolean rightParenFound = true; //when the ')' found
            String funName = "";    //for storing function name
            String name = funToken.getTokenValue();  //function name
            if (name.equals("RANDOM") || name.equals("NUM$") || name.equals("LEFT$") || name.equals("RIGHT$") || name.equals("MID$") || name.equals("VAL") || name.equals("VAL%")){
                funName = name;
                if (matchAndRemove(Token.Type.LPAREN) != null) {
                    while (rightParenFound) {
                        Node firstParam = getNodeForFunctionParameter();  //first parameter: string || number || null
                        if (firstParam != null)
                            paramList.add(firstParam);
                        if (matchAndRemove(Token.Type.COMMA) != null) {
                            Node paramNode = getNodeForFunctionParameter();   //get the rest of params
                            if (paramNode != null)
                                paramList.add(paramNode);
                            else
                                throw new Exception("Parameters inside of function can not be NULL!");
                        }
                        if (matchAndRemove(Token.Type.RPAREN) != null) {  //end with the ')'
                            rightParenFound = false;
                            return new FunctionNode(funName, paramList);
                        }
                    }
                }
            }

            else
                System.out.println("WARNING: THE FUNCTION NAME IS NOT FOUND!"); //throw exception here
        }
        return null;
    }

    /**
     * Return the Node as the parameter for Function
     * Currently either String or Number
     * @return
     */
    public Node getNodeForFunctionParameter() throws Exception {
        Token t1 = matchAndRemove(Token.Type.NUMBER);
        if(t1 != null){
            if(isInteger(t1.getTokenValue()))
                return new IntegerNode(Integer.parseInt(t1.getTokenValue()));
            else if(isFloat(t1.getTokenValue()))
                return new FloatNode(Float.parseFloat(t1.getTokenValue()));
        }
        else{
            Token t2 = matchAndRemove(Token.Type.STRING);
            if(t2 != null)
                return new StringNode(t2.getTokenValue());
        }
        return null;
    }



    /**
     * Syntax:
     * goto: c=4+9i
     */
    public StatementNode labeledStatement(){

        return null;
    }

    /**
     * Syntax: GOSUB label
     * @return GoSubNode
     * @throws Exception
     */
    public StatementNode gosubStatement() throws Exception {
        GosubNode node;
        if(matchAndRemove(Token.Type.GOSUB) != null){
            Token token = matchAndRemove(Token.Type.IDENTIFIER);
            if(token!=null){
                VariableNode label = new VariableNode(token.getTokenValue());
                node = new GosubNode(label);
                return node;
            }
            else
                throw new Exception("After GOSUB statement, it needs to call a label(or function)!");
        }
        return null;
    }

    /**
     * Syntax:
     * RETURN
     * @return
     */
    public StatementNode returnStatement(){
        if(matchAndRemove(Token.Type.RETURN) != null)
            return new ReturnNode();
        return null;
    }

    /**
     * Syntax:
     * FOR A = 0 TO 10 STEP 2
     * 	PRINT A
     * 	NEXT A
     * @return
     */
    public StatementNode forStatement() throws Exception {
        if(matchAndRemove(Token.Type.FOR) != null){
            Token varToken = tokenList.get(0); //A
            if(matchAndRemove(Token.Type.IDENTIFIER) != null){
                VariableNode varNode = new VariableNode(varToken.getTokenValue());
                if(matchAndRemove(Token.Type.EQUAL) != null){   //check the '='
                    Token beginIndex = tokenList.get(0);        //get beginIndex 0
                    if(matchAndRemove(Token.Type.NUMBER) != null){
                        int begin = Integer.parseInt(beginIndex.getTokenValue());
                        if(matchAndRemove(Token.Type.TO) != null){
                            Token endToken = tokenList.get(0);      //get endIndex 10
                            if(matchAndRemove(Token.Type.NUMBER) != null){
                                int end = Integer.parseInt(endToken.getTokenValue());
                                if(tokenList.size() > 1){       //if <1 then can not even do 'STEP 2'
                                    if(matchAndRemove(Token.Type.STEP) != null){
                                        Token stepToken = tokenList.get(0);     //get stepValue 2
                                        if(matchAndRemove(Token.Type.NUMBER) != null){
                                            int step = Integer.parseInt(stepToken.getTokenValue());
                                            return new ForNode(varNode, begin, end, step);
                                        }
                                    }
                                }
                                else
                                    return new ForNode(varNode, begin, end, 0); //default will set to 1
                            }
                        }
                    }
                }
            }
        }
        return null;
        //throw new Exception("ForStatement's Syntax is not valid!");
    }

    /**
     * Syntax:
     * FOR A = 0 TO 10 STEP 2
     * 	PRINT A
     * 	NEXT A
     * @return
     */
    public StatementNode nextStatement() throws Exception {
        if(matchAndRemove(Token.Type.NEXT) != null){
            Token varToken = tokenList.get(0);  //should be a variable
            if(varToken != null){
                VariableNode varNode = new VariableNode(varToken.getTokenValue());
                return new NextNode(varNode);
            }
            else
                throw new Exception("Invalid syntax for NEXT statement!");
        }
        return null;
    }

    /**
     * ex: Variable Equals Expr
     * x = 1
     * @return
     */
    public StatementNode assignment() throws Exception {
        VariableNode varNode;
        AssignmentNode assignmentNode;
        Token t = matchAndRemove(Token.Type.IDENTIFIER);  //check to see first token: Identifier? ex: a=1+3
        if(t != null && matchAndRemove(Token.Type.EQUAL)!=null){
            varNode = new VariableNode(t.tokenValue);
            assignmentNode = new AssignmentNode(varNode, parseExpression());
            return assignmentNode;
        }
        return null;
    }


    /**
     * DATA will deal with Number and String
     * @return
     * @throws Exception
     */
    public StatementNode dataStatement() throws Exception {
        List<Node> list = new ArrayList<>();

       if(matchAndRemove(Token.Type.DATA) != null){
           Node node = getNodeForDateStatement();
           list.add(node);
           while(matchAndRemove(Token.Type.COMMA) != null){
                Node n = getNodeForDateStatement();
                list.add(n);
           }
           return new DataNode(list);
       }
        return null;
    }

    /**
     * Return the node for dataStatement() method
     * @return
     * @throws Exception
     */
    public Node getNodeForDateStatement() throws Exception {
        Token t1 = matchAndRemove(Token.Type.NUMBER);
        if(t1 != null) {
            return parseFactor();
        }

        Token t2 = matchAndRemove(Token.Type.STRING);
        if(t2 != null)
            return new StringNode(t2.getTokenValue());
        return null;
    }

    /**
     * READ will deal with variables only
     * @return
     */
    public ReadNode readStatement(){
        List<VariableNode> list = new ArrayList<>();
        if(matchAndRemove(Token.Type.READ) != null){
            VariableNode node = getNodeForReadStatement();
            list.add(node);
            while(matchAndRemove(Token.Type.COMMA) != null){
                VariableNode n = getNodeForReadStatement();
                list.add(n);
            }
            return new ReadNode(list);
        }
        return null;
    }

    /**
     * Return the node for readStatement() methods
     * @return
     */
    public VariableNode getNodeForReadStatement(){
        Token t = matchAndRemove(Token.Type.IDENTIFIER);
        if(t != null)
            return new VariableNode(t.getTokenValue());
        return null;
    }

    /**
     * Syntax:
     * Input (StringNode||VariableNode) List<VariabeNode>
     * @return
     */
    public StatementNode inputStatement() throws Exception {
        List<VariableNode> list = new ArrayList<>();

        if(matchAndRemove(Token.Type.INPUT) != null){
            Token t1 = matchAndRemove(Token.Type.STRING);
            StringNode strNode;
            if(t1 != null){
                strNode = new StringNode(t1.tokenValue);    //firstNode must be StringNode
            }
            else
                throw new Exception("The first Node of InputStatement needs to be StringNode!");

            while(matchAndRemove(Token.Type.COMMA) != null){
                VariableNode n = getNodeForInput();
                   list.add(n);
            }
                return new InputNode(strNode, list);
            }

        return null;
    }

    /**
     * Get the node for InputStatement() methods
     * @return
     */
    public VariableNode getNodeForInput(){
        Token t2 = matchAndRemove(Token.Type.IDENTIFIER);
        if(t2 != null)
            return new VariableNode(t2.getTokenValue());
        return null;
    }

    /**
     *  accepts a print statement and creates a PrintNode or returns NULL:
     *  syntax: PRINT A, B
     *  invalid situation: PRINT
     */
    public StatementNode printStatement() throws Exception {
        Token t = matchAndRemove(Token.Type.PRINT);
        if(t == null) return null;
        List<Node> list = printList();
        //System.out.println("Content of PRINT"  + Arrays.toString(list.toArray()));    //print the content of printing
        return new PrintNode(list);
    }

    /**
     * Accept a print list(consisted of commas separated list of expressions)
     * @return  exprNode
     */  //print Id(a) , ID(b)
    public List<Node> printList() throws Exception {
        List<Node> list = new ArrayList<>();
        Node node = getNodeForPrintList();
        list.add(node);     //add the first node since it can't be comma
        boolean isEmpty = true; //when reach the end of content
        while(isEmpty){
            if(matchAndRemove(Token.Type.COMMA) != null){
                Node n = getNodeForPrintList();
                list.add(n);
            }
            else{
                isEmpty = false;
            }
        }

        return list;
    }

    /**
     * return the node for printList() method
     * @return
     */
    public Node getNodeForPrintList(){
        Token t1 = matchAndRemove(Token.Type.IDENTIFIER);
        if(t1 != null)
            return new VariableNode(t1.getTokenValue());

        Token t2 = matchAndRemove(Token.Type.STRING);
        if(t2 != null)
            return new StringNode(t2.getTokenValue());

        Token t3 = matchAndRemove(Token.Type.NUMBER);
        if (t3 != null) {
                if (isInteger(t3.getTokenValue()))
                    return new IntegerNode(Integer.parseInt(t3.getTokenValue()));
                else if (isFloat(t3.getTokenValue()))
                    return new FloatNode(Float.parseFloat(t3.getTokenValue()));
        }
        return null;
    }



    /**
     * If the first token is matched in the tokenList:
     * pop out and then remove it from the list
     * @param type
     * @return
     */
    public Token matchAndRemove(Token.Type type){
        Token removedToken;
        if(!tokenList.isEmpty() && tokenList.get(0).getType() == type){
            removedToken = tokenList.get(0);
            tokenList.remove(0);
            return removedToken;
        }
        else
            return null;
    }

    /**
     * Check whether the string is the integer or not
     * @param value
     * @return
     */
    public boolean isInteger(String value){
        try{
            Integer.parseInt(value);
        }
        catch (NumberFormatException e){
            return false;
        }
        catch (NullPointerException e){return false;}
        return true;
    }

    /**
     * Check whether the string is the float or not
     * @param value
     * @return
     */
    public boolean isFloat(String value){
        try{
            Float.parseFloat(value);
        }
        catch (NumberFormatException e){
            return false;
        }
        catch (NullPointerException e){return false;}
        return true;
    }

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("src/parser/parserTest");
        List<String> content = Files.readAllLines(path, Charset.forName("UTF-8"));
        System.out.println("Basic File: ");
        for(String s: content)
            System.out.println(s);

        System.out.println("\n\n");


        List<Node> statementList = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            statementList.add(new Parser(new Lexer().lex(content.get(i))).parse());
        }

        for(Node n: statementList)
            System.out.println(n);
    }

}


