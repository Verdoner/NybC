package ASTNode;

public class DeclNode <T> extends StmtNode implements ASTNode {
    private T id;
    private T value;

    public DeclNode(T id, T value) {
        this.id = id;
        this.value = value;
    }


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public DeclNode(T id, T value, int startLine, int endLine) {
        this.id = id;
        this.value = value;
        this.startLine = startLine;
        this.endLine = endLine;
    }
    public DeclNode(T id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeclNode{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }
}
