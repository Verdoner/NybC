import java.util.HashMap;

/**
 * NybCFrame is a class that represents a hashmap on the stack
 */
public class NybCScope {
    private final HashMap<String, Object> variables;
    public HashMap<String, Object> getVariables() {
        return variables;
    }

    public NybCScope(HashMap<String, Object> hashMap) {
        variables = hashMap;
    }

    public NybCScope() {
        variables = new HashMap<String, Object>();
    }

    @Override
    public String toString() {
        return variables.toString();
    }
}
