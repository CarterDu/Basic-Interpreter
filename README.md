# Basic-Interpreter
- consists of {Lexer, Parser, Interpreter}

- Lexer

   • design by the technique of state machine
   • Generate the tokens/lexmes (from the input file)to the tokenList 
   
   
- Parser

   • Iterate over the lexemes/tokens (tokenList) and output the abstract syntax tree(AST)
       EX: 4 + 5 * 6 => 
       
                        +
       
                      4   *
                      
                        5   6


- Interpreter

   • Optimize the AST while doing the semantic analysis 




-------------------------------------------------
- Input Syntax For Lexer (provided with examples): (for now it's good to have the space in-between each token)

  function: x = LEFT$("99" , 9)
  math operation: a = 1 / ( 2.8 + a ) 
  string type: "hello world"
  label: GOTO:
  print something on the console: PRINT c , 7, str$. //the value of c (default should be 0), 7, string value of str$ 

  DATA Type:
  STRING(word with the quote) || INTEGER || FLOAT || IDENTIFIER(word without the quote)

  Variable Type:
  var => integer or float value  ||  var$ => string value
