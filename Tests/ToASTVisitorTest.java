import ASTNode.*;
import AntlrGenFiles.NybCLexer;
import AntlrGenFiles.NybCParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ToASTVisitorTest {
    private ParseTree setupParseTree(String input) {
        var inputStream = CharStreams.fromString(input);
        var lexer = new NybCLexer(inputStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new NybCParser(tokenStream);
        return parser.program();
    }

    @Test
    void visitFunctionStmtTest() throws  IOException {
        var ASTvisitor = new ToASTVisitor();
        var parseTreeForFunction = setupParseTree("begin function test(var testValue); var x = 4; end function;");

        NybCParser.FunctionStmtContext functionStmtContext =(NybCParser.FunctionStmtContext) parseTreeForFunction.getChild(0);
        FuncNode funcNode = (FuncNode) ASTvisitor.visitFunctionStmt(functionStmtContext);
        Assertions.assertNotNull(funcNode);
        Assertions.assertEquals("FuncNode{id='test', param=[DeclNode{id='testValue', value=null}], stmtList=[DeclNode{id='x', value=IntNode{value=4}}]}", funcNode.toString());
    }


    @Test
    void visitCallStmtTest(){
        var ASTvisitor = new ToASTVisitor();
        var parseTreeForCallStmt = setupParseTree("var x = a(x);");


        CallFuncNode callFuncCtx = (CallFuncNode) ASTvisitor.visitCallStmt((NybCParser.CallStmtContext)
                parseTreeForCallStmt.getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0));

        Assertions.assertNotNull(callFuncCtx);
        Assertions.assertEquals("CallFuncNode{id='a', args=[IdentifierNode{value='x'}]}",callFuncCtx.toString());
    }

    @Test
    void visitBeginStmtTest(){
        var ASTvisitor = new ToASTVisitor();

        //Test For an if statement
        var parseTreeForIf = setupParseTree("begin if(1<2); var x; end if;");
        IfNode ifNodeCtx = (IfNode) ASTvisitor.visitBeginStmt((NybCParser.BeginStmtContext) parseTreeForIf.getChild(0).getChild(0));
        Assertions.assertNotNull(ifNodeCtx);
        assertEquals("IfNode{condition=BinaryOpNode{Left=IntNode{value=1}, Op=<, Right=IntNode{value=2}}, stmts=[DeclNode{id='x', value=null}], elseIfNodes=null}",ifNodeCtx.toString());

        //Test for a while loop
        var parseTreeForLoop = setupParseTree("begin loop(x<10); x = x+1; end loop;");
        LoopNode loopNodeCtx = (LoopNode) ASTvisitor.visitBeginStmt((NybCParser.BeginStmtContext) parseTreeForLoop.getChild(0).getChild(0));
        Assertions.assertNotNull(loopNodeCtx);
        Assertions.assertEquals("LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='x'}, Op=<, Right=IntNode{value=10}}, declaration=null, assignment=null, type='while', stmtList=[AssignNode{Left=IdentifierNode{value='x'}, Right=BinaryOpNode{Left=IdentifierNode{value='x'}, Op=+, Right=IntNode{value=1}}}]}", loopNodeCtx.toString());

        //Test for a for loop
        var parseTreeForForLoop = setupParseTree("begin loop(var x = 0; x<10; x=x+1); a(2); end loop;");
        LoopNode ForLoopNodeCtx = (LoopNode) ASTvisitor.visitBeginStmt((NybCParser.BeginStmtContext) parseTreeForForLoop.getChild(0).getChild(0));
        Assertions.assertNotNull(ForLoopNodeCtx);
        Assertions.assertEquals("LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='x'}, Op=<, Right=IntNode{value=10}}, declaration=DeclNode{id='x', value=IntNode{value=0}}, assignment=AssignNode{Left=IdentifierNode{value='x'}, Right=BinaryOpNode{Left=IdentifierNode{value='x'}, Op=+, Right=IntNode{value=1}}}, type='for', stmtList=[CallFuncNode{id='a', args=[IntNode{value=2}]}]}",ForLoopNodeCtx.toString());

        //Test for a do while loop
        var parseTreeForDoWhile = setupParseTree("begin loop; x = x+1; end loop(x<10);");
        LoopNode DoWhileLoopNodeCtx = (LoopNode) ASTvisitor.visitBeginStmt((NybCParser.BeginStmtContext) parseTreeForDoWhile.getChild(0).getChild(0));
        Assertions.assertNotNull(DoWhileLoopNodeCtx);
        Assertions.assertEquals("LoopNode{condition=BinaryOpNode{Left=IdentifierNode{value='x'}, Op=<, Right=IntNode{value=10}}, declaration=null, assignment=null, type='do-while', stmtList=[AssignNode{Left=IdentifierNode{value='x'}, Right=BinaryOpNode{Left=IdentifierNode{value='x'}, Op=+, Right=IntNode{value=1}}}]}",DoWhileLoopNodeCtx.toString());

        //Test for a switch
        var parseTreeSwitch = setupParseTree("begin switch(x); case 2: out(x); case 4: out(x); default: out(x); end switch;");
        SwitchNode SwitchNodeCtx = (SwitchNode) ASTvisitor.visitBeginStmt((NybCParser.BeginStmtContext) parseTreeSwitch.getChild(0).getChild(0));
        Assertions.assertNotNull(SwitchNodeCtx);
        Assertions.assertEquals("SwitchNode{switchCond=IdentifierNode{value='x'}, cases=[CaseNode{caseExp=IntNode{value=2}, stmtList=[CallFuncNode{id='out', args=[IdentifierNode{value='x'}]}]}, CaseNode{caseExp=IntNode{value=4}, stmtList=[CallFuncNode{id='out', args=[IdentifierNode{value='x'}]}]}, CaseNode{caseExp=null, stmtList=[CallFuncNode{id='out', args=[IdentifierNode{value='x'}]}]}]}",SwitchNodeCtx.toString());


    }

    //visitDeclareStmt - Markus
    @Test
    void visitDeclareStmtTest(){
        var ASTvisitor = new ToASTVisitor();

        //Test for no value
        var parseTreeForDeclareStmt = setupParseTree("var x;");
        NybCParser.DeclareStmtContext declareStmtContext =(NybCParser.DeclareStmtContext) parseTreeForDeclareStmt.getChild(0).getChild(0);
        DeclNode declNode = (DeclNode) ASTvisitor.visitDeclareStmt(declareStmtContext);

        Assertions.assertNotNull(declNode);
        Assertions.assertEquals("x", declNode.getId());
        Assertions.assertNull(declNode.getValue());

        //Test for expression
        var parseTreeForDeclareStmtWithExpression = setupParseTree("var x = 4;");
        NybCParser.DeclareStmtContext declareStmtContextWithExpression =(NybCParser.DeclareStmtContext) parseTreeForDeclareStmtWithExpression.getChild(0).getChild(0);
        DeclNode<IntNode> declNodeWithExpression = (DeclNode<IntNode>) ASTvisitor.visitDeclareStmt(declareStmtContextWithExpression);

        Assertions.assertNotNull(declNodeWithExpression);
        Assertions.assertEquals("x", declNodeWithExpression.getId());

        IntNode knownIntNode = new IntNode();
        knownIntNode.value = 4;
        Assertions.assertEquals(knownIntNode.value, declNodeWithExpression.getValue().value);

        //Test for array
        var parseTreeForDeclareStmtWithArray = setupParseTree("var x = [4];");
        NybCParser.DeclareStmtContext declareStmtContextWithArray =(NybCParser.DeclareStmtContext) parseTreeForDeclareStmtWithArray.getChild(0).getChild(0);
        DeclNode<ArrayNode> declNodeWithArray = (DeclNode<ArrayNode>) ASTvisitor.visitDeclareStmt(declareStmtContextWithArray);

        Assertions.assertNotNull(declNodeWithArray);
        Assertions.assertEquals("x", declNodeWithArray.getId());

        ArrayNode knownArrayNode = new ArrayNode();
        knownArrayNode.addValue(new IntNode());
        Assertions.assertEquals(knownArrayNode.getValues().size(), declNodeWithArray.getValue().getValues().size());
    }

    //visitAssignStmt - Markus
    @Test
    void visitAssignStmtTest(){
        var ASTvisitor = new ToASTVisitor();

        //Test for expression
        var parseTreeForAssignStmt = setupParseTree("x = 4;");
        NybCParser.AssignStmtContext assignExpressionStmtContext =(NybCParser.AssignStmtContext) parseTreeForAssignStmt.getChild(0).getChild(0);
        AssignNode<IdentifierNode, IntNode> assignExpressionNode = (AssignNode<IdentifierNode, IntNode>) ASTvisitor.visitAssignStmt(assignExpressionStmtContext);

        Assertions.assertNotNull(assignExpressionNode);
        Assertions.assertEquals("x", assignExpressionNode.getLeft().getValue());
        Assertions.assertEquals(4, assignExpressionNode.getRight().getValue());

        //Test for array
        var parseTreeForArrayStmt = setupParseTree("x = [4];");
        NybCParser.AssignStmtContext assignArrayStmtContext =(NybCParser.AssignStmtContext) parseTreeForArrayStmt.getChild(0).getChild(0);
        AssignNode<IdentifierNode, ArrayNode> assignArrayNode = (AssignNode<IdentifierNode, ArrayNode>) ASTvisitor.visitAssignStmt(assignArrayStmtContext);

        Assertions.assertNotNull(assignArrayNode);
        Assertions.assertEquals("x", assignArrayNode.getLeft().getValue());
        Assertions.assertEquals(1, assignArrayNode.getRight().getValues().size());

        //Test for arrayAccess expression assign
        var parseTreeForArrayAccessStmt = setupParseTree("x[0] = 4;");
        NybCParser.AssignStmtContext assignArrayAccessStmtContext =(NybCParser.AssignStmtContext) parseTreeForArrayAccessStmt.getChild(0).getChild(0);
        AssignNode<ArrayAccessNode, IntNode> assignArrayAccessNode = (AssignNode<ArrayAccessNode, IntNode>) ASTvisitor.visitAssignStmt(assignArrayAccessStmtContext);

        Assertions.assertNotNull(assignArrayAccessNode);
        Assertions.assertEquals("x", assignArrayAccessNode.getLeft().getId());
        Assertions.assertEquals(4, assignArrayAccessNode.getRight().getValue());
    }


    @Test
    void visitValueExpressionTest() throws IOException {
        var ASTvisitor = new ToASTVisitor();

        //Test For an Integer
        var parseTreeForInt = setupParseTree("var x = 4;");

        NybCParser.ValueExpressionContext IntExpCtx = (NybCParser.ValueExpressionContext)
                parseTreeForInt.getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
        IntNode IntResult = (IntNode) ASTvisitor.visitValueExpression(IntExpCtx);
        Assertions.assertNotNull(IntResult);
        assertThat(IntResult, instanceOf(IntNode.class));
        Assertions.assertEquals(4, IntResult.value);

        //Test For Identifier
        var parseTreeForIdent = setupParseTree("var x = y;");
        NybCParser.ValueExpressionContext IdentExpCtx = (NybCParser.ValueExpressionContext)
                parseTreeForIdent.getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
        IdentifierNode IdentResult = (IdentifierNode) ASTvisitor.visitValueExpression(IdentExpCtx);

        Assertions.assertNotNull(IdentResult);
        assertThat(IdentResult, instanceOf(IdentifierNode.class));
        Assertions.assertEquals("y", IdentResult.getValue());


        //Test for float
        var parseTreeForFloat = setupParseTree("var x = 4.2;");
        NybCParser.ValueExpressionContext FloatExpCtx = (NybCParser.ValueExpressionContext)
                parseTreeForFloat.getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
        FloatNode FloatResult = (FloatNode) ASTvisitor.visitValueExpression(FloatExpCtx);

        Assertions.assertNotNull(FloatResult);
        assertThat(FloatResult, instanceOf(FloatNode.class));

        //https://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java note: c# er bedre
        Assertions.assertEquals(4.2, (double) Math.round(FloatResult.getValue() * 100) / 100 );


        //Test for String
        var parseTreeForString = setupParseTree("var x = \"Hello\";");
        NybCParser.ValueExpressionContext StringExpCtx = (NybCParser.ValueExpressionContext)
                    parseTreeForString.getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
        StringNode stringResult = (StringNode) ASTvisitor.visitValueExpression(StringExpCtx);

        Assertions.assertNotNull(stringResult);
        assertThat(stringResult, instanceOf(StringNode.class));
        Assertions.assertEquals("Hello", stringResult.getValue());


        //Test for bool to fix with merge
        var parseTreeForBool = setupParseTree("var x = true;");
        NybCParser.ValueExpressionContext BoolExpCtx = (NybCParser.ValueExpressionContext)
                parseTreeForBool.getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getChild(0);
        BoolNode boolResult = (BoolNode) ASTvisitor.visitValueExpression(BoolExpCtx);

        Assertions.assertNotNull(boolResult);
        assertThat(boolResult, instanceOf(BoolNode.class));
        Assertions.assertEquals(true, boolResult.isValue());
    }




}
