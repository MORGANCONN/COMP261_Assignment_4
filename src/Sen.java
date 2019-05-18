public class Sen {
    senType sensorType;
    enum senType{
        fuelLeft,
        oppLR,
        oppFB,
        numBarrels,
        barrelLR,
        barrelFB,
        wallDist
    }
    public Sen(senType sensorType){
        this.sensorType = sensorType;
    }

    @Override
    public String toString(){
        return sensorType.toString();
    }

    public int evaluate(Robot robot){
        switch (sensorType){
            case oppFB:
                return robot.getOpponentFB();
            case oppLR:
                return robot.getOpponentLR();
            case barrelFB:
                return robot.getClosestBarrelFB();
            case barrelLR:
                return robot.getClosestBarrelLR();
            case fuelLeft:
                return robot.getFuel();
            case wallDist:
                return robot.getDistanceToWall();
            case numBarrels:
                return robot.numBarrels();
        }
        return -1;
    }
}
