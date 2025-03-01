package ASTNode;

public class CtrlFlowNode extends StmtNode implements ASTNode {
    private String type;
    private ExpNode returnExp = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ExpNode getReturnExp() {
        return returnExp;
    }

    public void setReturnExp(ExpNode returnExp) {
        this.returnExp = returnExp;
    }

    @Override
    public String toString() {
        return "CtrlFlowNode{" +
                "type='" + type + '\'' +
                ", returnExp=" + returnExp +
                '}';
    }
}
