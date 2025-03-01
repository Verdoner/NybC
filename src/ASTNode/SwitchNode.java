package ASTNode;

import java.util.ArrayList;
import java.util.List;

public class SwitchNode extends StmtNode implements ASTNode {
    private ExpNode switchCond;
    private List<CaseNode> cases = new ArrayList<CaseNode>();

    public ExpNode getSwitchCond() {
        return switchCond;
    }

    public void setSwitchCond(ExpNode switchCond) {
        this.switchCond = switchCond;
    }

    public List<CaseNode> getCases() {
        return cases;
    }

    public void addCase(CaseNode casing) {
        this.cases.add(casing);
    }

    @Override
    public String toString() {
        return "SwitchNode{" +
                "switchCond=" + switchCond +
                ", cases=" + cases +
                '}';
    }
}
