import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AirlineCompany airlineCompany = new AirlineCompany("Mamba");
        System.out.println(airlineCompany.getName());

        Flight flight1 = new Flight("C919","China",
                "New York","08:00AM","17:00PM",160,new ArrayList<>());
        Flight flight2 = new Flight("C999","Beijing",
                "Guangzhou","10:00AM","13:00PM",140,new ArrayList<>());
        airlineCompany.addFlight(flight1);
        airlineCompany.addFlight(flight2);
        List<Flight> allFlights =  airlineCompany.getAllFlights();

        //打印当前所有航班列表
        if (allFlights.isEmpty()) {
            System.out.println("No flights available.");
        } else {
            System.out.println("All flights in the airline:");
            for (Flight flight : allFlights) {
                System.out.println(flight.toString()); // Assuming toString() method is implemented in Flight class
            }
        }

        Flight getF1 =  airlineCompany.getFlightDetails("C919");
//        Flight getF2 = airlineCompany.getFlightDetails("C918");
        System.out.println(getF1.toString());
//
        airlineCompany.delayFlight("C919","08:30AM","17:30PM");
        System.out.println(getF1);
        airlineCompany.cancelFlight("C919");
        System.out.println("test");

    }
}