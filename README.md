# Basic-Interpreter
## consists of {Lexer, Parser, Interpreter}


### Lexer

   -- design by the technique of state machine
   
   -- Generate the tokens/lexmes (from the input file) to the tokenList 
   
   -- EX: a = b + c  ==>  IDENTIFY(a) EQUAL IDENTIFY(b) PLUS IDENTIFY(c)
   
   
 ### Parser

   -- Iterate over the lexemes/tokens (tokenList) and output the abstract syntax tree(AST)
       EX: 4 + 5 * 6 => 
       
                        +
                       / \
                      4   *
                         / \                       
                        5   6


### Interpreter

   -- Optimize the AST while doing the semantic analysis 
   
 ------------------
 ### How to COMPILE and RUN
 
   1. Inside of the folder, 2 files for testing. "test.txt" is the test file for Lexer. "parserTest.txt" is the test file for both Parser and Interpreter
   2. Editing the proper Basic programming language into the file before running the program




-------------------------------------------------
- Input Syntax For Lexer (provided with examples): (for now it's good to have the space in-between each token)

  function: x = LEFT$("99" , 9) 
  
  math operation: a = 1 / ( 2.8 + a ) 
  
  string type: "hello world"
  
  label: GOTO:
  
  print something on the console: str$ = "hello" PRINT c , 7, str. //the value of c (default should be 0), 7, string value of str$ 

--------------------
  - DATA Type:
  
     STRING(word with the quote) || INTEGER || FLOAT || IDENTIFIER(word without the quote)

     Variable Type:

     var => integer value ||  var$ => string value   ||   var% = float value
  
  ---------------
  - Usable Functions

      RANDOM() – returns a random integer   
      
      LEFT$(string, int) – returns the leftmost N characters from the string  
      
      RIGHT$(string, int) – returns the rightmost N characters from the string
      
      MID$(string,int, int) – returns the characters of the string, starting from the 2nd argument and taking the 3rd argument as the count
      
      MID$(“Albany”,2,3) = “ban”   
      
      NUM$(int or float) – converts a number to a string 
      
      VAL(string) – converts a string to an integer
      
      VAL%(string) – converts a string to a float
      
 
