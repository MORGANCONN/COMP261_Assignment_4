public class PayloadNode extends BaseNode{
    Condition condition = null;
    public void setCondition(Condition condition){
        this.condition = condition;
    }

    public Condition getCondition(){
        return condition;
    }

    BlockNode block = null;

    public void setLoopNodeSubTree(BlockNode toSet) {
        block = toSet;
    }

    public BlockNode getLoopNodeSubTree(){
        return block;
    }
}
