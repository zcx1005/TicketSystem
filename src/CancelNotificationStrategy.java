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
        // Iterate over the flight list to find the target flight
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                // If the flight is found, notify all passengers
                for (Passenger passenger : flight.getPassengers()) {
                    System.out.println("Dear " + passenger.getName() + ",");
                    System.out.println("We regret to inform you that your flight " + flightNumber + " has been cancelled.");
                    System.out.println(message); // Additional cancellation message
                    System.out.println("We apologize for the inconvenience caused.");
                }
                return; // Exit after sending notifications for the matched flight
            }
        }
        // If no flight matches the provided flight number
        System.out.println("Flight " + flightNumber + " not found.");
    }
}
