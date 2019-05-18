public class WhileNode extends PayloadNode {
    @Override
    public void execute(Robot robot){
        while(condition.evaluate(robot)){
            getPayloadNodeSubTree().execute(robot);
        }
        if(getChild()!=null){
            getChild().execute(robot);
        }
    }
    @Override
    public String toString(){
        return getChild()!=null ? "While"+"("+condition.toString()+")"+"{\n" + block.toString() + "}"+getChild().toString():"While"+"("+condition.toString()+")"+"{\n" + block.toString() + "}";
    }
}
