public class IfNode extends PayloadNode {

    @Override
    public void execute(Robot robot) {
        if(condition.evaluate(robot)){
            getPayloadNodeSubTree().execute(robot);
        }
        if(getChild()!=null){
            getChild().execute(robot);
        }
    }
    @Override
    public String toString(){
        return getChild()!=null ? "If"+"("+condition.toString()+")"+"{\n" + block.toString() + "}"+getChild().toString():"If"+"("+condition.toString()+")"+"{\n" + block.toString() + "}";
    }
}
