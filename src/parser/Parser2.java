package parser;

import java.util.ArrayList;
import java.util.List;

public class Parser2 {

        private List<Token> tokenList = new ArrayList<Token>();


        public Parser2(List<Token> tokenList){
            this.tokenList = tokenList;
        }

        /**
         * parse the tokenList to AST
         * @return MathOpNode
         */
        public Node parse() throws Exception {

            return parseExpression();
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

            return getRightExpression(exprNode);
        }

        public Node getRightExpression(Node left) throws Exception {
            Token token = new Token();
            Node opNode = null;
            if(matchAndRemove(Token.Type.PLUS) != null){token.setType(Token.Type.PLUS);}
            if(matchAndRemove(Token.Type.MINUS) != null){token.setType(Token.Type.MINUS);}
            if(token.getType() == null) return left;

            Node secondTerm = parseTerm();
            if(secondTerm == null)
                throw new Exception("Found + and - but the expression is not completed!");

            if(token.getType() == Token.Type.PLUS)
                opNode = new MathOpNode(MathOpNode.Opcode.ADD, left, secondTerm);
            else if(token.getType() == Token.Type.MINUS)
                opNode = new MathOpNode(MathOpNode.Opcode.MINUS, left, secondTerm);

            return getRightExpression(opNode);
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
            return getRightTerm(termNode);
        }

        public Node getRightTerm(Node left) throws Exception {
            Token token = new Token();
            Node opNode = null;
            if(matchAndRemove(Token.Type.TIME) != null){token.setType(Token.Type.TIME);}
            if(matchAndRemove(Token.Type.DIVIDE) != null){token.setType(Token.Type.DIVIDE);}
            if(token.getType() == null) return left;

            Node secondFactor = parseFactor();
            if(secondFactor == null) throw new Exception("Found * and / but the expression is not completed!");
            if(token.getType() == Token.Type.TIME)
                opNode = new MathOpNode(MathOpNode.Opcode.MULTI, left, secondFactor);
            else if(token.getType() == Token.Type.DIVIDE)
                opNode = new MathOpNode(MathOpNode.Opcode.DIVIDE, left, secondFactor);

            return getRightTerm(opNode);
        }

        /**
         * Factor: {-}NUM || LPAREN Expression RPAREN
         * @return
         */
        public Node parseFactor() throws Exception {
            Node node = null;
            Node exprNode = null;
            Token token = this.tokenList.get(0);
            if(matchAndRemove(Token.Type.NUMBER) != null){   //if token is number
                if(isInteger(token.getTokenValue()))
                    node = new IntegerNode(Integer.parseInt(token.getTokenValue()));

                else if(isFloat(token.getTokenValue()))
                    node = new FloatNode(Float.parseFloat(token.getTokenValue()));
            }
            else if(matchAndRemove(Token.Type.LPAREN) != null){   //( + expression + )
                exprNode = parseExpression();
                if(node == null) throw new Exception("Found left Parenthesis but no expression!");
                if(matchAndRemove(Token.Type.RPAREN) != null)
                    return exprNode;
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
            Token removedToken = new Token();
            if(tokenList.get(0).type == type){
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
        tokenList.add(new Token(Token.Type.NUMBER, "23"));
        tokenList.add(new Token(Token.Type.TIME));
        tokenList.add(new Token(Token.Type.NUMBER, "0.9"));
        tokenList.add(new Token(Token.Type.TIME));
        tokenList.add(new Token(Token.Type.NUMBER, "-23"));



        Parser2 parser = new Parser2(tokenList);
        System.out.println(parser.parse());

    }
    }


