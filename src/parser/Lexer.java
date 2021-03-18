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
        LETTER, DIGIT, WHITESPACE, SYMBOL, LABEL, PRINT, QUOTATION;
    }

    HashMap<String, Token.Type> keywords = new HashMap<String, Token.Type>();    //use to store the known word

    public List<Token> lex(String input) {

        buildKeywords();    //initializing all the known words
        State state = State.START;
        String tokenValue = "";
        char[] charArr = input.toCharArray();

        if (input.isBlank()) {
            Token token = new Token();
            token.setType(null);
            token.setTokenValue("");
            tokenList.add(token);
            return tokenList;
        }

        //read the char by char from the input of each line
       //for (char c : charArr) {
        for (int i = 0; i < charArr.length; i++) {
            CharacterClass characterClass = null;
            char c = charArr[i];
            if(Character.isDigit(c))
                characterClass = CharacterClass.DIGIT;
            else if(isSymbol(c))
                characterClass = CharacterClass.SYMBOL;
            else if(Character.isSpaceChar(c))
                characterClass = CharacterClass.WHITESPACE;
            else if(Character.isLetter(c))
                characterClass = CharacterClass.LETTER;
            else if(c == ':')
                characterClass = CharacterClass.LABEL;
            else if(c == '"'){
                characterClass = CharacterClass.QUOTATION;
            }


            switch (state) {
                case START:
                    switch (characterClass){
                        case DIGIT:
                            tokenValue = "" + c;
                            state = State.NUMBER;
                            break;

                        case WHITESPACE:
                            state = State.WHITESPACE;
                            break;

                        case SYMBOL:
                            //state = State.SYMBOL;
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

                        case LETTER:
                            tokenValue = "" + c;
                            state = State.IDENTIFIER;
                            break;

                        case QUOTATION:
                            tokenValue = "" + c;
                            break;

                    }
                    break;

                case NUMBER:
                    switch (characterClass){
                        case DIGIT:
                            tokenValue += c;    //adding the value to numtoken
                            break;

                        case SYMBOL:
                            Token numToken = new Token(Token.Type.NUMBER);
                            numToken.setTokenValue(tokenValue);
                            tokenList.add(numToken);
                           // tokenValue = "" + c;    //reset the tokenValue
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
                            if (!Character.isDigit(c) || i != charArr.length) {     //note: try not to read the last space
                                Token currentToken = new Token(Token.Type.NUMBER);
                                currentToken.setTokenValue(tokenValue);
                                tokenList.add(currentToken);
                                tokenValue = "";
                            }
                            break;
                    }

                case IDENTIFIER:
                    switch (characterClass){
                        case LETTER:
                            tokenValue += c;
                            break;
                        case WHITESPACE:
                            Token currentToken = new Token(Token.Type.IDENTIFIER);
                            currentToken.setTokenValue(tokenValue);
                            tokenList.add(currentToken);
                            tokenValue = "";
                            break;
                        case LABEL:
                            Token labelToken = new Token(Token.Type.LABEL);
                            labelToken.setTokenValue(tokenValue);
                            tokenList.add(labelToken);
                            tokenValue = "";
                            break;
                    }

                case STRING:
                    switch (characterClass){
                        case QUOTATION:
                            tokenValue += c;
                            break;
                    }
            }


        }
//        switch (state){
//            case NUMBER:
//                Token numToken = new Token(Token.Type.NUMBER);
//                numToken.setTokenValue(tokenValue);
//                tokenList.add(numToken);
//                break;
//
//            case WHITESPACE:
//                tokenValue = "";
//                state = State.START;
//                break;
//
//        }

        return tokenList;
    }


    /**
     * Building the keywords (known word)
     */
    private void buildKeywords(){
        keywords.put("PRINT", Token.Type.PRINT);
        keywords.put("READ", Token.Type.READ);
        keywords.put("DATA", Token.Type.DATA);
        keywords.put("INPUT", Token.Type.INPUT);
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


    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/Users/lingxiaodudu/IdeaProjects/Parser/src/parser/test");
        //Path path = Paths.get(filename[0]);
        List<String> content = Files.readAllLines(path, Charset.forName("UTF-8"));
        Lexer lexer = new Lexer();
        for(String line: content){
            for(Token token: lexer.lex(line))
                System.out.print(token + "\t");
        }

    }

}

//