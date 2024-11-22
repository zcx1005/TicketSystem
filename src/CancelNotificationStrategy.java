import java.util.List;

/**
 * A notification strategy for informing passengers about flight cancellations.
 * Implements the PassengerNotification interface.
 */
public class CancelNotificationStrategy implements PassengerNotification {
    private List<Flight> flights; // List of flights provided externally

    /**
     * Constructor to initialize the CancelNotificationStrategy with a list of flights.
     *
     * @param flights The list of flights to manage notifications for.
     */
    public CancelNotificationStrategy(List<Flight> flights) {
        this.flights = flights;
    }

    /**
     * Sends a cancellation notification to all passengers of the specified flight.
     *
     * @param flightNumber The flight number of the flight to notify passengers about.
     * @param message      The custom message to include in the notification.
     */
    @Override
    public void sendNotification(String flightNumber, String message) {
        // 查找航班
        Flight flight = getFlightDetails(flightNumber);
        if (flight != null) {
            // 遍历乘客并发送通知
            for (Passenger passenger : flight.getPassengers()) {
                System.out.println("Sending cancellation notification to " + passenger.getName() + ": " + message);
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
