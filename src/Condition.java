public class Condition {
    private Sen conditionSensor;
    private Renlop renlop;
    private int conditionValue;
    public Condition(Sen conditionSensor, Renlop renlop,int conditionValue){
        this.conditionSensor = conditionSensor;
        this.renlop = renlop;
        this.conditionValue = conditionValue;
    }

    public boolean evaluate(Robot robot){
        return renlop.evaluate(conditionSensor.evaluate(robot),conditionValue);
    }

    @Override
    public String toString(){
        return renlop.toString()+"("+conditionSensor.toString()+","+conditionValue+")";
    }

    public Sen getConditionSensor() {
        return conditionSensor;
    }

    public void setConditionSensor(Sen conditionSensor) {
        this.conditionSensor = conditionSensor;
    }

    public Renlop getRenlop() {
        return renlop;
    }

    public void setRenlop(Renlop renlop) {
        this.renlop = renlop;
    }

    public int getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(int conditionValue) {
        this.conditionValue = conditionValue;
    }
}
