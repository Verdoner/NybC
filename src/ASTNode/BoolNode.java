package ASTNode;

public class BoolNode extends ExpNode implements ASTNode {
    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    private boolean value;

    @Override
    public String toString() {
        return "BoolNode{" +
                "value=" + value +
                '}';
    }
}








