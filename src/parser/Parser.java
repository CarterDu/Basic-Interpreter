package parser;

import java.util.ArrayList;
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
    public Node parse() throws Exception {
        return Statements();
    }

    /**
     * Expression: Term {PLUS || MINUS} Term
     * expression is always be the root node
     * @return
     */
    public Node parseExpression() throws Exception {
        Node exprNode = parseTerm();
        boolean isFound = true;
        if(exprNode == null)
            return null;
        do{
            if(matchAndRemove(Token.Type.PLUS) != null)
                exprNode = new MathOpNode(MathOpNode.Opcode.ADD, exprNode, parseTerm());
            else if(matchAndRemove(Token.Type.MINUS) != null)
                exprNode = new MathOpNode(MathOpNode.Opcode.MINUS, exprNode, parseTerm());
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
        Token token = this.tokenList.get(0);
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
        return null;
    }

    /**
     * accept any numbers of statements
     * and call Statement() until Statement() fails
     * @return
     */
    public Node Statements() throws Exception {
        List<Node> list = new ArrayList<>();
        Node ast = null;
        do {
            ast = Statement();
            if(ast != null) {
                list.add(ast);
            }
        }while(ast != null);

        return new StatementsNode(list);
    }

    /**
     * handle a single statement & return its node
     * @return
     */
    public Node Statement() throws Exception {
        Node statNode;
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
        return statNode;

    }

    /**
     * Syntax:
     * goto: c=4+9i
     */
    public Node labeledStatement(){

        return null;
    }

    /**
     * Syntax: GOSUB label
     * @return GoSubNode
     * @throws Exception
     */
    public Node gosubStatement() throws Exception {
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
    public Node returnStatement(){
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
    public Node forStatement() throws Exception {
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
    public Node nextStatement() throws Exception {
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
    public Node assignment() throws Exception {
        VariableNode varNode;
        AssignmentNode assignmentNode;
        Token t = matchAndRemove(Token.Type.IDENTIFIER);  //check to see first token: Identifier?
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
    public Node dataStatement() throws Exception {
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
        if(t1 != null)
            return parseFactor();

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
    public Node inputStatement() throws Exception {
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
     */
    public Node printStatement() throws Exception {
        Token t = matchAndRemove(Token.Type.PRINT);
        if(t == null) return null;
        List<Node> list = printList();
        System.out.println(list.toArray());
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
        while(matchAndRemove(Token.Type.COMMA) != null){
            Node n = getNodeForPrintList();
            list.add(n);
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
        //FOR A = 0 TO 10 STEP 2
        List<Token> tokenList = new ArrayList<>();      //manually adding the token
        tokenList.add(new Token(Token.Type.FOR));
        tokenList.add(new Token(Token.Type.IDENTIFIER, "A"));
        tokenList.add(new Token(Token.Type.EQUAL));
        tokenList.add(new Token(Token.Type.NUMBER, "0"));
        tokenList.add(new Token(Token.Type.TO));
        tokenList.add(new Token(Token.Type.NUMBER, "10"));
        tokenList.add(new Token(Token.Type.STEP));
        tokenList.add(new Token(Token.Type.NUMBER, "2"));
        Parser parser = new Parser(tokenList);
        System.out.println(parser.parse());

        //NEXT A
        List<Token> tokenList2 = new ArrayList<>();
        Parser parser2 = new Parser(tokenList2);
        tokenList2.add(new Token(Token.Type.NEXT));
        tokenList2.add(new Token(Token.Type.IDENTIFIER, "A"));
        System.out.println((parser2.parse()));

        //GOSUB GOTO
        List<Token> tokenList3 = new ArrayList<>();
        Parser parser3 = new Parser(tokenList3);
        tokenList3.add(new Token(Token.Type.GOSUB));
        tokenList3.add(new Token(Token.Type.IDENTIFIER, "GOTO:"));
        System.out.println((parser3.parse()));


    }

}

