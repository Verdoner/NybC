package ASTNode;

import java.util.ArrayList;
import java.util.List;

public class CallFuncNode extends ExpNode implements ASTNode{
    private String id;
    private List<ExpNode> args = new ArrayList<ExpNode>();

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void addArgs(ExpNode args){
        this.args.add(args);
    }

    public List<ExpNode> getArgs(){
        return args;
    }

    @Override
    public String toString() {
        return "CallFuncNode{" +
                "id='" + id + '\'' +
                ", args=" + args +
                '}';
    }
}
