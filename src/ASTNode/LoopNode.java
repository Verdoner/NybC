package ASTNode;

import java.util.ArrayList;
import java.util.List;

public class LoopNode extends StmtNode implements ASTNode{
    private ExpNode condition;
    private DeclNode<Integer> declaration;
    private AssignNode<String, Integer> assignment;
    private String type;
    private List<StmtNode> stmtList = new ArrayList<StmtNode>();

    public ExpNode getCondition() {
        return condition;
    }

    public void setCondition(ExpNode condition) {
        this.condition = condition;
    }

    public DeclNode<Integer> getDeclaration() {
        return declaration;
    }

    public void setDeclaration(DeclNode<Integer> declaration) {
        this.declaration = declaration;
    }

    public AssignNode<String, Integer> getAssignment() {
        return assignment;
    }

    public void setAssignment(AssignNode<String, Integer> assignment) {
        this.assignment = assignment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<StmtNode> getStmtList() {
        return stmtList;
    }

    public void addStmt(StmtNode stmt) {
        this.stmtList.add(stmt);
    }

    @Override
    public String toString() {
        return "LoopNode{" +
                "condition=" + condition +
                ", declaration=" + declaration +
                ", assignment=" + assignment +
                ", type='" + type + '\'' +
                ", stmtList=" + stmtList +
                '}';
    }
}
