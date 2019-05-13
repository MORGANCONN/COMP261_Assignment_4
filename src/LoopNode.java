public class LoopNode extends BaseNode {
    BaseNode loopNodeSubTree = null;

    public void setLoopNodeSubTree(BaseNode toSet) {
        loopNodeSubTree = toSet;
    }

    public BaseNode getLoopNodeSubTree(){
        return loopNodeSubTree;
    }

    @Override
    public void execute(Robot robot){
        while(true){
            loopNodeSubTree.execute(robot);
        }
    }

    @Override
    public String toString(){
        String toReturn = "Loop{\n";
        BaseNode incrementedNode = loopNodeSubTree;
        while(incrementedNode!=null && incrementedNode.getChild()!=null){
            toReturn = toReturn + incrementedNode.toString() + "\n";
            incrementedNode = incrementedNode.getChild();
        }
        toReturn = toReturn + "}";
        return toReturn;
    }
}
