public class ActNode extends BaseNode {
    actionType action;
    private Expression actionArgument = null;
    private boolean repeating = false;
    public void setActArgument(Expression numToSet) {
        actionArgument = numToSet;
        repeating = true;
    }

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
        for(int i = 0;i<(actionArgument==null?1:actionArgument.evaluate(robot));i++) {
            switch (action) {
                case move:
                    robot.move();
                    break;
                case wait:
                    robot.idleWait();
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
        }
        if(this.getChild()!=null){
            this.getChild().execute(robot);
        }
    }

    @Override
    public String toString(){
        return this.getChild()!=null ? action.toString() + (repeating ? "("+actionArgument+")" +";\n":";\n") +this.getChild().toString():action.toString() +(repeating ? "("+actionArgument+")" +";\n":";\n");
    }
}
