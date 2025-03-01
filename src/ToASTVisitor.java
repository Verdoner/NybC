import ASTNode.*;
import AntlrGenFiles.NybCBaseVisitor;
import AntlrGenFiles.NybCParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ToASTVisitor extends NybCBaseVisitor<ASTNode> {
    @Override
    public ASTNode visit(ParseTree tree) {
        if (tree == null) {
            return null;
        }
        return tree.accept(this);
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#program}.
     * @param ctx the parse tree
     * @return ProgramNode
     */
    @Override
    public ProgramNode visitProgram(NybCParser.ProgramContext ctx) {
        ProgramNode node = new ProgramNode();
        for (ParseTree childNode: ctx.children) {
            if (!childNode.getClass().getSimpleName().equals("StmtContext") && !childNode.getClass().getSimpleName().equals("FunctionStmtContext")) {
                Error.GLOBAL_MUST_CONTAIN_STMT((NybCParser.StmtContext) childNode);
            }
            node.addStmt(visit(childNode));
        }
        return node;
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#stmt}.
     * @param ctx the parse tree
     * @return StmtNode
     */
    @Override
    public StmtNode visitStmt(NybCParser.StmtContext ctx) {
        return (StmtNode) visit(ctx.getChild(0));
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#functionStmt}.
     * @param ctx the parse tree
     * @return FuncNode
     */
    @Override
    public FuncNode visitFunctionStmt(NybCParser.FunctionStmtContext ctx) {
        FuncNode node = new FuncNode();
        node.setStartLine(ctx.getStart().getLine());
        node.setEndLine(ctx.getStop().getLine());

        node.setId(ctx.getChild(2).getText());
        if (!ctx.getChild(ctx.children.size()-2).getText().equals(node.getId()) && !ctx.getChild(ctx.children.size()-2).getText().equals("function"))  {
            Error.INCORRECT_END_FUNCTION(node, ctx.getChild(ctx.children.size()-2).getText());
        }
        if (ctx.getChild(ctx.children.size()-2).getText().equals("function")){
            for (int i = 1; i < ctx.IDENT().size(); i++) {
                if (node.getId().equals(ctx.IDENT(i).getText())){
                    Error.FUNC_PARAM_EQ_FUNC_NAME(node);
                }
                node.addParam(new DeclNode<>(ctx.IDENT(i).getText()));
            }
        } else {
            for (int i = 1; i < ctx.IDENT().size()-1; i++) {
                if (node.getId().equals(ctx.IDENT(i).getText())){
                    Error.FUNC_PARAM_EQ_FUNC_NAME(node);
                }
                node.addParam(new DeclNode<>(ctx.IDENT(i).getText()));
            }
        }
        for (ParseTree childNode: ctx.stmt()) {
            node.addStmt((StmtNode) visit(childNode));
        }
        return node;
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#beginStmt}.
     * @param ctx the parse tree
     * @return IfNode | LoopNode | SwitchNode | null (if none of the above
     */
    @Override
    public ASTNode visitBeginStmt(NybCParser.BeginStmtContext ctx) {
        return switch (ctx.getChild(1).getText()){
            case "if" -> {
                IfNode node = new IfNode();
                node.setStartLine(ctx.getStart().getLine());
                node.setEndLine(ctx.getStop().getLine());
                node.setCondition((ExpNode) visit(ctx.getChild(3)));
                node.setIfNode((IfNode) visit(ctx.extendedIf()));
                for (ParseTree stmtNode: ctx.stmt()){
                    node.addStmt((StmtNode) visit(stmtNode));
                }
                yield node;
            }
            case "loop" -> {
                LoopNode node = new LoopNode();
                node.setStartLine(ctx.getStart().getLine());
                node.setEndLine(ctx.getStop().getLine());
                    ParseTree ctxChild = ctx.getChild(3);
                if (ctxChild instanceof NybCParser.ExpressionContext expressionContext) {
                    node.setType("while");
                    node.setCondition((ExpNode) visit(expressionContext));
                } else if (ctxChild instanceof NybCParser.StmtContext) {
                    node.setType("do-while");
                    node.setCondition((ExpNode) visit(ctx.getChild(ctx.children.size() - 2)));
                } else if (ctxChild instanceof NybCParser.DeclareStmtContext declareStmtContext) {
                    node.setType("for");
                    node.setDeclaration((DeclNode<Integer>) visit(declareStmtContext));
                    node.setCondition((ExpNode) visit(ctx.getChild(5)));
                    node.setAssignment((AssignNode<String, Integer>) visit(ctx.getChild(7)));
                }
                for (ParseTree stmt : ctx.stmt()) {
                    node.addStmt((StmtNode) visit(stmt));
                }
                yield node;
            }
            case "switch" -> {
                SwitchNode node = new SwitchNode();
                node.setStartLine(ctx.getStart().getLine());
                node.setEndLine(ctx.getStop().getLine());
                node.setSwitchCond((ExpNode) visit(ctx.expression()));
                for (ParseTree caseNode : ctx.switchCase()) {
                    node.addCase((CaseNode) visit(caseNode));
                }
                yield node;
            }
            default -> null;
        };
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#switchCase}.
     * @param ctx the parse tree
     * @return CaseNode
     */
    @Override
    public CaseNode visitSwitchCase(NybCParser.SwitchCaseContext ctx) {
        CaseNode node = new CaseNode();
        node.setStartLine(ctx.getStart().getLine());
        node.setEndLine(ctx.getStop().getLine());
        if (ctx.getChild(0).getText().equals("case")) {
            node.setCaseExp((ExpNode) visit(ctx.getChild(1)));
        }
        for (ParseTree stmtNode : ctx.stmt()) {
            node.addStmt((StmtNode) visit(stmtNode));
        }
        return node;
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#extendedIf}.
     * @param ctx the parse tree
     * @return IfNode
     */
    @Override
    public IfNode visitExtendedIf(NybCParser.ExtendedIfContext ctx) {
        IfNode node = new IfNode();
        node.setStartLine(ctx.getStart().getLine());
        node.setEndLine(ctx.getStop().getLine());
        for (ParseTree stmtNode : ctx.stmt()) {
            node.addStmt((StmtNode) visit(stmtNode));
        }
        node.setCondition((ExpNode) visit(ctx.expression()));
        node.setIfNode((IfNode) visit(ctx.extendedIf()));
        return node;
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#declareStmt}.
     * @param ctx the parse tree
     * @return DeclNode
     */
    @Override
    public DeclNode<?> visitDeclareStmt(NybCParser.DeclareStmtContext ctx) {
        if (ctx.getChild(3) != null) {
            if (ctx.getChild(3) instanceof  NybCParser.ExpressionContext) {
                return new DeclNode<>(ctx.IDENT().getText(), (ExpNode) visit(ctx.expression()));
            } else if (ctx.getChild(3) instanceof NybCParser.ArrayContext) {
                return new DeclNode<>(ctx.IDENT().getText(), (ArrayNode) visit(ctx.array()));
            } else {
                Error.DECLARE_ASSIGNMENT_WRONG_TYPE((NybCParser.DeclareStmtContext) ctx.getChild(3));
            }
        } else return new DeclNode<>(ctx.IDENT().getText());
        return null;
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#assignStmt}.
     * @param ctx the parse tree
     * @return AssignNode
     */
    @Override
    public AssignNode<?, ?> visitAssignStmt(NybCParser.AssignStmtContext ctx) {
        if(ctx.getChild(0).getClass().getSimpleName().equals("ArrayAccessContext")){
            return new AssignNode<>(((ArrayAccessNode<?>)visit(ctx.getChild(0))), ((ExpNode)visit(ctx.getChild(2))), ctx.getStart().getLine(), ctx.getStop().getLine());
        }
        else{
            if(ctx.getChild(2).getClass().getSimpleName().equals("ArrayContext")) {
                return new AssignNode<>(new IdentifierNode(ctx.IDENT().getText()), (ArrayNode) visit(ctx.getChild(2)), ctx.getStart().getLine(), ctx.getStop().getLine());
            }
            else{
                return new AssignNode<>(new IdentifierNode(ctx.IDENT().getText()), (ExpNode) visit(ctx.getChild(2)), ctx.getStart().getLine(), ctx.getStop().getLine());
            }
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#array}.
     * @param ctx the parse tree
     * @return ArrayNode
     */
    @Override
    public ArrayNode visitArray(NybCParser.ArrayContext ctx) {
        ArrayNode arrayNode = new ArrayNode();
        arrayNode.setStartLine(ctx.getStart().getLine());
        arrayNode.setEndLine(ctx.getStop().getLine());
        for (ParseTree expNode : ctx.expression()) {
            arrayNode.addValue((ExpNode) visit(expNode));
        }
        return arrayNode;
    }

    /**
     * Visit a terminal node.
     * @param node the parse tree
     * @return
     */
    @Override
    public ASTNode visitTerminal(TerminalNode node) {
        return super.visitTerminal(node);
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#ctrlFlowStmt}.
     * @param ctx the parse tree
     * @return CtrlFlowNode
     */
    @Override
    public CtrlFlowNode visitCtrlFlowStmt(NybCParser.CtrlFlowStmtContext ctx) {
        CtrlFlowNode node = new CtrlFlowNode();
        node.setStartLine(ctx.getStart().getLine());
        node.setEndLine(ctx.getStop().getLine());
        node.setReturnExp((ExpNode) visit(ctx.expression()));
        switch (ctx.getChild(0).getText()) {
            case "return" -> node.setType("return");
            case "continue" -> node.setType("continue");
            case "break" -> node.setType("break");
        }
        return node;
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#expression()}.
     * @param ctx the parse tree
     * @return BinaryOpNode | ExpNode
     */
    @Override
    public ASTNode visitExpression(NybCParser.ExpressionContext ctx) {
        if (ctx.children.size() == 3) {
            return new BinaryOpNode(
                    (ExpNode) visit(ctx.getChild(0)),
                    ctx.getChild(1).getText(),
                    (ExpNode) visit(ctx.getChild(2)),
                    ctx.getStart().getLine(),
                    ctx.getStop().getLine()
            );
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#orExp()}.
     * @param ctx the parse tree
     * @return BinaryOpNode | ExpNode
     */
    @Override
    public ASTNode visitOrExp(NybCParser.OrExpContext ctx) {
        if (ctx.children.size() == 3) {
            return new BinaryOpNode(
                    (ExpNode) visit(ctx.getChild(0)),
                    ctx.getChild(1).getText(),
                    (ExpNode) visit(ctx.getChild(2)),
                    ctx.getStart().getLine(),
                    ctx.getStop().getLine()
            );
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#andExp()}.
     * @param ctx the parse tree
     * @return BinaryOpNode | ExpNode
     */
    @Override
    public ASTNode visitAndExp(NybCParser.AndExpContext ctx) {
        if (ctx.children.size() == 3) {
            return new BinaryOpNode(
                    (ExpNode) visit(ctx.getChild(0)),
                    ctx.getChild(1).getText(),
                    (ExpNode) visit(ctx.getChild(2)),
                    ctx.getStart().getLine(),
                    ctx.getStop().getLine()
            );
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#eqExp()}.
     * @param ctx the parse tree
     * @return BinaryOpNode | ExpNode
     */
    @Override
    public ASTNode visitEqExp(NybCParser.EqExpContext ctx) {
        if (ctx.children.size() == 3) {
            return new BinaryOpNode(
                    (ExpNode) visit(ctx.getChild(0)),
                    ctx.getChild(1).getText(),
                    (ExpNode) visit(ctx.getChild(2)),
                    ctx.getStart().getLine(),
                    ctx.getStop().getLine()
            );
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#relationalExp()}.
     * @param ctx the parse tree
     * @return BinaryOpNode | ExpNode
     */
    @Override
    public ASTNode visitRelationalExp(NybCParser.RelationalExpContext ctx) {
        if (ctx.children.size() == 3) {
            return new BinaryOpNode(
                    (ExpNode) visit(ctx.getChild(0)),
                    ctx.getChild(1).getText(),
                    (ExpNode) visit(ctx.getChild(2)),
                    ctx.getStart().getLine(),
                    ctx.getStop().getLine()
            );
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#additionExp()}.
     * @param ctx the parse tree
     * @return BinaryOpNode | ExpNode
     */
    @Override
    public ASTNode visitAdditionExp(NybCParser.AdditionExpContext ctx) {
        if (ctx.children.size() == 3) {
            return new BinaryOpNode(
                    (ExpNode) visit(ctx.getChild(0)),
                    ctx.getChild(1).getText(),
                    (ExpNode) visit(ctx.getChild(2)),
                    ctx.getStart().getLine(),
                    ctx.getStop().getLine()
            );
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#unaryExp()}.
     * @param ctx the parse tree
     * @return UnaryOpNode | ExpNode
     */
    @Override
    public ASTNode visitUnaryExp(NybCParser.UnaryExpContext ctx) {
        if (ctx.children.size() == 2) {
            UnaryOpNode node = new UnaryOpNode();
            node.setStartLine(ctx.getStart().getLine());
            node.setEndLine(ctx.getStop().getLine());
            node.setRight((ExpNode) visit(ctx.getChild(1)));
            node.setOp(ctx.getChild(0).getText());
            return node;
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#parenthExp()}.
     * @param ctx the parse tree
     * @return ParenthNode | ExpNode
     */
    @Override
    public ASTNode visitParenthExp(NybCParser.ParenthExpContext ctx) {
        if (ctx.children.size() ==  3) {
            ParenthNode node = new ParenthNode();
            node.setStartLine(ctx.getStart().getLine());
            node.setEndLine(ctx.getStop().getLine());
            node.setInner((ExpNode) visit(ctx.getChild(1)));
            return node;
        } else {
            return visit(ctx.getChild(0));
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#arrayAccess}.
     * @param ctx the parse tree
     * @return ArrayAccessNode
     */
    @Override
    public ArrayAccessNode<?> visitArrayAccess(NybCParser.ArrayAccessContext ctx) {
        String id = ctx.getChild(0).getText();
        if(ctx.INT() != null){
            return new ArrayAccessNode<>(id, Integer.parseInt(ctx.INT().getText()), ctx.getStart().getLine(), ctx.getStop().getLine());
        } else {
            return new ArrayAccessNode<>(id, ctx.getChild(2).getText(), ctx.getStart().getLine(), ctx.getStop().getLine());
        }
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#callStmt}.
     * @param ctx the parse tree
     * @return CallFuncNode
     */
    @Override
    public CallFuncNode visitCallStmt(NybCParser.CallStmtContext ctx) {
        CallFuncNode node = new CallFuncNode();
        node.setStartLine(ctx.getStart().getLine());
        node.setEndLine(ctx.getStop().getLine());
        node.setId(ctx.IDENT().getText());
        for (ParseTree childNode: ctx.children) {
            if (childNode instanceof NybCParser.ExpressionContext){
                node.addArgs((ExpNode) visit(childNode));
            }
        }
        return node;
    }

    /**
     * Visit a parse tree produced by {@link NybCParser#valueExpression}.
     * @param ctx the parse tree
     * @return IntNode | IdentifierNode | FloatNode | StringNode | BoolNode
     */
    @Override
    public ASTNode visitValueExpression(NybCParser.ValueExpressionContext ctx) {
        if (ctx.INT() != null) {
            IntNode node = new IntNode();
            node.setStartLine(ctx.getStart().getLine());
            node.setEndLine(ctx.getStop().getLine());
            node.setValue(Integer.parseInt(ctx.INT().getText()));
            return node;
        } else if (ctx.IDENT() != null) {
            IdentifierNode node = new IdentifierNode();
            node.setStartLine(ctx.getStart().getLine());
            node.setEndLine(ctx.getStop().getLine());
            node.setValue(ctx.IDENT().getText());
            return node;
        } else if (ctx.FLOAT() != null) {
            FloatNode node = new FloatNode();
            node.setStartLine(ctx.getStart().getLine());
            node.setEndLine(ctx.getStop().getLine());
            node.setValue(Float.parseFloat(ctx.FLOAT().getText()));
            return node;
        } else if (ctx.STRING() != null) {
            StringNode node = new StringNode();
            node.setStartLine(ctx.getStart().getLine());
            node.setEndLine(ctx.getStop().getLine());
            String value = ctx.STRING().getText();
            value = value.substring(1,value.length()-1);
            node.setValue(value);
            return node;
        } else if (ctx.BOOL() != null) {
            BoolNode node = new BoolNode();
            node.setStartLine(ctx.getStart().getLine());
            node.setEndLine(ctx.getStop().getLine());
            node.setValue(Boolean.parseBoolean(ctx.BOOL().getText()));
            return node;
        } else {
            Error.INCORRECT_TYPE_FOR_VALUE_EXP(ctx);
            return null;
        }
    }
}
