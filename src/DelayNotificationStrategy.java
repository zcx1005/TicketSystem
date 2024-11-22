import java.util.List;

/**
 * A notification strategy for informing passengers about flight delays.
 * Implements the PassengerNotification interface.
 */
public class DelayNotificationStrategy implements PassengerNotification {
    private List<Flight> flights; // List of flights provided externally

    /**
     * Constructor to initialize the DelayNotificationStrategy with a list of flights.
     *
     * @param flights The list of flights to manage notifications for.
     */
    public DelayNotificationStrategy(List<Flight> flights) {
        this.flights = flights;
    }

    /**
     * Sends a delay notification to all passengers of the specified flight.
     *
     * @param flightNumber The flight number of the delayed flight.
     * @param message      The custom message to include in the notification, such as new timings.
     */
    @Override
    public void sendNotification(String flightNumber, String message) {
        // Iterate over the flight list to find the target flight
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                // If the flight is found, notify all passengers
                for (Passenger passenger : flight.getPassengers()) {
                    System.out.println("Dear " + passenger.getName() + ",");
                    System.out.println("We regret to inform you that your flight " + flightNumber + " is delayed.");
                    System.out.println(message); // Additional delay information
                    System.out.println("Thank you for your understanding.");
                }
                return; // Exit after sending notifications for the matched flight
            }
        }
        // If no flight matches the provided flight number
        System.out.println("Flight " + flightNumber + " not found.");
    }
}
