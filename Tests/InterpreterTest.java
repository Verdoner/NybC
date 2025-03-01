import ASTNode.*;
import AntlrGenFiles.NybCLexer;
import AntlrGenFiles.NybCParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class InterpreterTest {

    private Pair<NybCStack,ProgramNode> setupInterpreter(String input){
        var inputStream = CharStreams.fromString(input);
        var lexer = new NybCLexer(inputStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new NybCParser(tokenStream);
        var parseTree = parser.program();
        var ASTVisitor = new ToASTVisitor();
        ProgramNode correctAST = (ProgramNode) ASTVisitor.visit(parseTree);

        NybCStack nybCStack = new NybCStack();

        var initialVisitor = new InitialVisitor(nybCStack,new ArrayList<>());
        initialVisitor.Visit(correctAST);

        ASTVisitor astVisitor = new ASTVisitor(nybCStack);
        astVisitor.Visit(correctAST);

        return new Pair<NybCStack,ProgramNode>(nybCStack,correctAST);
    }

    @Test
    void visitLoopNode(){
        // Test for while loop

        // make the code and get the return stack and AST
        Pair<NybCStack,ProgramNode> returnPair = setupInterpreter("var x = 0; begin loop(x < 10); x = x + 1; end loop;");
        NybCStack nybCStack = returnPair.a;
        ProgramNode AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        Interpreter interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        // Test that the loop has completed and corectly incremented x
        Assertions.assertEquals(10,(int) nybCStack.GetVariableOnStack("x", null));


        // Test for for loop
        // make the code and get the return stack and AST
        returnPair = setupInterpreter("var y = 0; begin loop(var x = 0;x < 10; x = x + 1); y = y + 1; end loop;");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        // Test that the loop has completed and corectly incremented y
        Assertions.assertEquals(10,(int) nybCStack.GetVariableOnStack("y", null));


        // Test for Do while Loop
        // make the code and get the return stack and AST
        returnPair = setupInterpreter("var z = 0; begin loop; z = z + 1; end loop(z<10);");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        // Test that the loop has completed and corectly incremented z
        Assertions.assertEquals(10,(int) nybCStack.GetVariableOnStack("z", null));


    }

    @Test
    void visitSwitchNode(){
        // Test for switch with two cases' that have a break and a default

        // make the code and get the return stack and AST
        Pair<NybCStack,ProgramNode> returnPair = setupInterpreter("var x = 2; begin switch(x); case 1: x = x+1; break; case 2: x = x+2; break; default: x = x+3; break; end switch;");
        NybCStack nybCStack = returnPair.a;
        ProgramNode AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        Interpreter interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(4,(int) nybCStack.GetVariableOnStack("x", null));

        // Test for switch with two cases' that have no break and a default

        // make the code and get the return stack and AST
        returnPair = setupInterpreter("var x = 1; begin switch(x); case 1: x = x+1; case 2: x = x+2; default: x = x+3; end switch;");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(7,(int) nybCStack.GetVariableOnStack("x", null));

        // Test for switch with only a default

        // make the code and get the return stack and AST
        returnPair = setupInterpreter("var x = 1; begin switch(x);default: x = x+3; end switch;");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(4,(int) nybCStack.GetVariableOnStack("x", null));
    }


    @Test
    void avisitAssignNode(){
    // Test for assignment int

        // make the code and get the return stack and AST
        Pair<NybCStack,ProgramNode> returnPair = setupInterpreter("var x = 2;");
        NybCStack nybCStack = returnPair.a;
        ProgramNode AST = returnPair.b;

        Interpreter interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(2,(int) nybCStack.GetVariableOnStack("x", null));

    // Test for assignment float
        // make the code and get the return stack and AST
        returnPair = setupInterpreter("var x = 2.4;");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(2.4f,(float) nybCStack.GetVariableOnStack("x", null));


    // Test for assignment string
        returnPair = setupInterpreter("var x = \"Hello\";");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals("Hello",nybCStack.GetVariableOnStack("x", null));

    // Test for assignment boolean
        returnPair = setupInterpreter("var x = true;");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(true,nybCStack.GetVariableOnStack("x", null));


    // test for assigment to bool with uops
        returnPair = setupInterpreter("var x = !true;");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(false,nybCStack.GetVariableOnStack("x", null));


    }

    @Test
    void visitCallFuncNode(){

    // Test for a custom function
        // make the code and get the return stack and AST
        Pair<NybCStack,ProgramNode> returnPair = setupInterpreter("begin function add2ToVar(var x); x = x + 2; return x; end function; var y = 2; y = add2ToVar(y);");
        NybCStack nybCStack = returnPair.a;
        ProgramNode AST = returnPair.b;

        Interpreter interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(4,(int) nybCStack.GetVariableOnStack("y", null));

    // Test for a custom function with multiple parameters
        returnPair = setupInterpreter("begin function add2ToVar(var x, var z); x = x + 2; x = x + z; return x; end function; var y = 2; y = add2ToVar(y,2);");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(6,(int) nybCStack.GetVariableOnStack("y", null));


    // Test for a custom function with no parameters
        returnPair = setupInterpreter("begin function returnVar(); return 2; end function; var y; y = returnVar();");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(2,(int) nybCStack.GetVariableOnStack("y", null));

    // Test for a custom function with the name of a reserved keyword
        returnPair = setupInterpreter("begin function out(); return 2; end function; var y; y = returnVar();");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        List<String> keywords = new ArrayList<>();
        keywords.add("out");
        interpreter = new Interpreter(nybCStack,keywords);

        Interpreter TMPInterpreter = interpreter;
        ProgramNode TMPAST = AST;
        Assertions.assertThrows(RuntimeException.class, () -> {
            TMPInterpreter.Visit(TMPAST);
        });

    }

    @Test
    void visitArrayAccessNode(){

    // Test for array access with int array
        // make the code and get the return stack and AST
        Pair<NybCStack,ProgramNode> returnPair = setupInterpreter("var x = [0,1,2]; var y = 2 + x[1];");
        NybCStack nybCStack = returnPair.a;
        ProgramNode AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        Interpreter interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(3,(int) nybCStack.GetVariableOnStack("y", null));

    // Test for array access with string
        returnPair = setupInterpreter("var x = [\"Hello\",\"There\"]; var y = 2 + x[1];");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals("2There",nybCStack.GetVariableOnStack("y", null));

    // Test for array access with string
        returnPair = setupInterpreter("var x = [2.9]; var y = 2 + x[0];");
        nybCStack = returnPair.a;
        AST = returnPair.b;

        // setup the interpreter and make it visit the AST
        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals(4.9f,nybCStack.GetVariableOnStack("y", null));

    // Test for array access with string, int and float
        returnPair = setupInterpreter("var x = [\"Hello\",2,2.5]; var y = 2 + x[0]; var z = 2 + x[1]; var a = 2 + x[2];");
        nybCStack = returnPair.a;
        AST = returnPair.b;


        // setup the interpreter and make it visit the AST
        interpreter = new Interpreter(nybCStack,new ArrayList<>());
        interpreter.Visit(AST);

        Assertions.assertEquals("2Hello",nybCStack.GetVariableOnStack("y", null));
        Assertions.assertEquals(4,nybCStack.GetVariableOnStack("z", null));
        Assertions.assertEquals(4.5f,nybCStack.GetVariableOnStack("a", null));



    }


}