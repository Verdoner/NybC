import ASTNode.*;

import java.util.HashMap;
import java.util.List;

public class InitialVisitor extends ASTVisitor {
    /*List containing all the reserved words*/
    List<String> keywords;

    /*Constructor for the InitialVisitor class*/
    public InitialVisitor(NybCStack nybCStack, List<String> keywords) {
        super(nybCStack);
        this.keywords = keywords;
    }

    /*Visit method for the FuncNode class*/
    @Override
    public Object Visit(FuncNode node) {
        return node;
    }

    /*Visit method for the ProgramNode class. Takes all function declarations
    and inserts them into the function map*/
    @Override
    public void Visit(ProgramNode node) {
        nybCStack.PushStack();

        for (Object stmt: node.getStmtList()) {
            if (stmt instanceof FuncNode) {
                for (String keyword : keywords) {
                    if (((FuncNode) stmt).getId().equals(keyword)) {
                        Error.FUNCTION_NAME_RESERVED(((FuncNode) stmt));
                    }
                }

                if(nybCStack.IsFunctionDeclared(((FuncNode) stmt).getId())) {
                    Error.FUNCTION_ALREADY_DECLARED(((FuncNode) stmt));
                }

                FuncNode function = (FuncNode)Visit((FuncNode) stmt);
                HashMap<String, Object> functionMap = new HashMap<>();

                for (DeclNode<String> param: (function.getParam())){
                    functionMap.put(param.getId(), param.getValue());
                }
                functionMap.put("0", stmt);
                functionMap.put("1", null);

                nybCStack.PutFunction(function.getId(), functionMap);
            }
        }
    }
}