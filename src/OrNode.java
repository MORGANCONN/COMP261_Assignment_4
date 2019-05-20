public class OrNode implements Condition {
    private Condition left;
    private Condition right;
    public OrNode(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean evaluate(Robot robot) {
        return left.evaluate(robot) || right.evaluate(robot);
    }

    @Override
    public String toString(){
        return "or("+left.toString()+","+right.toString()+")";
    }
}
