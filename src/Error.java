import ASTNode.*;
import AntlrGenFiles.NybCParser;

/*
* The class Error is used to throw exceptions when an error occurs in the code.
* */
public class Error{
    public static void SYNTAX_ERROR(NullPointerException e){
        throw new RuntimeException("Syntax error: "+e);
    }
    public static void VARIABLE_ALREADY_DECLARED(DeclNode<?> node){
        throw new RuntimeException("Variable '"+node.getId()+"' has already been declared. Used in line "+node.getStartLine());
    }
    public static void VARIABLE_NOT_DECLARED(ExpNode node) {
        throw new RuntimeException("Variable '" + node + "' has not been declared. Used in line " + node.getStartLine());
    }
    public static void VARIABLE_NAME_RESERVED(DeclNode<?> node){
        throw new RuntimeException("Variable name '"+node.getId()+"' is reserved. Used in line "+node.getStartLine());
    }
    public static void FUNCTION_ALREADY_DECLARED(FuncNode node){
        throw new CustomRuntimeException("Function '"+node.getId()+"' has already been declared. Used in line "+node.getStartLine());
    }
    public static void FUNCTION_NOT_DECLARED(CallFuncNode node){
        throw new CustomRuntimeException("Function '"+node.getId()+"' has not been declared. Used in line "+node.getStartLine());
    }
    public static void FUNCTION_NAME_RESERVED(FuncNode node){
        throw new CustomRuntimeException("Function name '"+node.getId()+"' is reserved. Used in line "+node.getStartLine());
    }
    public static void INCORRECT_END_FUNCTION(FuncNode node, String endId){
        throw new RuntimeException("Incorrect end function. Expected '"+node.getId()+"' but got '"+endId+"'");
    }
    public static void FUNC_PARAM_EQ_FUNC_NAME(FuncNode node){
        throw new RuntimeException("Parameter for function can not be the same as name for function. Used in line "+node.getStartLine());
    }
    public static void GLOBAL_MUST_CONTAIN_STMT(NybCParser.StmtContext ctx){
        throw new RuntimeException("Global must contain statement. Given '"+ctx.getClass().getSimpleName()+"' in line "+ctx.getStart().getLine());
    }
    public static void INCORRECT_TYPE_FOR_VALUE_EXP(NybCParser.ValueExpressionContext ctx){
        throw new RuntimeException("Incorrect type for value expression. Used in line: "+ctx.getStart().getLine());
    }
    public static void DECLARE_ASSIGNMENT_WRONG_TYPE(NybCParser.DeclareStmtContext ctx) {
        throw new RuntimeException("Declare assignment wrong type, can not be '"+ctx.getChild(3).getClass().getSimpleName()+"'. Used in line "+ctx.getStart().getLine());
    }

    /*CTRL flow errors*/
    public static void CONTINUE_BREAK_NOT_ALLOWED_IN_GLOBAL(CtrlFlowNode node){
        throw new RuntimeException("Control statement not allowed in global: '"+node.getType()+"'. Used in line "+node.getStartLine());
    }
    public static void CONTINUE_BREAK_NOT_ALLOWED_IN_FUNCTION(CtrlFlowNode node){
        throw new RuntimeException("Control statement not allowed in function: '"+node.getType()+"'. Used in line "+node.getStartLine());
    }

    /*Loop errors*/
    public static void INCORRECT_LOOP_CONDITION(LoopNode node){
        throw new RuntimeException("Condition for loops must be a boolean value: '"+node.getCondition().toString()+"'. Used in line "+node.getStartLine());
    }

    /*Array errors*/
    public static void ARRAY_INDEX_VAR_NOT_INT(ArrayAccessNode<?> node){
        throw new RuntimeException("Variable used for array index must be an integer: '"+node.getIndex()+"'. Used in line "+node.getStartLine());
    }
    public static void ARRAY_INDEX_NOT_VALID(ArrayAccessNode<?> node, String index){
        throw new RuntimeException("Array index error '"+index+"'. Used in line "+node.getStartLine());
    }
    public static void ARRAY_INDEX_OUT_OF_BOUNDS(ArrayAccessNode<?> node){
        throw new RuntimeException("Array index out of bounds: '"+node.getIndex().toString()+"'. Used in line "+node.getStartLine());
    }

    /*Assignment value errors*/
    public static void ASSIGNMENT_VALUE_NOT_VALID(AssignNode<?, ?> node){
        throw new RuntimeException("Assignment value not valid: '"+node.getRight().toString()+"'. Used in line "+node.getStartLine());
    }
    public static void ASSIGNEE_NOT_VALID(AssignNode<?, ?> node){
        throw new RuntimeException("Assignment value not valid: '"+node.getLeft().toString()+"'. Used in line "+node.getStartLine());
    }
    public static void ASSIGNMENT_ARRAY_INDEX_VAR_NOT_INT(AssignNode<?, ?> node, String index){
        throw new RuntimeException("Variable used for array index must be an integer: '"+index+"'. Used in line "+node.getStartLine());
    }
    public static void ASSIGNMENT_ARRAY_INDEX_NOT_VALID(AssignNode<?, ?> node, String index){
        throw new RuntimeException("Array index error '"+index+"'. Used in line "+node.getStartLine());
    }

    /*If errors*/
    public static void INCORRECT_IF_CONDITION(IfNode node){
        throw new RuntimeException("Condition for if statement must be a boolean value: '"+node.getCondition().toString()+"'. Used in line "+node.getStartLine());
    }

    /*Out errors*/
    public static void OUT_TOO_MANY_ARGS(CallFuncNode node){
        throw new RuntimeException("Out statement can only take one argument. Used in line "+node.getStartLine());
    }

    /*In errors*/
    public static void IN_TOO_MANY_ARGS(CallFuncNode node){
        throw new RuntimeException("In statement can not take arguments. Used in line "+node.getStartLine());
    }

    /*Function call errors*/
    public static void FUNCTION_CALL_WRONG_AMOUNT_OF_ARGS(FuncNode node){
        throw new RuntimeException("Function call has wrong amount of arguments: '" + node.getId()+"'. Used in line "+node.getStartLine());
    }

    /*Unary op errors*/
    public static void TYPE_NOT_VALID_FOR_UNARY_PLUS(UnaryOpNode node){
        throw new RuntimeException("Type not valid for unary plus operation: " + node.getRight().toString() + " (must be integer or float). Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_UNARY_MINUS(UnaryOpNode node){
        throw new RuntimeException("Type not valid for unary minus operation: " + node.getRight().toString() + " (must be integer or float). Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_UNARY_NOT(UnaryOpNode node){
        throw new RuntimeException("Type not valid for unary not operation: " + node.getRight().toString() +" (must be boolean). Used in line "+node.getStartLine());
    }

    /*Binary op errors*/
    public static void TYPE_NOT_VALID_FOR_BINARY_PLUS(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary plus operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_MINUS(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary minus operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_MULT(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary multiplication operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_DIV(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary division operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_AND(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary and operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight() + " (must be booleans). Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_OR(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary or operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight() + " (must be booleans). Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_EQ(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary equals operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_NEQ(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary not equals operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight() + " (must be integer, float or boolean). Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_GT(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary greater than operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_GTE(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary greater than or equals operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_LT(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary less than operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_BINARY_LTE(BinaryOpNode node){
        throw new RuntimeException("Type not valid for binary less than or equals operation: " + node.getLeft() + " " + node.getOp() + " " + node.getRight()+". Used in line "+node.getStartLine());
    }
    public static void TYPE_NOT_VALID_FOR_OUT(CallFuncNode node, Object value){
        throw new RuntimeException("Type not valid for out: "+ value +". Used in line "+node.getStartLine());
    }
}
