package ASTNode;

public class ParenthNode extends ExpNode implements ASTNode {
    private ExpNode Inner;

    public ExpNode getInner() {
        return Inner;
    }

    public void setInner(ExpNode inner) {
        Inner = inner;
    }

    @Override
    public String toString() {
        return "ParenthNode{" +
                "Inner=" + Inner +
                '}';
    }
}
