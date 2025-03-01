package ASTNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgramNode implements ASTNode{
    private List<ASTNode> stmtList = new ArrayList<>();

    public List<ASTNode> getStmtList() {
        return stmtList;
    }

    public void addStmt(ASTNode stmt) {
        this.stmtList.add(stmt);
    }

    @Override
    public String toString() {
        return "ProgramNode{" +
                "stmtList=" + stmtList +
                '}';
    }
}



