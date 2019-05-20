public class Add implements Expression{
    private Expression left;
    private Expression right;
    public Add(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }
    @Override
    public int evaluate(Robot robot) {
        return left.evaluate(robot)+right.evaluate(robot);
    }

    @Override
    public String toString(){
       return "add("+left.toString()+","+right.toString()+")";
    }
}
