package parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;


public class Lexer {
    List<Token> tokenList = new ArrayList<Token>();

    public enum State{
        START, NUMBER, STRING, IDENTIFIER, SYMBOL, WHITESPACE;
    }

    public enum CharacterClass{
        LETTER, DIGIT, WHITESPACE, SYMBOL, LABEL, QUOTATION, DECIMAL;
    }

    HashMap<String, Token.Type> keywords = new HashMap<String, Token.Type>();    //use to store the known word

    public List<Token> lex(String input) throws Exception {

        buildKeywords();    //initializing all the known words
        State state = State.START;
        String tokenValue = "";
        char[] charArr = input.toCharArray();   //store each char from input to charArr

        if (input.isBlank()) {
            Token token = new Token();
            token.setType(null);
            token.setTokenValue("");
            tokenList.add(token);
            return tokenList;
        }

        //read the char by char from the input of each line
        for (int i = 0; i < charArr.length; i++) {
            CharacterClass characterClass = null;
            char c = charArr[i];
            if(Character.isDigit(c))      // || isSymbol(c)
                characterClass = CharacterClass.DIGIT;
            else if(isSymbol(c))
                characterClass = CharacterClass.SYMBOL;
            else if(Character.isSpaceChar(c))
                characterClass = CharacterClass.WHITESPACE;
            else if(Character.isLetter(c) || c == '"' || c == ',')
                characterClass = CharacterClass.LETTER;
            else if(c == ':')
                characterClass = CharacterClass.LABEL;
            else if(c == '.')
                characterClass = CharacterClass.DECIMAL;

            switch (state) {
                case START:
                    switch (characterClass){
                        case DIGIT:
                            tokenValue = "" + c;
                            state = State.NUMBER;
                            break;

                        case WHITESPACE:
                            state = State.START;
                            break;

                        case SYMBOL:
                            tokenValue = "" + c;
                            state = State.SYMBOL;
                            break;

                        case LETTER:
                            tokenValue = "" + c;
                            state = State.IDENTIFIER;
                            break;

                        case QUOTATION:
                            tokenValue = "" + c;
                            state = State.STRING;
                            break;
                    }
                    break;

                case SYMBOL:
                    switch (characterClass){
                        case SYMBOL:
                            tokenValue += c;
                            break;

                        case WHITESPACE:
                            if(tokenValue.equals("+"))
                                tokenList.add(new Token(Token.Type.PLUS));
                            else if(tokenValue.equals("-"))
                                tokenList.add(new Token(Token.Type.MINUS));
                            else if(tokenValue.equals("*"))
                                tokenList.add(new Token(Token.Type.TIME));
                            else if(tokenValue.equals("/"))
                                tokenList.add(new Token(Token.Type.DIVIDE));
                            else if(tokenValue.equals("="))
                                tokenList.add(new Token(Token.Type.EQUAL));
                            else if(tokenValue.equals("<"))
                                tokenList.add(new Token(Token.Type.LESS));
                            else if(tokenValue.equals(">"))
                                tokenList.add(new Token(Token.Type.GREATER));
                            else if(tokenValue.equals("("))
                                tokenList.add(new Token(Token.Type.LPAREN));
                            else if(tokenValue.equals(")"))
                                tokenList.add(new Token(Token.Type.RPAREN));
                            else
                                throw new Exception("Invalid Symbol Detected!");
                            state = State.START;
                            break;

                        case DIGIT:
                            if(c == '+'){
                                Token opToken = new Token(Token.Type.PLUS);
                                tokenList.add(opToken);
                            }
                            else if(c == '-'){
                                Token opToken = new Token(Token.Type.MINUS);
                                tokenList.add(opToken);
                            }
                            else if(c == '*'){
                                Token opToken = new Token(Token.Type.TIME);
                                tokenList.add(opToken);
                            }
                            else if(c == '/'){
                                Token opToken = new Token(Token.Type.DIVIDE);
                                tokenList.add(opToken);
                            }
                            else if(c == '='){
                                Token opToken = new Token(Token.Type.EQUAL);
                                tokenList.add(opToken);
                            }
                            else if(c == '('){
                                Token opToken = new Token(Token.Type.LPAREN);
                                tokenList.add(opToken);
                            }
                            else if(c == ')'){
                                Token opToken = new Token(Token.Type.RPAREN);
                                tokenList.add(opToken);
                            }
                            state = State.NUMBER;
                            break;
                    }
                    break;


                case NUMBER:
                    switch (characterClass){
                        case DIGIT:
                            tokenValue += c;    //adding the value to numtoken
                            break;

                        case DECIMAL:
                            tokenValue = tokenValue + ".";
                            break;

                        case SYMBOL:
                            Token numToken = new Token(Token.Type.NUMBER);
                            numToken.setTokenValue(tokenValue);
                            tokenList.add(numToken);
                            //tokenValue = "" + c;    //reset the tokenValue
                            tokenValue = "";
                            if(c == '+'){
                                Token opToken = new Token(Token.Type.PLUS);
                                tokenList.add(opToken);
                            }
                            else if(c == '-'){
                                Token opToken = new Token(Token.Type.MINUS);
                                tokenList.add(opToken);
                            }
                            else if(c == '*'){
                                Token opToken = new Token(Token.Type.TIME);
                                tokenList.add(opToken);
                            }
                            else if(c == '/'){
                                Token opToken = new Token(Token.Type.DIVIDE);
                                tokenList.add(opToken);
                            }
                            else if(c == '='){
                                Token opToken = new Token(Token.Type.EQUAL);
                                tokenList.add(opToken);
                            }
                            else if(c == '('){
                                Token opToken = new Token(Token.Type.LPAREN);
                                tokenList.add(opToken);
                            }
                            else if(c == ')'){
                                Token opToken = new Token(Token.Type.RPAREN);
                                tokenList.add(opToken);
                            }
                            break;


                        case WHITESPACE:
                            tokenList.add(new Token(Token.Type.NUMBER, tokenValue));
                            state = State.START;
                            break;
                    }
                    break;

                    //buffer = PRINT
                    //check the hashmap, if(buffer = hash.get(buffer)),
                case IDENTIFIER:
                    switch (characterClass){
                        case LETTER:
                            if(c == '"'){   //close quotation for String
                                tokenValue = tokenValue + "\"";
                                state = State.STRING;
                            }
                            else
                                tokenValue += c;
                            break;

                        case SYMBOL:    //deal with AssignmentStatement (ex: x=x+3)
                            tokenList.add(new Token(Token.Type.IDENTIFIER, tokenValue));
                            tokenValue="";
                            if(c == '='){
                                tokenList.add(new Token(Token.Type.EQUAL));
                            }
                            else if(c == '+')
                                tokenList.add(new Token(Token.Type.PLUS));
                            break;


                        case DIGIT:
                            state = State.NUMBER;
                            break;

                        case WHITESPACE:
                            generateKeywordToken(tokenValue);
                            state = State.START;
                            break;

                        case LABEL:
                            Token labelToken = new Token(Token.Type.LABEL);
                            labelToken.setTokenValue(tokenValue);
                            tokenList.add(labelToken);
                            tokenValue = "";
                            break;
                    }
                    break;

                case STRING:
                    switch (characterClass){
                        case WHITESPACE:
                            tokenList.add(new Token(Token.Type.STRING, tokenValue));
                            state = State.START;
                            break;
                    }
                    break;

                case WHITESPACE:
                    break;
            }


        }
        //for end of each line
        switch (state){
            case NUMBER:
                Token numToken = new Token(Token.Type.NUMBER);
                numToken.setTokenValue(tokenValue);
                tokenList.add(numToken);
                break;

            case IDENTIFIER:
                Token wordToken = new Token(Token.Type.IDENTIFIER);
                wordToken.setTokenValue(tokenValue);
                tokenList.add(wordToken);
                break;

            case STRING:
                Token strToken = new Token(Token.Type.STRING);
                strToken.setTokenValue(tokenValue);
                tokenList.add(strToken);
                break;

            case SYMBOL:
                if(tokenValue.equals("+")){
                    Token opToken = new Token(Token.Type.PLUS);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals("-")){
                    Token opToken = new Token(Token.Type.MINUS);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals("*")){
                    Token opToken = new Token(Token.Type.TIME);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals("/")){
                    Token opToken = new Token(Token.Type.DIVIDE);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals("=")){
                    Token opToken = new Token(Token.Type.EQUAL);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals("(")){
                    Token opToken = new Token(Token.Type.LPAREN);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals(")")){
                    Token opToken = new Token(Token.Type.RPAREN);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals("<")){
                    Token opToken = new Token(Token.Type.LESS);
                    tokenList.add(opToken);
                }
                else if(tokenValue.equals(">")){
                    Token opToken = new Token(Token.Type.GREATER);
                    tokenList.add(opToken);
                }
                break;

            case WHITESPACE:
                //tokenList.add(null);
                System.out.println(9);
                break;
        }

        return tokenList;
    }


    /**
     * Building the keywords (known word)
     */
    private void buildKeywords(){
        keywords.put(",", Token.Type.COMMA);
        keywords.put("PRINT", Token.Type.PRINT);
        keywords.put("READ", Token.Type.READ);
        keywords.put("DATA", Token.Type.DATA);
        keywords.put("INPUT", Token.Type.INPUT);
        keywords.put("RETURN", Token.Type.RETURN);
        keywords.put("NEXT", Token.Type.NEXT);
        keywords.put("FOR", Token.Type.FOR);
        keywords.put("TO", Token.Type.TO);
        keywords.put("STEP", Token.Type.STEP);
        keywords.put("IF", Token.Type.IF);
        keywords.put("THEN", Token.Type.THEN);
        //BUILD-IN FUNCTIONS
        keywords.put("RANDOM", Token.Type.FUNCTION);
        keywords.put("LEFT$", Token.Type.FUNCTION);
        keywords.put("RIGHT$", Token.Type.FUNCTION);
        keywords.put("MID$", Token.Type.FUNCTION);
        keywords.put("NUM$", Token.Type.FUNCTION);
        keywords.put("VAL", Token.Type.FUNCTION);
        keywords.put("VAL%", Token.Type.FUNCTION);
    }


    public void generateKeywordToken(String s) {
        if(keywords.containsKey(s)){
            if(s.equals("PRINT"))
                tokenList.add(new Token(Token.Type.PRINT));
            else if(s.equals(","))
                tokenList.add(new Token(Token.Type.COMMA));
        }
        else {
            tokenList.add(new Token(Token.Type.IDENTIFIER, s));
        }
    }

    /**
     * Determine whether the chaacter is the digit(number)
     * @param c
     * @return
     */
    private char digit(char c){
        if (Character.isDigit(c)) {
            return c;
        }
        return ' ';
    }

    /**
     * Determine the character is the symbol like + - * / = ( ) > <
     * @param c
     * @return
     */
    private boolean isSymbol(char c){
        if (c=='+' || c=='-' || c=='*' || c=='/' || c=='=' || c=='(' || c==')' || c=='>' || c=='<') {
            return true;
        }
        return false;
    }

    /**
     * Determine the character containing the decimal point
     * @param c
     * @return
     */
    private boolean isDecimal(char c){
        if(c == '.')
            return true;

        return false;
    }

    /**
     * Determine the character is the space
     * @param c
     * @return
     */
    private boolean isSpace(char c){
        if (Character.isSpaceChar(c)) {
            return true;
        }
        return false;
    }


    public static void main(String[] args) throws Exception {
        Path path = Paths.get("/Users/lingxiaodudu/IdeaProjects/Parser/src/parser/test");
        //Path path = Paths.get(filename[0]);
        List<String> content = Files.readAllLines(path, Charset.forName("UTF-8"));

        System.out.println("Content in the file: ");
        System.out.println("---------------------------------------");
        for (int i = 0; i < content.size(); i++) {
            System.out.println(content.get(i));
        }

        System.out.println();
        System.out.println();

        System.out.println("TokenList: ");
        System.out.println("---------------------------------------");
        for(int i = 0; i < content.size(); i++){
            for(Token token: new Lexer().lex(content.get(i))) {
                System.out.print(token + "\t");
            }
            System.out.println();
        }

    }
}


//lexer can not deal with input like x=3+3?

//state of Identifier: deal with digit, symbol
//ex: x=3+3, x=a+3, x=3

//FUNCTION <> add to token, hashmap? constructor?

//should i create character class for PRINT DATA sort of?
//for ifNode, can that be if x=5 then print 5?
//boolean expression can have many constructor? since x>5 or x>y

//switch(state){
//    case Identiifer:
//        if(characterClass == letter)
//            buffer +=c;
//        }
//        else{
//            generateIdentifierToken(buffer);
//            buffer = "";
//        }
//        }
//        void generateIdentiferToken(String s){
//            Token t = Hashmap.get(s);
//            if(t == null)
//                t = new Token(Identifer);
//            else
//                s = null;
//            list.add(new Token(t, s));
//        }