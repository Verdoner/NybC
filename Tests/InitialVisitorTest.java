import ASTNode.ProgramNode;
import AntlrGenFiles.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class InitialVisitorTest {

    private ProgramNode setupAST(String input) {
        var inputStream = CharStreams.fromString(input);
        var lexer = new NybCLexer(inputStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new NybCParser(tokenStream);
        var parseTree = parser.program();
        var ASTVisitor = new ToASTVisitor();
        return (ProgramNode) ASTVisitor.visit(parseTree);
    }


    @Test
    void VisitProgramNodeTest() {

        // Can the function be insterted into the function map?
        var AST = setupAST("begin function hello(); var x = 2; end function;");
        NybCStack nybCStack = new NybCStack();
        List<String> keywords = new ArrayList<>();
        keywords.add("out"); keywords.add("in");
        var InitialVisitor = new InitialVisitor(nybCStack,keywords);
        InitialVisitor.Visit(AST);
        Assertions.assertEquals("{hello={0=FuncNode{id='hello', param=[], stmtList=[DeclNode{id='x', value=IntNode{value=2}}]}, 1=null}}",nybCStack.FmapToString());

        // Can a function with the same name happen twice?
        Assertions.assertThrows(RuntimeException.class,()->{
            var AST2 = setupAST("begin function hello(); var x = 2; end function; begin function hello(); var x = 2; end function;");
            NybCStack nybCStack2 = new NybCStack();
            List<String> keywords2 = new ArrayList<>();
            keywords2.add("out"); keywords2.add("in");
            var InitialVisitor2 = new InitialVisitor(nybCStack2,keywords2);
            InitialVisitor2.Visit(AST2);
        });


        // Can a function with the same name as a keyword happen?
        Assertions.assertThrows(RuntimeException.class,()->{
            var AST2 = setupAST("begin function out(); var x = 2; end out;");
            NybCStack nybCStack2 = new NybCStack();
            List<String> keywords3 = new ArrayList<>();
            keywords3.add("out"); keywords3.add("in");
            var InitialVisitor2 = new InitialVisitor(nybCStack2,keywords3);
            InitialVisitor2.Visit(AST2);
        });


    }



}
