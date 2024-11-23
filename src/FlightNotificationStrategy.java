import java.util.List;

/**
 * A notification strategy for informing passengers about flight events.
 * Implements the PassengerNotification interface.
 */
public class FlightNotificationStrategy implements PassengerNotification {
    private List<Flight> flights; // List of flights provided externally

    /**
     * Constructor to initialize the FlightNotificationStrategy with a list of flights.
     *
     * @param flights The list of flights to manage notifications for.
     */
    public FlightNotificationStrategy(List<Flight> flights) {
        this.flights = flights;
    }

    /**
     * Sends a notification to all passengers of the specified flight.
     *
     * @param flightNumber The flight number of the flight to notify passengers about.
     * @param message      The custom message to include in the notification.
     * @param type         The type of notification (e.g., "Cancellation", "Delay").
     */
    public void sendNotification(String flightNumber, String message, String type) {
        // 查找航班
        Flight flight = getFlightDetails(flightNumber);
        if (flight != null) {
            // 遍历乘客并发送通知
            for (Passenger passenger : flight.getPassengers()) {
                System.out.println("Sending " + type + " notification to " + passenger.getName() + ": " + message);
                // 这里可以模拟发送邮件或短信等
            }
        } else {
            System.out.println("Flight " + flightNumber + " not found.");
        }
    }

    private Flight getFlightDetails(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

}
