package ASTNode;

import java.util.ArrayList;
import java.util.List;

public class ArrayNode extends ExpNode implements ASTNode {
    private List<ExpNode> value = new ArrayList<>();

    public List<ExpNode> getValues() {
        return value;
    }

    public void addValue(ExpNode value) {
        this.value.add(value);
    }

    @Override
    public String toString() {
        return "ArrayNode{" +
                "value=" + value +
                '}';
    }
}
