public class BaseNode implements RobotProgramNode {
    protected BaseNode child = null;
    protected BaseNode parent = null;
    @Override
    public void execute(Robot robot) {
    }

    /**
     * Sets the node's child
     * @param child the node to set as the child
     */
    public void setChild(BaseNode child){
        this.child = child;
    }

    /**
     * Returns the child of the node
     */
    public BaseNode getChild(){
        return child;
    }

    public void setParent(BaseNode parent){
        this.parent = parent;
    }

    public BaseNode getParent(){
        return parent;
    }
}
