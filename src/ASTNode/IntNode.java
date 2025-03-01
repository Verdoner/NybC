package ASTNode;

public class IntNode extends ExpNode implements ASTNode {

    public int value;


    public IntNode(int value) {
        this.value = value;
    }

    public IntNode() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntNode{" +
                "value=" + value +
                '}';
    }
}
