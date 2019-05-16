public class BlockNode extends BaseNode {
    BaseNode block = null;
    public void setBlock(BaseNode toSet) {
        block = toSet;
    }

    public BaseNode getBlock(){
        return block;
    }

    @Override
    public void execute(Robot robot){
            block.execute(robot);
    }

    @Override
    public String toString(){
        String toReturn = "";
        BaseNode checkNode = block;
        while(checkNode!=null && checkNode.getChild()!=null){
            toReturn = toReturn + checkNode.toString() + "\n";
            checkNode = checkNode.getChild();
        }
        return toReturn;
    }
}
