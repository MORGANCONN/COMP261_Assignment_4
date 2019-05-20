public class Subtraction implements Expression {
    private Expression left;
    private Expression right;
    public Subtraction(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }
    @Override
    public int evaluate(Robot robot) {
        return left.evaluate(robot)-right.evaluate(robot);
    }
    @Override
    public String toString(){
        return "sub("+left.toString()+","+right.toString()+")";
    }
}
