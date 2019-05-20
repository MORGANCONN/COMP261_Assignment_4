public class ElseNode extends PayloadNode {
    @Override
    public void execute(Robot robot) {
        getPayloadNodeSubTree().execute(robot);
        if (getChild() != null) {
            getChild().execute(robot);
        }
    }
    @Override
    public String toString(){
        return getChild()!=null ? "Else"+"{\n" + block.toString() + "}"+getChild().toString():"Else"+"{\n" + block.toString() + "}";
    }
}
