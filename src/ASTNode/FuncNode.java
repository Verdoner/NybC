package ASTNode;
import java.util.ArrayList;
import java.util.List;

public class FuncNode extends StmtNode implements ASTNode {
    private String id;
    private List<DeclNode> param = new ArrayList<>();

    private List<StmtNode> stmtList = new ArrayList<StmtNode>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DeclNode> getParam() {
        return param;
    }

    public void addParam(DeclNode param) {
        this.param.add(param);
    }

    public List<StmtNode> getStmsList() {
        return stmtList;
    }

    public void addStmt(StmtNode stmt) {
        this.stmtList.add(stmt);
    }

    @Override
    public String toString() {
        return "FuncNode{" +
                "id='" + id + '\'' +
                ", param=" + param +
                ", stmtList=" + stmtList +
                '}';
    }
}
