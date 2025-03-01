import ASTNode.ProgramNode;
import AntlrGenFiles.NybCLexer;
import AntlrGenFiles.NybCParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class IntergrationTest {


    @Test
    void FullTest() throws Exception {

        // Use the code from FullTest.txt and run it through the lexar and parser

        Path fileName = Path.of("Tests/FullTest.txt");
        var inputStream = CharStreams.fromString(Files.readString(fileName));
        var lexer = new NybCLexer(inputStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new NybCParser(tokenStream);
        var parseTree = parser.program();
        var parseTreevisitor = new ToASTVisitor();

        // Run ToAstVisitor and check if the AST is correct
        ProgramNode AST = (ProgramNode) parseTreevisitor.visit(parseTree);
        String ASTString = AST.toString();
        Assertions.assertEquals("ProgramNode{stmtList=[FuncNode{id='test', param=[DeclNode{id='x', value=null}, DeclNode{id='y', value=null}], stmtList=[DeclNode{id='p', value=BoolNode{value=true}}, DeclNode{id='t', value=IdentifierNode{value='x'}}, DeclNode{id='u', value=IdentifierNode{value='y'}}, DeclNode{id='int', value=IntNode{value=5}}, DeclNode{id='string', value=StringNode{value=Test String}}, DeclNode{id='float', value=5.5}, DeclNode{id='bool', value=BoolNode{value=true}}, DeclNode{id='holder1', value=null}, IfNode{condition=BinaryOpNode{Left=IdentifierNode{value='t'}, Op===, Right=IdentifierNode{value='u'}}, stmts=[AssignNode{Left=IdentifierNode{value='holder1'}, Right=BoolNode{value=true}}], elseIfNodes=IfNode{condition=null, stmts=[AssignNode{Left=IdentifierNode{value='holder1'}, Right=BoolNode{value=false}}], elseIfNodes=null}}, DeclNode{id='holder2', value=null}, IfNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=>, Right=IntNode{value=5}}, stmts=[AssignNode{Left=IdentifierNode{value='holder2'}, Right=BoolNode{value=true}}], elseIfNodes=IfNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=<, Right=IntNode{value=5}}, stmts=[AssignNode{Left=IdentifierNode{value='holder2'}, Right=BoolNode{value=false}}], elseIfNodes=IfNode{condition=null, stmts=[AssignNode{Left=IdentifierNode{value='holder2'}, Right=BoolNode{value=false}}], elseIfNodes=null}}}, LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=<, Right=IntNode{value=10}}, declaration=null, assignment=null, type='while', stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=+, Right=IntNode{value=1}}}]}, LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='p'}, Op=<, Right=IntNode{value=10}}, declaration=DeclNode{id='p', value=IntNode{value=0}}, assignment=AssignNode{Left=IdentifierNode{value='p'}, Right=BinaryOpNode{Left=IdentifierNode{value='p'}, Op=+, Right=IntNode{value=1}}}, type='for', stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=+, Right=IntNode{value=1}}}]}, LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=<, Right=IntNode{value=10}}, declaration=null, assignment=null, type='do-while', stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=+, Right=IntNode{value=1}}}]}, SwitchNode{switchCond=IdentifierNode{value='int'}, cases=[CaseNode{caseExp=IntNode{value=1}, stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=IntNode{value=1}}, CtrlFlowNode{type='break', returnExp=null}]}, CaseNode{caseExp=IntNode{value=2}, stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=IntNode{value=2}}, CtrlFlowNode{type='break', returnExp=null}]}, CaseNode{caseExp=null, stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=IntNode{value=3}}]}]}, DeclNode{id='l', value=ArrayNode{value=[IntNode{value=5}]}}, AssignNode{Left=ArrayAccessNode{id='l', index=0, startLine=58, endLine=58}, Right=IntNode{value=1}}, DeclNode{id='m', value=ArrayAccessNode{id='l', index=0, startLine=60, endLine=60}}, DeclNode{id='n', value=BinaryOpNode{Left=ArrayAccessNode{id='l', index=0, startLine=62, endLine=62}, Op=!=, Right=IdentifierNode{value='m'}}}]}, CallFuncNode{id='test', args=[IntNode{value=3}, IntNode{value=4}]}, DeclNode{id='h', value=CallFuncNode{id='fourtimes2', args=[IntNode{value=3}]}}, DeclNode{id='m', value=BinaryOpNode{Left=IdentifierNode{value='h'}, Op=/, Right=ParenthNode{Inner=BinaryOpNode{Left=IntNode{value=0}, Op=+, Right=IntNode{value=1}}}}}, FuncNode{id='fourtimes2', param=[DeclNode{id='x', value=null}], stmtList=[CtrlFlowNode{type='return', returnExp=BinaryOpNode{Left=IntNode{value=4}, Op=*, Right=IntNode{value=2}}}]}, DeclNode{id='t', value=BoolNode{value=true}}, IfNode{condition=BinaryOpNode{Left=BinaryOpNode{Left=BinaryOpNode{Left=BinaryOpNode{Left=BinaryOpNode{Left=IntNode{value=3}, Op===, Right=IntNode{value=3}}, Op=&&, Right=BinaryOpNode{Left=IntNode{value=2}, Op=!=, Right=IntNode{value=3}}}, Op=||, Right=IdentifierNode{value='t'}}, Op=||, Right=BinaryOpNode{Left=IntNode{value=45}, Op=>=, Right=IntNode{value=3}}}, Op=||, Right=BinaryOpNode{Left=IntNode{value=8}, Op=<=, Right=IntNode{value=3}}}, stmts=[DeclNode{id='b', value=IntNode{value=2}}], elseIfNodes=null}]}"
                , ASTString);

        // Run InitialVisitor and check if the fmap is correct
        List<String> keyWords = new ArrayList<>();
        keyWords.add("out");
        Assertions.assertDoesNotThrow(()->{
            InitialVisitor initialVisitor = new InitialVisitor(new NybCStack(), keyWords);
            initialVisitor.Visit(AST);
        });

        NybCStack nybCStack = new NybCStack();

        InitialVisitor initialVisitor = new InitialVisitor(nybCStack, keyWords);
        initialVisitor.Visit(AST);
        Assertions.assertEquals("{test={0=FuncNode{id='test', param=[DeclNode{id='x', value=null}, DeclNode{id='y', value=null}], stmtList=[DeclNode{id='p', value=BoolNode{value=true}}, DeclNode{id='t', value=IdentifierNode{value='x'}}, DeclNode{id='u', value=IdentifierNode{value='y'}}, DeclNode{id='int', value=IntNode{value=5}}, DeclNode{id='string', value=StringNode{value=Test String}}, DeclNode{id='float', value=5.5}, DeclNode{id='bool', value=BoolNode{value=true}}, DeclNode{id='holder1', value=null}, IfNode{condition=BinaryOpNode{Left=IdentifierNode{value='t'}, Op===, Right=IdentifierNode{value='u'}}, stmts=[AssignNode{Left=IdentifierNode{value='holder1'}, Right=BoolNode{value=true}}], elseIfNodes=IfNode{condition=null, stmts=[AssignNode{Left=IdentifierNode{value='holder1'}, Right=BoolNode{value=false}}], elseIfNodes=null}}, DeclNode{id='holder2', value=null}, IfNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=>, Right=IntNode{value=5}}, stmts=[AssignNode{Left=IdentifierNode{value='holder2'}, Right=BoolNode{value=true}}], elseIfNodes=IfNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=<, Right=IntNode{value=5}}, stmts=[AssignNode{Left=IdentifierNode{value='holder2'}, Right=BoolNode{value=false}}], elseIfNodes=IfNode{condition=null, stmts=[AssignNode{Left=IdentifierNode{value='holder2'}, Right=BoolNode{value=false}}], elseIfNodes=null}}}, LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=<, Right=IntNode{value=10}}, declaration=null, assignment=null, type='while', stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=+, Right=IntNode{value=1}}}]}, LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='p'}, Op=<, Right=IntNode{value=10}}, declaration=DeclNode{id='p', value=IntNode{value=0}}, assignment=AssignNode{Left=IdentifierNode{value='p'}, Right=BinaryOpNode{Left=IdentifierNode{value='p'}, Op=+, Right=IntNode{value=1}}}, type='for', stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=+, Right=IntNode{value=1}}}]}, LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=<, Right=IntNode{value=10}}, declaration=null, assignment=null, type='do-while', stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=BinaryOpNode{Left=IdentifierNode{value='int'}, Op=+, Right=IntNode{value=1}}}]}, SwitchNode{switchCond=IdentifierNode{value='int'}, cases=[CaseNode{caseExp=IntNode{value=1}, stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=IntNode{value=1}}, CtrlFlowNode{type='break', returnExp=null}]}, CaseNode{caseExp=IntNode{value=2}, stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=IntNode{value=2}}, CtrlFlowNode{type='break', returnExp=null}]}, CaseNode{caseExp=null, stmtList=[AssignNode{Left=IdentifierNode{value='int'}, Right=IntNode{value=3}}]}]}, DeclNode{id='l', value=ArrayNode{value=[IntNode{value=5}]}}, AssignNode{Left=ArrayAccessNode{id='l', index=0, startLine=58, endLine=58}, Right=IntNode{value=1}}, DeclNode{id='m', value=ArrayAccessNode{id='l', index=0, startLine=60, endLine=60}}, DeclNode{id='n', value=BinaryOpNode{Left=ArrayAccessNode{id='l', index=0, startLine=62, endLine=62}, Op=!=, Right=IdentifierNode{value='m'}}}]}, x=null, 1=null, y=null}, fourtimes2={0=FuncNode{id='fourtimes2', param=[DeclNode{id='x', value=null}], stmtList=[CtrlFlowNode{type='return', returnExp=BinaryOpNode{Left=IntNode{value=4}, Op=*, Right=IntNode{value=2}}}]}, x=null, 1=null}}",nybCStack.FmapToString());


        // Run Interpreter and check if the stack is correct
        Interpreter interpreter = new Interpreter(nybCStack, keyWords);

        interpreter.Visit(AST);
        System.out.println(nybCStack.StackToString());

        Assertions.assertEquals(8,nybCStack.GetVariableOnStack("h", null));
        Assertions.assertEquals(8.0f,nybCStack.GetVariableOnStack("m", null));
        Assertions.assertEquals(true,nybCStack.GetVariableOnStack("t", null));


    }
}
