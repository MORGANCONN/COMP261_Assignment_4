public class WhileNode extends PayloadNode {
    @Override
    public void execute(Robot robot){
        while(condition.isTrue()){
            getLoopNodeSubTree().execute(robot);
        }
        if(getChild()!=null){
            getChild().execute(robot);
        }
    }
}
