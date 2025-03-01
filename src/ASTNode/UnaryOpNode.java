package ASTNode;

public class UnaryOpNode extends ExpNode implements ASTNode{
    private ExpNode Right;
    private String Op;

    public ExpNode getRight() {
        return Right;
    }

    public void setRight(ExpNode right) {
        Right = right;
    }

    public String getOp() {
        return Op;
    }

    public void setOp(String op) {
        Op = op;
    }

    public UnaryOpNode(ExpNode right, String op) {
        Right = right;
        Op = op;
    }

    public UnaryOpNode() {
    }

    @Override
    public String toString() {
        return "UnaryOpNode{" +
                "Op='" + Op + '\'' +
                ",  Right=" + Right +
                '}';
    }
}
