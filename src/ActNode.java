public class ActNode extends BaseNode {
    actionType action;
    enum actionType{
        move,
        turnL,
        turnR,
        takeFuel,
        wait,
        shieldOn,
        shieldOff,
        turnAround
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
            case shieldOn:
                robot.setShield(true);
                break;
            case shieldOff:
                robot.setShield(false);
                break;
            case turnAround:
                robot.turnAround();
                break;
        }
        if(this.getChild()!=null){
            this.getChild().execute(robot);
        }
    }

    @Override
    public String toString(){
        return this.getChild()!=null ? action.toString() + ";\n" +this.getChild().toString():action.toString() + ";\n";
    }
}
