package ASTNode;

public class StmtNode implements ASTNode {
    protected int startLine = 0;
    protected int endLine = 0;

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

}
