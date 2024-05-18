import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

public class Elevator implements Runnable {
    private final int timeBetweenFloors, countFloors;
    private int currentFloor;
    public LinkedBlockingQueue<Human> peopleOnFloor = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Human> passengers = new LinkedBlockingQueue<>();
    private Direction direction;
    private final String color, reset = "\u001B[0m";
    static int countRequests;

    public Direction getDirection() {
        return direction;
    }

    Elevator(String color, int countPeople, int countFloors, int timeBetweenFloors) {
        this.color = color;
        direction = Direction.UP;
        currentFloor = 1;
        this.timeBetweenFloors = timeBetweenFloors;
        countRequests = countPeople;
        this.countFloors = countFloors;
    }

    private void sleep(int time) {
        try {
            Thread.sleep((long) time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void step(int summand, int finalFloor) {
        while (currentFloor != finalFloor + summand) {
            //System.out.printf("%d\t%d\n", peopleOnFloor.size(), passengers.size());
            for (Human h : peopleOnFloor) {
                if (h.findDir() == direction && h.getCurrentFloor() == currentFloor) {
                    System.out.printf(color+"Пассажир %d зашёл в лифт на этаже %d\n"+reset, h.getUid(), currentFloor);
                    passengers.add(h);
                    if (direction == Direction.UP) {
                        finalFloor = Math.max(h.getDesiredFloor(), finalFloor);
                    } else {
                        finalFloor = Math.min(h.getDesiredFloor(), finalFloor);
                    }
                    peopleOnFloor.remove(h);
                }
            }
            for (Human h : passengers) {
                if (h.getDesiredFloor() == currentFloor) {
                    passengers.remove(h);
                    countRequests--;
                    //System.out.println(countRequests);
                    System.out.printf(color+"Пассажир %d вышел из лифта на этаже %d\n"+reset, h.getUid(), currentFloor);
                }
            }
            if (finalFloor != currentFloor) {
                currentFloor += summand;
            } else {
                finalFloor -= summand;
            }
            sleep(timeBetweenFloors);
        }
    }

    public void run() {
        while (countRequests > 0) {
            if (peopleOnFloor.isEmpty() && passengers.isEmpty()) {
                sleep(500);
            } else if (direction == Direction.UP) {
                //System.out.println("UP");
                int finalFloor = 0, newCurrentFloor = countFloors + 1;
                for (Human h : peopleOnFloor) {
                    if (h.findDir() == direction) {
                        finalFloor = Math.max(finalFloor, h.getDesiredFloor());
                        newCurrentFloor = Math.min(newCurrentFloor, h.getCurrentFloor());
                    }
                }
                if (finalFloor == 0) {
                    direction = Direction.DOWN;
                    continue;
                }
                sleep(Math.abs(newCurrentFloor - currentFloor) * timeBetweenFloors);
                currentFloor = newCurrentFloor;
                step(1, finalFloor);
            } else if (direction == Direction.DOWN) {
                //System.out.println("DOWN");
                int finalFloor = countFloors + 1, newCurrentFloor = 0;
                for (Human h : peopleOnFloor) {
                    if (h.findDir() == direction) {
                        finalFloor = Math.min(finalFloor, h.getDesiredFloor());
                        newCurrentFloor = Math.max(newCurrentFloor, h.getCurrentFloor());
                    }
                }
                if (finalFloor == countFloors + 1) {
                    direction = Direction.UP;
                    continue;
                }
                sleep(Math.abs(newCurrentFloor - currentFloor) * timeBetweenFloors);
                currentFloor = newCurrentFloor;
                step(-1, finalFloor);
            }
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }
}
