public class RenlopCondition implements Condition {
    private Expression left;
    private Renlop renlop;
    private Expression right;
    public RenlopCondition(Renlop renlop,Expression left, Expression right){
        this.left = left;
        this.renlop = renlop;
        this.right = right;
    }
    public boolean evaluate(Robot robot){
        return renlop.evaluate(left.evaluate(robot), right.evaluate(robot));
    }

    @Override
    public String toString(){
        return renlop.toString()+"("+ left.toString()+","+ right +")";
    }


}
