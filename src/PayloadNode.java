public class PayloadNode extends BaseNode{
    // the condition that if and while statements have
    Condition condition = null;
    public void setCondition(Condition condition){
        this.condition = condition;
    }
    public Condition getCondition(){
        return condition;
    }

    // the block that loop, if and while statements have
    BlockNode block = null;
    public void setPayloadNodeSubTree(BlockNode toSet) {
        block = toSet;
    }
    public BlockNode getPayloadNodeSubTree(){
        return block;
    }

}
