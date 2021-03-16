package parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Basic {

    public static void main(String[] filename) throws Exception {
//        System.out.println("------------------------------------The Input format must be consistent! Ex: <12 + -12 = 0 > and always needs a tab at the end of each line!------------------------------------------");
//        System.out.println("-----------------!!!Without the tab at the end of line will cause the last token to be overwritten!!!--------------------------------------");
//        Path path = Paths.get("C:\\Users\\Carter Du\\Downloads\\ICSI311Project\\src\\lexer3\\testfile.txt");
//        //Path path = Paths.get(filename[0]);
//        List<String> content = Files.readAllLines(path, Charset.forName("UTF-8"));
//        Lexer lexer = new Lexer();
//
//        for(String line: content){
//            for(Token token: lexer.lex(line))
//                System.out.print(token + "\t");
//        }

//        List<Token> tokenList = new ArrayList<>();      //manually adding the token
//        tokenList.add(new Token(Token.Type.NUMBER, "23"));
//        tokenList.add(new Token(Token.Type.TIME));
//        tokenList.add(new Token(Token.Type.NUMBER, "0.9"));
//        tokenList.add(new Token(Token.Type.TIME));
//        tokenList.add(new Token(Token.Type.NUMBER, "-23"));

        List<Token> tokenList = new ArrayList<>();      //manually adding the token
        tokenList.add(new Token(Token.Type.PRINT, "PRINT"));
        tokenList.add(new Token(Token.Type.IDENTIFIER, "a"));
        tokenList.add(new Token(Token.Type.EQUAL));
        tokenList.add(new Token(Token.Type.NUMBER, "2"));
        tokenList.add(new Token(Token.Type.PLUS));
        tokenList.add(new Token(Token.Type.NUMBER, "2"));


        Parser parser = new Parser(tokenList);
        System.out.println(parser.parse());


    }
}
