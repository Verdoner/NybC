import ASTNode.*;

import java.util.ArrayList;
import java.util.List;

public class ASTVisitor{
    /**
     * The NybCStack instance used by the ASTVisitor.
     * This stack is used to store and retrieve variables during the traversal of the AST.
     */
    protected NybCStack nybCStack;

    /**
     * Constructor for the ASTVisitor.
     * @param nybCStack The NybCStack instance used by the ASTVisitor.
     */
    public ASTVisitor(NybCStack nybCStack) {
        this.nybCStack = nybCStack;
    }

    /**
     * Visit method for the IdentifierNode.
     * This method is used to retrieve the value of a variable from the NybCStack.
     * @param node The IdentifierNode to visit.
     * @return The value of the variable.
     */
    public Object Visit(IdentifierNode node) {
        return nybCStack.GetVariableOnStack(node.getValue(), node);
    }

    /**
     * Visit method for the BoolNode.
     * This method is used to retrieve the value of a boolean node.
     * @param node The BoolNode to visit.
     * @return The value of the boolean.
     */
    public Boolean Visit(BoolNode node){
        return node.isValue();
    }

    /**
     * Visit method for the FloatNode.
     * This method is used to retrieve the value of a float node.
     * @param node The FloatNode to visit.
     * @return The value of the float.
     */
    public Float Visit(FloatNode node){
        return node.getValue();
    }

    /**
     * Visit method for the IntNode.
     * This method is used to retrieve the value of an integer node.
     * @param node The IntNode to visit.
     * @return The value of the integer.
     */
    public Integer Visit(IntNode node){
        return node.getValue();
    }

    /**
     * Visit method for the StringNode.
     * This method is used to retrieve the value of a string node.
     * @param node The StringNode to visit.
     * @return The value of the string.
     */
    public String Visit(StringNode node){
        return node.getValue();
    }

    /**
     * Visit method for the ArrayNode.
     * This method is used to retrieve the values of an array node.
     * @param node The ArrayNode to visit.
     * @return The values of the array as a list.
     */
    public List<Object> Visit(ArrayNode node) {
        List<Object> list = new ArrayList<>();
        for (ExpNode exp: node.getValues()) {
            list.add(Visit(exp));
        }
        return list;
    }

    /**
     * Visit method for the CtrlFlowNode.
     * This method is used to handle the return, continue and break statements in the AST.
     * @param node The CtrlFlowNode to visit.
     * @return The CtrlFlowNode.
     */
    public CtrlFlowNode Visit(CtrlFlowNode node) {
        if (node.getType().equals("return") && node.getReturnExp() != null){
            var value = Visit(node.getReturnExp());
            if (nybCStack.IsVariableOnStack("0")) {
                nybCStack.ReplaceVariableOnStack("1", value);
            }
        }
        return node;
    }

    /**
     * Visit method for the BinaryOpNode.
     * This method is used to handle binary operations as well as conversion of types in the AST.
     * @param node The BinaryOpNode to visit.
     * @return The result of the binary operation.
     */
    public Object Visit(BinaryOpNode node){
        var left = Visit(node.getLeft());
        var right = Visit(node.getRight());
        switch (node.getOp()) {
            case "+" -> {
                if (left instanceof String) {
                    return (String) left + right;
                } else if (right instanceof String) {
                    return left + (String) right;
                } else if (left instanceof Integer && right instanceof Integer) {
                    return (int) left + (int) right;
                } else if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer)) {
                    return ((Number) left).floatValue() + ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_PLUS(node);
            }
            case "-" -> {
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left - (int) right;
                } else if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer)) {
                    return ((Number) left).floatValue() - ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_MINUS(node);
            }
            case "*" -> {
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left * (int) right;
                } else if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer)) {
                    return ((Number) left).floatValue() * ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_MULT(node);
            }
            case "/" -> {
                if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer) && ((Number) right).floatValue() != 0.0) {
                    return ((Number) left).floatValue() / ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_DIV(node);
            }
            case "<" -> {
                if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer)) {
                    return ((Number) left).floatValue() < ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_LT(node);
            }
            case ">" -> {
                if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer)) {
                    return ((Number) left).floatValue() > ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_GT(node);
            }
            case "<=" -> {
                if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer)) {
                    return ((Number) left).floatValue() <= ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_LTE(node);
            }
            case ">=" -> {
                if ((left instanceof Float || left instanceof Integer) && (right instanceof Float || right instanceof Integer)) {
                    return ((Number) left).floatValue() >= ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_GTE(node);
            }
            case "!=" -> {
                if (left instanceof String && right instanceof String) {
                    return !left.equals(right);
                } else if (left instanceof Float && right instanceof Float) {
                    return (float) left != (float) right;
                } else if (left instanceof Boolean && right instanceof Boolean) {
                    return left != right;
                } else if (left instanceof Integer && right instanceof Integer) {
                    return (int) left != (int) right;
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_NEQ(node);
            }
            case "==" -> {
                if (left instanceof String && right instanceof String) {
                    return left.equals(right);
                } else if (left instanceof Float && right instanceof Float) {
                    return (float) left == (float) right;
                } else if (left instanceof Boolean && right instanceof Boolean) {
                    return left == right;
                } else if (left instanceof Integer && right instanceof Integer) {
                    return (int) left == (int) right;
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_EQ(node);
            }
            case "||" -> {
                if (left instanceof Boolean && right instanceof Boolean) {
                    return (Boolean) left || (Boolean) right;
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_OR(node);
            }
            case "&&" -> {
                if (left instanceof Boolean && right instanceof Boolean) {
                    return (Boolean) left && (Boolean) right;
                }
                Error.TYPE_NOT_VALID_FOR_BINARY_AND(node);
            }
        }
        return null;
    }

    /**
     * Visit method for the UnaryOpNode.
     * This method is used to handle unary operations in the AST.
     * @param node The UnaryOpNode to visit.
     * @return The result of the unary operation.
     */
    public Object Visit(UnaryOpNode node) {
        var right = Visit(node.getRight());
        switch (node.getOp()) {
            case "+" -> {
                if (right instanceof Integer || right instanceof Float) {
                    return ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_UNARY_PLUS(node);
            }
            case "-" -> {
                if (right instanceof Integer || right instanceof Float) {
                    return -1 * ((Number) right).floatValue();
                }
                Error.TYPE_NOT_VALID_FOR_UNARY_MINUS(node);
            }
            case "!" -> {
                if (right instanceof Boolean) {
                    return !(Boolean) right;
                }
                Error.TYPE_NOT_VALID_FOR_UNARY_NOT(node);
            }
        }
        return null;
    }

    /**
     * Visit method for the ParenthNode.
     * This method is used to handle parentheses in the AST.
     * @param node The ParenthNode to visit.
     * @return The result of the inner expression.
     */
    public Object Visit(ParenthNode node) {
        return Visit(node.getInner());
    }
    public  Object Visit(ArrayAccessNode<?> node){return null;}
    public  Object Visit(CallFuncNode node){return null;}
    public  Object Visit(IfNode node){return null;}
    public  Object Visit(DeclNode<?> node){return null;}
    public  Object Visit(AssignNode<?,?> node){return null;}
    public  Object Visit(CaseNode node){return null;}
    public  Object Visit(SwitchNode node){return null;}
    public  Object Visit(LoopNode node){return null;}
    public  Object Visit(FuncNode node){return null;}
    public  void Visit(ProgramNode node){}

    /**
     * Visit method for the ExpNode.
     * This method is used to visit the different types of expressions in the AST.
     * @param node The ExpNode to visit.
     * @return The result of the expression.
     */
    public Object Visit(ExpNode node)
    {
        if (node instanceof BoolNode) {
            return Visit((BoolNode) node);
        } else if (node instanceof IdentifierNode) {
            return Visit((IdentifierNode) node);
        }else if (node instanceof IntNode) {
            return Visit((IntNode) node);
        }else if (node instanceof FloatNode) {
            return Visit((FloatNode) node);
        }else if (node instanceof StringNode) {
            return Visit((StringNode) node);
        }else if (node instanceof ParenthNode) {
            return Visit((ParenthNode) node);
        }else if (node instanceof UnaryOpNode) {
            return Visit((UnaryOpNode) node);
        }else if (node instanceof BinaryOpNode) {
            return Visit((BinaryOpNode) node);
        }else if (node instanceof ArrayAccessNode) {
            return Visit((ArrayAccessNode<?>) node);
        }else if (node instanceof CallFuncNode) {
            return Visit((CallFuncNode) node);
        }
        return null;
    }

    /**
     * Visit method for the StmtNode.
     * This method is used to visit the different types of statements in the AST.
     * @param node The StmtNode to visit.
     * @return The result of the statement.
     */
    public Object Visit (StmtNode node)
    {
        if (node instanceof IfNode) {
            return Visit((IfNode) node);
        } else if (node instanceof LoopNode) {
            return Visit((LoopNode) node);
        }else if (node instanceof SwitchNode) {
            return Visit((SwitchNode) node);
        }else if (node instanceof DeclNode) {
            return Visit((DeclNode<?>) node);
        }else if (node instanceof AssignNode) {
            return Visit((AssignNode<?,?>) node);
        }else if (node instanceof CallFuncNode) {
            return Visit((CallFuncNode) node);
        }else if (node instanceof CtrlFlowNode) {
            return Visit((CtrlFlowNode) node);
        }
        return null;
    }

}
