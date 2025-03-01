package ASTNode;

public class StringNode extends ExpNode implements ASTNode {
    private String value;

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringNode{" +
                "value=" + value +
                '}';
    }
}
