public class Renlop {
    private renlopTypes renlopType;
    enum renlopTypes{
        lt,
        gt,
        eq
    }
    public Renlop(renlopTypes renlopType){
        this.renlopType = renlopType;
    }

    @Override
    public String toString(){
        return renlopType.toString();
    }
    /**
     *  returns a boolean depending on the value that the sensor returns, the renlop type and the value supplied
     *  to check against.
     * @param sensorValue The value that the sensor returns
     * @param valueToCheckAgainst the value that is being checked against
     * @return the boolean that indicates if the logical statement is true or false
     */
    public boolean evaluate(int sensorValue,int valueToCheckAgainst){
        switch (renlopType){
            case eq:
                return sensorValue == valueToCheckAgainst;
            case gt:
                return sensorValue > valueToCheckAgainst;
            case lt:
                return sensorValue < valueToCheckAgainst;
        }
        return false;
    }
}
