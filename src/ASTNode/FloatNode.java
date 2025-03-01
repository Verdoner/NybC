package ASTNode;

public class FloatNode extends ExpNode implements ASTNode{
    private float value;
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(value);
    }
}
