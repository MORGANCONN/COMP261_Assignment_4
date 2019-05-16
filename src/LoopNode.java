public class LoopNode extends PayloadNode {

    @Override
    public void execute(Robot robot){
        while(true){
            block.execute(robot);
        }
    }

    @Override
    public String toString(){
        return "loop{\n"+block.toString()+"}";
    }
}
