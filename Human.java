import java.util.concurrent.ThreadLocalRandom;

public class Human {
    private int currentFloor;
    private int desiredFloor;
    private final int uid;
    private static int count = 0;

    Human(int currentFloor, int desiredFloor) {
        this.currentFloor = currentFloor;
        this.desiredFloor = desiredFloor;
        count++;
        uid = count;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getDesiredFloor() {
        return desiredFloor;
    }

    public void setDesiredFloor(int desiredFloor) {
        this.desiredFloor = desiredFloor;
    }

    public Direction findDir(){
        if (currentFloor < desiredFloor) {
            return Direction.UP;
        }
        return Direction.DOWN;
    }

    public static Human getRandomHuman(int maxFloor) {
        int currentFloor = ThreadLocalRandom.current().nextInt(1, maxFloor + 1);
        int desiredFloor = ThreadLocalRandom.current().nextInt(1, maxFloor + 1);
        if (desiredFloor == currentFloor) {
            desiredFloor = currentFloor + ThreadLocalRandom.current().nextInt(-currentFloor + 1, maxFloor - currentFloor);
        }
        return new Human(currentFloor, desiredFloor);
    }

    public int getUid() {
        return uid;
    }
}