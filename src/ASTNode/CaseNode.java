package ASTNode;

import java.util.ArrayList;
import java.util.List;

public class CaseNode extends SwitchNode implements ASTNode {
    private ExpNode caseExp = null;
    private List<StmtNode> stmtList = new ArrayList<StmtNode>();

    public ExpNode getCaseExp() {
        return caseExp;
    }

    public void setCaseExp(ExpNode caseExp) {
        this.caseExp = caseExp;
    }

    public List<StmtNode> getStmtList() {
        return stmtList;
    }

    public void addStmt(StmtNode stmt) {
        this.stmtList.add(stmt);
    }

    @Override
    public String toString() {
        return "CaseNode{" +
                "caseExp=" + caseExp +
                ", stmtList=" + stmtList +
                '}';
    }
}
