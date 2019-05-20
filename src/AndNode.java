public class AndNode implements Condition {
    private Condition left;
    private Condition right;

    public AndNode(Condition left, Condition right){
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean evaluate(Robot robot) {
        return left.evaluate(robot)&&right.evaluate(robot);
    }

    @Override
    public String toString(){
        return "and("+left.toString()+","+right.toString()+")";
    }
}
