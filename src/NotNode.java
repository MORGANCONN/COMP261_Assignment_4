public class NotNode implements Condition {
    Condition nottedCondition;
    public NotNode(Condition condition) {
        this.nottedCondition = condition;
    }

    @Override
    public boolean evaluate(Robot robot) {
        return !nottedCondition.evaluate(robot);
    }
    @Override
    public String toString(){
        return "not("+nottedCondition.toString()+")";
    }
}
