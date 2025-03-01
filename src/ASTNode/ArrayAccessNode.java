package ASTNode;

public class ArrayAccessNode <T> extends ExpNode implements ASTNode{
    private String id;
    private T index;

    public ArrayAccessNode(String id, T index, int startLine, int endLine){
        this.id = id;
        this.index = index;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setIndex(T index){
        this.index = index;
    }

    public T getIndex(){
        return index;
    }

    @Override
    public String toString() {
        return "ArrayAccessNode{" +
                "id='" + id + '\'' +
                ", index=" + index +
                ", startLine=" + startLine +
                ", endLine=" + endLine +
                '}';
    }
}
