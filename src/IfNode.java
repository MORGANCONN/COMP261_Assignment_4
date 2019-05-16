public class IfNode extends PayloadNode {

    @Override
    public void execute(Robot robot) {
        if(condition.isTrue()){
            getLoopNodeSubTree().execute(robot);
        }
        if(getChild()!=null){
            getChild().execute(robot);
        }
    }
}
