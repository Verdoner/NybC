import ASTNode.CallFuncNode;
import ASTNode.DeclNode;
import ASTNode.ExpNode;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * This class is used to store the current state of the program.
 */
public class NybCStack {
    private final HashMap<String, HashMap<String, Object>> fMap = new HashMap<>();
    private final Stack<NybCScope> stack = new Stack<>();

    private Stack<NybCScope> getStack() { return stack; }

    private HashMap<String, HashMap<String, Object>> getFMap() {
        return fMap;
    }

    public void PutFunction(String id, HashMap<String, Object> functionMap) {
        getFMap().put(id, functionMap);
    }

    public HashMap<String, Object> LookupFunc(CallFuncNode node) {
        if (getFMap().containsKey(node.getId())){
            //There is sent a cloned table to avoid writ in the same table
            @SuppressWarnings("unchecked")
            HashMap<String, Object> clonedFunctionMap = (HashMap<String, Object>) getFMap().get(node.getId()).clone();

            return clonedFunctionMap;
        } else {
            Error.FUNCTION_NOT_DECLARED(node);
            return null;
        }
    }

    public boolean IsFunctionDeclared(String id) {
        return getFMap().containsKey(id);
    }

    public void PutVariableToCurrentStack(DeclNode<?> node, Object value) {
        if(IsVariableOnCurrentStack((String) node.getId())) {
            Error.VARIABLE_ALREADY_DECLARED(node);
            return;
        }
        getStack().peek().getVariables().put((String) node.getId(), value);
    }

    public boolean IsVariableOnCurrentStack(String id) {
        return getStack().peek().getVariables().containsKey(id);
    }

    public void ReplaceVariableOnStack(String id, Object newValue) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if(!stack.get(i).getVariables().containsKey(id)) {
                continue;
            }
            stack.get(i).getVariables().replace(id, newValue);
            return;
        }
    }

    public boolean IsVariableOnStack(String id) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if(stack.get(i).getVariables().containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    public Object GetVariableOnStack(String id, ExpNode node) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            Object variable = stack.get(i).getVariables().get(id);
            if (variable == null && !IsVariableOnStack(id)) {
                Error.VARIABLE_NOT_DECLARED(node);
                return null;
            }
            else if(variable != null){
                return variable;
            }
        }
        return null;
    }

    public void PushStack() {
        stack.push(new NybCScope());
    }

    public void PushStack(HashMap<String, Object> hashMap) {
        stack.push(new NybCScope(hashMap));
    }

    public void PopStack() {
        stack.pop();
    }

    public String StackToString(){
        return stack.toString();
    }

    public String FmapToString(){
        return fMap.toString();
    }

    @SuppressWarnings("unchecked")
    public List<Object> getArray (String id, ExpNode node) {
        if(GetVariableOnStack(id, node) instanceof  List<?>) {
            return (List<Object>) GetVariableOnStack(id, node);
        }
        return null;
    }


}


