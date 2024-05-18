import java.util.Random;


public class Building implements Runnable {
    private final int countFloors;
    private int countRequests;
    Elevator[] elevators;

    Building(int countFloors, int countRequests, Elevator[] elevators) {
        if (countFloors < 2 || countRequests < 0) {
            throw new IllegalArgumentException("incorrect parameters of Building");
        }
        this.countFloors = countFloors;
        this.countRequests = countRequests;
        this.elevators = elevators;
    }

    private boolean isSuitableElevator(Human h, Elevator elevator) {
        if (h.findDir() == Direction.UP && h.getCurrentFloor() >= elevator.getCurrentFloor()) {
            return true;
        } else return h.findDir() == Direction.DOWN && h.getCurrentFloor() <= elevator.getCurrentFloor();
    }

    private void selectNearestElevator(Human h) {
        if (Math.abs(elevators[0].getCurrentFloor() - h.getCurrentFloor()) > Math.abs(elevators[1].getCurrentFloor() - h.getDesiredFloor())) {
            elevators[1].peopleOnFloor.add(h);
        } else {
            elevators[0].peopleOnFloor.add(h);
        }
    }

    private void selectBestElevator(Human h) {
        if (elevators[0].getDirection() == elevators[1].getDirection()) {
            selectNearestElevator(h);
        } else if (h.findDir() == elevators[0].getDirection() && isSuitableElevator(h, elevators[0])) {
            elevators[0].peopleOnFloor.add(h);
        } else if (h.findDir() == elevators[1].getDirection() && isSuitableElevator(h, elevators[1])) {
            elevators[1].peopleOnFloor.add(h);
        } else if (elevators[0].getDirection() == Direction.STOP) {
            elevators[0].peopleOnFloor.add(h);
        } else if (elevators[1].getDirection() == Direction.STOP) {
            elevators[1].peopleOnFloor.add(h);
        } else {
            selectNearestElevator(h);
        }
    }

    public void run() {
        Random engine = new Random();
        while (countRequests > 0) {
            Human h = Human.getRandomHuman(countFloors);
            System.out.printf("Пассажир %d: currentFloor=%d, desiredFloor=%d\n",
                    h.getUid(), h.getCurrentFloor(), h.getDesiredFloor());
            selectBestElevator(h);
            try {
                Thread.sleep(200 + engine.nextInt(-100, 100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            countRequests--;
        }
    }

    public int getCountFloors() {
        return countFloors;
    }

    public int getCountElevators() {
        int countElevators = 2;
        return countElevators;
    }

    public int getCountRequests() {
        return countRequests;
    }

    public void setCountRequests(int countRequests) {
        if (countRequests < 0) {
            throw new IllegalArgumentException("incorrect number of requests in Building");
        }
        this.countRequests = countRequests;
    }
}
