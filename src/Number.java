public class Number implements Expression {
    private int number;
    public Number(int num){
        this.number = num;
    }
    @Override
    public int evaluate(Robot robot) {
        return number;
    }
    @Override
    public String toString(){
        return Integer.toString(number);
    }
}
