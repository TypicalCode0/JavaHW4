import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int countRequests, countFloors, timeBetweenFloors;
        System.out.print("Введите количество людей, которым будет необходим лифт -> ");
        Scanner scanner = new Scanner(System.in);
        countRequests = scanner.nextInt();
        System.out.print("Введите количество этажей в здании -> ");
        countFloors = scanner.nextInt();
        System.out.print("Введите скорость передвижения лифта между ближайшими этажами в милисекундах -> ");
        timeBetweenFloors = scanner.nextInt();
        Elevator e1 = new Elevator( "\u001B[32m",  countRequests, countFloors, timeBetweenFloors);
        Elevator e2 = new Elevator( "\u001B[34m",  countRequests, countFloors, timeBetweenFloors);
        Elevator[] elevators = {e1, e2};
        Building house = new Building(countFloors, countRequests, elevators);

        new Thread(house).start();
        new Thread(e1).start();
        new Thread(e2).start();
    }
}
