import ASTNode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Interpreter extends ASTVisitor {
    List<String> keywords;
    public Interpreter(NybCStack nybCStack, List<String> keywords) {
        super(nybCStack);
        this.keywords = keywords;
    }

    /**
     * Visit the AST and interpret the code
     * @param node the root node of the AST
     */
    @Override
    public void Visit(ProgramNode node) {
        Object CtrlFlow;
        for (Object stmt: node.getStmtList()) {
            if (stmt instanceof FuncNode){
                continue;
            }
            CtrlFlow = Visit((StmtNode) stmt);
            if (CtrlFlow == null) {
                continue;
            }
            if (((CtrlFlowNode) CtrlFlow).getType().equals("continue") || ((CtrlFlowNode) CtrlFlow).getType().equals("break")){
                Error.CONTINUE_BREAK_NOT_ALLOWED_IN_GLOBAL(((CtrlFlowNode) CtrlFlow));
            } else if (((CtrlFlowNode) CtrlFlow).getType().equals("return")) {
                break;
            }
        }
    }

    /**
     * Visit the FuncNode and visit all the statements in the statements list of the function
     * @param node func node
     */
    @Override
    public Object Visit(FuncNode node) {
        Object CtrlFlow;
        for (StmtNode stmt: node.getStmsList()) {
            CtrlFlow = Visit(stmt);
            if (CtrlFlow == null) {
                continue;
            }
            if (((CtrlFlowNode) CtrlFlow).getType().equals("continue") || ((CtrlFlowNode) CtrlFlow).getType().equals("break")){
                Error.CONTINUE_BREAK_NOT_ALLOWED_IN_FUNCTION(((CtrlFlowNode) CtrlFlow));
            } else if (((CtrlFlowNode) CtrlFlow).getType().equals("return")) {
                return CtrlFlow;
            }
        }
        return null;
    }

    /**
     * Visit loop node and run the logic corresponding to the type of loop
     * @param node loopnode
     * @return null or a ctrlFlowNode
     */
    @Override
    public Object Visit(LoopNode node) {
        nybCStack.PushStack();
        if (node.getDeclaration() != null) {
            Visit(node.getDeclaration());
        }

        if (!(Visit(node.getCondition()) instanceof Boolean)) {
            Error.INCORRECT_LOOP_CONDITION(node);
        }
        switch (node.getType()) {
            case "while" -> {
                return whileLoopLogic(node);
            }
            case "for" -> {
                return forLoopLogic(node);
            }
            case "do-while" -> {
                return doWhileLoopLogic(node);
            }
        }
        return null;
    }

    /**
     * Logic for while-loop
     */
    private Object whileLoopLogic(LoopNode node) {
        boolean goOutOfLoop = false;
        while ((Boolean) Visit(node.getCondition())) {
            if(goOutOfLoop) {
                break;
            }
            nybCStack.PushStack();
            for (StmtNode stmt : node.getStmtList()) {
                Object CtrlFlow = Visit(stmt);
                if (CtrlFlow == null) {
                    continue;
                }
                if(((CtrlFlowNode) CtrlFlow).getType().equals("return")) {
                    nybCStack.PopStack();
                    nybCStack.PopStack();
                    return CtrlFlow;
                }
                else if(((CtrlFlowNode) CtrlFlow).getType().equals("break")) {
                    goOutOfLoop = true;
                    nybCStack.PopStack();
                    break;
                }
                else if(((CtrlFlowNode) CtrlFlow).getType().equals("continue")) {
                    break;
                }
            }
            nybCStack.PopStack();
        }
        nybCStack.PopStack();
        return null;
    }

    /**
     * Logic for for-loop
     */
    private Object forLoopLogic(LoopNode node) {
        boolean goOutOfLoop = false;
        while ((Boolean) Visit(node.getCondition())) {
            if(goOutOfLoop) {
                break;
            }
            nybCStack.PushStack();
            for (StmtNode stmt : node.getStmtList()) {
                Object CtrlFlow = Visit(stmt);
                if (CtrlFlow == null) {
                    continue;
                }
                if(((CtrlFlowNode) CtrlFlow).getType().equals("return")) {
                    nybCStack.PopStack();
                    nybCStack.PopStack();
                    return CtrlFlow;
                }
                else if(((CtrlFlowNode) CtrlFlow).getType().equals("break")) {
                    goOutOfLoop = true;
                    break;
                }
                else if(((CtrlFlowNode) CtrlFlow).getType().equals("continue")) {
                    break;
                }
            }
            nybCStack.PopStack();
            Visit(node.getAssignment());
        }
        nybCStack.PopStack();
        return null;
    }

    /**
     * Logic for do-while-loop
     */
    private Object doWhileLoopLogic(LoopNode node) {
        boolean goOutOfLoop = false;
        do {
            if(goOutOfLoop) {
                break;
            }
            nybCStack.PushStack();
            for (StmtNode stmt : node.getStmtList()) {
                Object CtrlFlow = Visit(stmt);
                if (CtrlFlow == null) {
                    continue;
                }
                if(((CtrlFlowNode) CtrlFlow).getType().equals("return")) {
                    nybCStack.PopStack();
                    nybCStack.PopStack();
                    return CtrlFlow;
                }
                else if(((CtrlFlowNode) CtrlFlow).getType().equals("break")) {
                    goOutOfLoop = true;
                    nybCStack.PopStack();
                    break;
                }
                else if(((CtrlFlowNode) CtrlFlow).getType().equals("continue")) {
                    break;
                }
            }
            nybCStack.PopStack();
        } while ((Boolean) Visit(node.getCondition()));
        nybCStack.PopStack();
        return null;
    }

    /**
     * Visit the switch node and return null or a ctrlFlowNode
     * @param node switch node
     * @return a ctrlFlowNode or null
     */
    @Override
    public Object Visit(SwitchNode node) {
        nybCStack.PushStack();
        Object CtrlFlow;

        if (Visit(node.getSwitchCond()) instanceof String) {
            for (CaseNode cases: node.getCases()) {
                if (!(Visit(node.getSwitchCond()).equals(Visit(cases.getCaseExp())) || cases.getCaseExp() == null)) {
                    continue;
                }
                CtrlFlow = Visit(cases);
                if (CtrlFlow == null) {
                    continue;
                }
                if (((CtrlFlowNode) CtrlFlow).getType().equals("break")) {
                    nybCStack.PopStack();
                    return null;
                } else if (((CtrlFlowNode) CtrlFlow).getType().equals("return")) {
                    nybCStack.PopStack();
                    return CtrlFlow;
                }
            }
        } else {
            for (CaseNode cases: node.getCases()) {
                if (!(Visit(node.getSwitchCond()) == Visit(cases.getCaseExp()) || cases.getCaseExp() == null)) {
                    continue;
                }
                CtrlFlow = Visit(cases);
                if (CtrlFlow == null) {
                    continue;
                }
                if (((CtrlFlowNode) CtrlFlow).getType().equals("break")) {
                    nybCStack.PopStack();
                    return null;
                } else if (((CtrlFlowNode) CtrlFlow).getType().equals("return")) {
                    nybCStack.PopStack();
                    return CtrlFlow;
                }
            }
        }
        nybCStack.PopStack();
        return null;
    }

    /**
     * Visit the case node
     * @param node return node
     * @return a ctrlFlowNode or null
     */
    @Override
    public Object Visit(CaseNode node) {
        Object CtrlFlow;
        for (StmtNode stmt: node.getStmtList()) {
            CtrlFlow = Visit(stmt);
            if (CtrlFlow != null && (((CtrlFlowNode) CtrlFlow).getType().equals("return") || ((CtrlFlowNode) CtrlFlow).getType().equals("break") || ((CtrlFlowNode) CtrlFlow).getType().equals("continue"))){
                return CtrlFlow;
            }
        }
        return null;
    }

    /**
     * Visit the array access node
     * @param node array access node
     * @return a ctrlFlowNode or null
     */
    @Override
    public Object Visit(ArrayAccessNode<?> node) {
        List<Object> array = nybCStack.getArray(node.getId(), node);
        if (array == null) {
            Error.VARIABLE_NOT_DECLARED(node);
        }

        if (node.getIndex() instanceof Integer) {
            try {
                return array.get((Integer) node.getIndex());
            } catch (IndexOutOfBoundsException e){
                Error.ARRAY_INDEX_OUT_OF_BOUNDS(node);
            }
        } else if (node.getIndex() instanceof String) {
            var index = nybCStack.GetVariableOnStack((String) node.getIndex(), node);
            if (index instanceof Integer){
                try {
                    return array.get((int) index);
                } catch (IndexOutOfBoundsException e){
                    Error.ARRAY_INDEX_OUT_OF_BOUNDS(node);
                }
            } else {
                Error.ARRAY_INDEX_VAR_NOT_INT(node);
            }
        } else {
            Error.ARRAY_INDEX_NOT_VALID(node, node.getIndex().toString());
        }
        return null;
    }

    /**
     * Visit the assign node
     * @param node assign node
     * @return null
     */
    @Override
    public Object Visit(AssignNode<?,?> node) {
        var left = node.getLeft();
        var right = node.getRight();
        if (left instanceof ArrayAccessNode){
            var arrayName = ((ArrayAccessNode<?>) left).getId();
            var arrayIndex = ((ArrayAccessNode<?>) left).getIndex();
            List<Object> array = nybCStack.getArray(arrayName, node);
            if (arrayIndex instanceof Integer) {
                array.set((int) arrayIndex, Visit((ExpNode) right));
            } else if (arrayIndex instanceof String) {
                var index = nybCStack.GetVariableOnStack((String) arrayIndex, node);
                if (index instanceof Integer){
                    array.set((int) index, Visit((ExpNode) right));
                } else {
                    Error.ASSIGNMENT_ARRAY_INDEX_VAR_NOT_INT(node, (String) arrayIndex);
                }
            } else {
                Error.ASSIGNMENT_ARRAY_INDEX_NOT_VALID(node, arrayIndex.toString());
            }
        } else if (left instanceof IdentifierNode) {
            left = ((IdentifierNode) left).getValue();
            nybCStack.GetVariableOnStack((String) left, node);
            if(right instanceof ArrayNode) {
                nybCStack.ReplaceVariableOnStack((String) left, Visit((ArrayNode) node.getRight()));
            }
            else if(right instanceof ExpNode) {
                nybCStack.ReplaceVariableOnStack((String) left, Visit((ExpNode) node.getRight()));
            }
        } else {
            Error.ASSIGNEE_NOT_VALID(node);
        }
        return null;
    }

    /**
     * Visit the decl node
     * @param node decl node
     * @return null
     */
    @Override
    public Object Visit(DeclNode<?> node) {
        for (String keyword : keywords) {
            if (node.getId().equals(keyword)) {
                Error.VARIABLE_NAME_RESERVED(node);
            }
        }
        if (node.getValue() instanceof ArrayNode) {
            nybCStack.PutVariableToCurrentStack(node, Visit((ArrayNode) node.getValue()));
        } else if (node.getValue() instanceof ExpNode) {
            nybCStack.PutVariableToCurrentStack(node, Visit((ExpNode) node.getValue()));
        } else {
            nybCStack.PutVariableToCurrentStack(node, null);
        }
        return null;
    }

    /**
     * Visit the if node
     * @param node if node
     * @return a ctrlFlowNode or null
     */
    @Override
    public Object Visit(IfNode node) {
        if (!(Visit(node.getCondition()) instanceof Boolean) && node.getCondition() != null){
            Error.INCORRECT_IF_CONDITION(node);
        }
        nybCStack.PushStack();
        Object CtrlFlow;
        if (node.getCondition() == null || (Boolean) Visit(node.getCondition())){
            for (StmtNode stmt: node.getStmts()){
                CtrlFlow = Visit(stmt);
                if (CtrlFlow != null && (((CtrlFlowNode) CtrlFlow).getType().equals("return") || ((CtrlFlowNode) CtrlFlow).getType().equals("break") || ((CtrlFlowNode) CtrlFlow).getType().equals("continue"))){
                    nybCStack.PopStack();
                    return CtrlFlow;
                }
            }
        } else {
            IfNode elseifNode = node.getElseIfNode();
            if(elseifNode != null) {
                CtrlFlow = Visit(elseifNode);
                if (CtrlFlow != null && (((CtrlFlowNode) CtrlFlow).getType().equals("return") || ((CtrlFlowNode) CtrlFlow).getType().equals("break") || ((CtrlFlowNode) CtrlFlow).getType().equals("continue"))){
                    nybCStack.PopStack();
                    return CtrlFlow;
                }
            }
        }
        nybCStack.PopStack();
        return null;
    }

    /**
     * Visit the call func node
     * @param node call func node
     * @return a null or a return value of the function
     */
    @Override
    public Object Visit(CallFuncNode node) {
        if (node.getId().equals("out")) {
            if (node.getArgs().size() != 1) {
                Error.OUT_TOO_MANY_ARGS(node);
            }
            if(Visit(node.getArgs().get(0)) == null) {
                Error.TYPE_NOT_VALID_FOR_OUT(node, Visit(node.getArgs().get(0)));
            }
            System.out.println(Visit(node.getArgs().get(0)));

            return null;
        } else if (node.getId().equals("in")) {
            if(!node.getArgs().isEmpty()){
                Error.IN_TOO_MANY_ARGS(node);
            }
            Scanner scan = new Scanner(System.in);
            return scan.nextLine();

        } else {

            HashMap<String, Object> fMap = nybCStack.LookupFunc(node);
            FuncNode funcNode = (FuncNode) fMap.get("0");

            List<Object> paramList = new ArrayList<>();
            for (int i = 0; i < funcNode.getParam().size(); i++) {
                paramList.add(Visit(node.getArgs().get(i)));
            }
            nybCStack.PushStack(fMap);
            if (node.getArgs().size() != funcNode.getParam().size()){
                Error.FUNCTION_CALL_WRONG_AMOUNT_OF_ARGS(funcNode);
            }
            int i = 0;
            for (DeclNode<String> param: funcNode.getParam()) {
                nybCStack.ReplaceVariableOnStack(param.getId(), paramList.get(i));
                i++;
            }
            Object CtrlFlow = Visit(funcNode);
            if (CtrlFlow != null && ((CtrlFlowNode) CtrlFlow).getReturnExp() != null) {
                Object returnValue = nybCStack.GetVariableOnStack("1", node);
                nybCStack.PopStack();
                return returnValue;
            }
            nybCStack.PopStack();
        }
        return null;
    }
}
