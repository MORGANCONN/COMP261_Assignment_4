public class ActNode extends BaseNode {
    actionType action;
    enum actionType{
        move,
        turnL,
        turnR,
        takeFuel,
        wait
    }
    public ActNode(actionType action){
        this.action = action;
    }
    @Override
    public void execute(Robot robot) {
        switch (action){
            case move:
                robot.move();
                break;
            case wait:
                break;
            case turnL:
                robot.turnLeft();
                break;
            case turnR:
                robot.turnRight();
                break;
            case takeFuel:
                robot.takeFuel();
                break;
        }
    }
}
