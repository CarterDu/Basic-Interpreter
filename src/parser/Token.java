package parser;

import java.util.InputMismatchException;
import java.util.MissingFormatArgumentException;

/**
 * @author Carter Du
 * Define categories of tokens
 */
public class Token {
    public enum Type{
        NUMBER("NUMBER"), PLUS("+"), MINUS("-"), TIME("*"), DIVIDE("/"),
        EQUAL("="), END("END"), LESS("<"), GREATER(">"), NOTEQUALS("<>"),
        GREATERANDEQUALS(">="), LESSANDEQUALS("<="), LPAREN("("), RPAREN(")"),
        IDENTIFIER(), PRINT(), LABEL("LABEL"), STRING(), START, COMMA, READ, DATA, INPUT, FOR,
        NEXT, RETURN, GOSUB, TO, STEP, IF, THEN, FUNCTION;
        private String value;
        private Type(){}
        private Type(String value){
            this.value = value;
        }
    }

    public Type type;
    public String tokenValue;

    public Token(){}

    public Token(Type type){ this.type =type; }

    public Token(Type type, String tokenValue){
        this.type = type;
        this.tokenValue = tokenValue;
    }

    public String toString(){
            if(type == Type.NUMBER) {
                return "NUMBER(" + tokenValue + ")";
            }
            else if(type == Type.IDENTIFIER) {
                return "IDENTIFIER(" + tokenValue + ")";
            }
            else if(type == Type.LABEL) {
                return "LABEL(" + tokenValue + ")";
            }
            else if(type == Type.PRINT) {
                return "PRINT";
            }
            else if(type == Type.STRING){
                return "STRING(" + tokenValue + ")";
            }
            else if(type == Type.FUNCTION){
                if(tokenValue.equals("RANDOM"))
                    return "RANDOM()";
                else
                    return "";
            }
            else if(type == null)
                return "";
            else{
                try{
                    return type.toString();
                }
                catch (Exception e){
                    throw new InputMismatchException("Invalid Input here!");
                }
            }
        }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
