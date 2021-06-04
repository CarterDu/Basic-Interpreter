package parser;



import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Basic{

        public static void main(String[] args) throws Exception {
//                //IF x>5 THEN xIsGREAT
//                List<Token> tokenList0 = new ArrayList<>();
//                Parser parser0 = new Parser(tokenList0);
//                tokenList0.add(new Token(Token.Type.IF));
//                tokenList0.add(new Token(Token.Type.IDENTIFIER, "x"));
//                tokenList0.add(new Token(Token.Type.GREATER));
//                tokenList0.add(new Token(Token.Type.NUMBER, "5"));
//                tokenList0.add(new Token(Token.Type.THEN));
//                tokenList0.add(new Token(Token.Type.IDENTIFIER, "xIsGREAT"));
//                System.out.println((parser0.parse()));
//
//                //NUM(7)
//                List<Token> tokenList = new ArrayList<>();      //manually adding the token
//                Parser parser = new Parser(tokenList);
//                tokenList.add(new Token(Token.Type.FUNCTION, "NUM$"));
//                tokenList.add(new Token(Token.Type.LPAREN));
//                tokenList.add(new Token(Token.Type.NUMBER, "7"));
//                tokenList.add(new Token(Token.Type.RPAREN));
//                System.out.println((parser.parse()));
//
//                //RANDOM()
//                List<Token> tokenList2 = new ArrayList<>();
//                Parser parser2 = new Parser(tokenList2);
//                tokenList2.add(new Token(Token.Type.FUNCTION, "RANDOM"));
//                tokenList2.add(new Token(Token.Type.LPAREN));
//                tokenList2.add(new Token(Token.Type.RPAREN));
//                System.out.println((parser2.parse()));
//
//                //LEFT$("apple", 7)
//                List<Token> tokenList3 = new ArrayList<>();
//                Parser parser3 = new Parser(tokenList3);
//                tokenList3.add(new Token(Token.Type.FUNCTION, "LEFT$"));
//                tokenList3.add(new Token(Token.Type.LPAREN));
//                tokenList3.add(new Token(Token.Type.STRING, "apple"));
//                tokenList3.add(new Token(Token.Type.COMMA));
//                tokenList3.add(new Token(Token.Type.NUMBER, "7"));
//                tokenList3.add(new Token(Token.Type.RPAREN));
//                System.out.println((parser3.parse()));

        Path path = Paths.get("src/parser/test");
        List<String> content = Files.readAllLines(path, Charset.forName("UTF-8"));
//        for(String s: content)
//            System.out.println(s);
//        for (int i = 0; i < content.size(); i++) {
//            System.out.println(new Parser(new Lexer().lex(content.get(i))).parse());
//        }

//
//        List<StatementNode> statementList = new ArrayList<>();
//        for (int i = 0; i < content.size(); i++) {
//            statementList.add((StatementNode) new Parser(new Lexer().lex(content.get(i))).parse());
//        }
//
//        Interpreter interpreter = new Interpreter(statementList);
//        interpreter.initialize();
//        interpreter.interpret();

        }
    }

