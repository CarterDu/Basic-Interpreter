package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
        private Node node;
        private List<Token> tokenList = new ArrayList<Token>();
        private boolean isPrintState = false;
        private boolean isAssignState = false;


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
            Node exprNode = null;
            exprNode = parseTerm();
            if(exprNode == null)
                return null;
            if(matchAndRemove(Token.Type.PLUS) != null)
                exprNode = new MathOpNode(MathOpNode.Opcode.ADD, exprNode, parseExpression());
            else if(matchAndRemove(Token.Type.MINUS) != null)
                exprNode = new MathOpNode(MathOpNode.Opcode.MINUS, exprNode, parseExpression());

            return exprNode;
        }

        /**
         * Term: Factor {TIME || DIVIDE} Factor
         * @return
         */
        public Node parseTerm() throws Exception {
            Node termNode = null;
            termNode = parseFactor();
            if(termNode == null)
                return null;
            if(matchAndRemove(Token.Type.TIME) != null)
                termNode = new MathOpNode(MathOpNode.Opcode.MULTI, termNode, parseExpression());

            else if(matchAndRemove(Token.Type.DIVIDE) != null)
                termNode = new MathOpNode(MathOpNode.Opcode.DIVIDE, termNode, parseExpression());

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
                return null;//null?node
            }


    /**
     * handle a single statement & return its node
     * @return
     */
    public Node Statement() throws Exception {
        Node statNode = null;
//            Token token = tokenList.get(0); //check the first token
//
//            if(token.getType() == Token.Type.PRINT) {
//                    statNode = printStatement();
//                    return statNode;
//            }
//            else if(token.getType() == Token.Type.IDENTIFIER) {
//                statNode = assignment();
//                return statNode;
//            }
        statNode = printStatement();
        if (statNode == null)
            statNode = assignment();
//        if(statNode == null)
//            statNode = dataStatement();
//        if(statNode == null)
//            statNode = readStatement();

        return statNode;
    }

    /**
     * accept any numbers of statements
     * and call Statement() until Statement() fails
     * @return
     */
    public Node Statements() throws Exception {
        Node AST = Statement();
        List<Node> list = new ArrayList<>();
        while(AST != null)
            list.add(AST);
        return new StatementsNode(list);
    }


            /**
             * ex: Variable Equals Expr
             * @return
             */
            public Node assignment() throws Exception {
              VariableNode varNode = null;
              AssignmentNode assignmentNode = null;
              Token t = matchAndRemove(Token.Type.IDENTIFIER);  //check to see first token: Identifier?
              if(t != null && matchAndRemove(Token.Type.EQUAL)!=null){
                  isAssignState = true;
                  varNode = new VariableNode(t.tokenValue);
                  assignmentNode = new AssignmentNode(varNode, parseExpression());
                  return assignmentNode;
              }
                 return null;
              }


            /**
             *  accepts a print statement and creates a PrintNode or returns NULL:
             */
              public Node printStatement() throws Exception {
                  PrintNode printNode = new PrintNode(printList());
                  return printNode;
              }

         /**
         * Accept a print list(consisted of commas separated list of expressions)
         * @return  exprNode
        */  //print Id(a) , ID(b)
        public List<Node> printList() throws Exception {
            List<Node> list = new ArrayList<>();
            VariableNode varNode = null;
            if (tokenList.get(0).getType() == Token.Type.PRINT) {
                isPrintState = true;
                tokenList.remove(0);
                  //now this token will be whatever after print
                    if (tokenList.get(0).getType() == Token.Type.IDENTIFIER) {
                        varNode = new VariableNode(tokenList.get(0).getTokenValue());
                        list.add(varNode);
                    }
//                    if(token.getType() == Token.Type.EQUAL){
//                        list.add(new AssignmentNode(varNode, parseExpression()));
//                    }
                }
            else
                isPrintState = false;
            return list;
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
        List<Token> tokenList = new ArrayList<>();      //manually adding the token
        tokenList.add(new Token(Token.Type.PRINT, "PRINT"));
        tokenList.add(new Token(Token.Type.IDENTIFIER, "a"));
        tokenList.add(new Token(Token.Type.IDENTIFIER, "k"));
//        tokenList.add(new Token(Token.Type.EQUAL));
//        tokenList.add(new Token(Token.Type.NUMBER, "2"));
//        tokenList.add(new Token(Token.Type.PLUS));
//        tokenList.add(new Token(Token.Type.NUMBER, "2"));


        Parser parser = new Parser(tokenList);
        System.out.println(parser.parse());
    }

}

