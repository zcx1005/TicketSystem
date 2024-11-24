import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Represents an airline company that manages flights, handles flight modifications,
 * and notifies passengers about flight updates such as cancellations and delays.
 */
public class AirlineCompany implements AirlineManagement {
    private final String name; // Airline company name
    private List<Flight> flights; // List of flights managed by the airline
    private PassengerNotification passengerNotification; // Notification strategy for passenger updates

    /**
     * Constructor to initialize the airline company with its name and an empty flight list.
     *
     * @param name The name of the airline company
     */
    public AirlineCompany(String name) {
        this.name = name;
        this.flights = new ArrayList<>();
    }

    /**
     * Retrieves the name of the airline company.
     *
     * @return The airline company name with a suffix
     */
    public String getName() {
        return this.name + " AirlineCompany";
    }

    /**
     * Adds a flight to the airline's flight list after validating the flight details.
     *
     * @param flight The flight object to be added
     * @return True if the flight was successfully added, otherwise false
     */
    @Override
    public boolean addFlight(Flight flight) {
        // Validate flight information
        if (flight.getFlightNumber().isEmpty() || flight.getDepartureTime() == null || flight.getArrivalTime() == null ||
                flight.getDeparture() == null || flight.getDestination() == null ||
                flight.getCapacity() <= 0) {
            System.out.println("Incomplete flight information. Please provide all details.");
            return false;
        }

        // Add flight to the list and set it open for reservation
        flights.add(flight);
        flight.setOpenForReservation(true);
        System.out.println("Flight " + flight.getFlightNumber() + " from " + flight.getDeparture() +
                " to " + flight.getDestination() + " has been successfully added.");
        return true;
    }

    /**
     * Cancels a flight by its flight number. If passengers have booked the flight,
     * they are notified, otherwise the flight is removed from the list.
     *
     * @param flightNumber The flight number of the flight to be cancelled
     */
    @Override
    public void cancelFlight(String flightNumber) {
        try {
            // Find the target flight
            Flight targetFlight = getFlightDetails(flightNumber);

            if (targetFlight == null) {
                System.out.println("Flight " + flightNumber + " not found.");
                return;
            }

            // If no passengers have booked
            if (targetFlight.getPassengers().isEmpty()) {
                flights.remove(targetFlight);
                System.out.println("No passengers have booked this flight. The flight has been successfully cancelled.");
            } else {
                targetFlight.setStatus("Cancelled");
                System.out.println("Flight " + flightNumber + " has been marked as cancelled.");

                // Notify passengers
                FlightNotificationStrategy notificationStrategy = new FlightNotificationStrategy(flights);
                notificationStrategy.sendNotification(
                        flightNumber,
                        "We apologize for the inconvenience. Please contact our customer service for assistance.",
                        "Cancellation"
                );

                System.out.println("All passengers who have booked this flight have been notified about the cancellation.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while cancelling the flight: " + e.getMessage());
        }
    }

    /**
     * Delays a flight and updates its departure and arrival times. Passengers
     * are notified about the new timings.
     *
     * @param flightNumber     The flight number of the delayed flight
     * @param newDepartureTime The new departure time
     * @param newArrivalTime   The new arrival time
     */
    @Override
    public void delayFlight(String flightNumber, LocalDateTime newDepartureTime, LocalDateTime newArrivalTime) {
        Flight targetFlight = getFlightDetails(flightNumber);

        if (targetFlight == null) {
            throw new IllegalArgumentException("Flight " + flightNumber + " not found.");
        }

        if (newDepartureTime.isAfter(targetFlight.getDepartureTime())
                && newArrivalTime.isAfter(targetFlight.getArrivalTime())) {
            targetFlight.setDepartureTime(newDepartureTime);
            targetFlight.setArrivalTime(newArrivalTime);
            targetFlight.setDelay(true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            System.out.println("Flight " + flightNumber + " has been delayed. New departure time: "
                    + newDepartureTime.format(formatter) + ", new arrival time: " + newArrivalTime.format(formatter));

            if (targetFlight.getPassengers().isEmpty()) {
                System.out.println("No passengers have booked this flight.");
            } else {
                FlightNotificationStrategy notificationStrategy = new FlightNotificationStrategy(flights);
                notificationStrategy.sendNotification(flightNumber,
                        "The flight has been delayed. New departure time: "
                                + newDepartureTime.format(formatter) + ", new arrival time: "
                                + newArrivalTime.format(formatter),
                        "Delay");

                System.out.println("All passengers who have booked this flight have been notified about the delay.");
            }
        } else {
            throw new IllegalArgumentException("New departure and arrival times must be later than the original times.");
        }
    }

    /**
     * Retrieves flight details by its flight number.
     *
     * @param flightNumber The flight number
     * @return The flight object if found, otherwise null
     */
    @Override
    public Flight getFlightDetails(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

    /**
     * Retrieves the list of all flights managed by the airline.
     *
     * @return A list of all flights
     */
    @Override
    public List<Flight> getAllFlights() {
        return flights;
    }

    /**
     * Identifies the most popular routes based on the number of flights.
     *
     * @return A list of the top popular routes with flight counts
     */
    public List<String> getPopularRoutes() {
        Map<String, Integer> routeCountMap = new HashMap<>();

        for (Flight flight : flights) {
            String route = flight.getDeparture() + " - " + flight.getDestination();
            routeCountMap.put(route, routeCountMap.getOrDefault(route, 0) + 1);
        }

        List<Map.Entry<String, Integer>> sortedRoutes = new ArrayList<>(routeCountMap.entrySet());
        sortedRoutes.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        List<String> popularRoutes = new ArrayList<>();
        for (int i = 0; i < Math.min(3, sortedRoutes.size()); i++) {
            Map.Entry<String, Integer> entry = sortedRoutes.get(i);
            popularRoutes.add(entry.getKey() + " (" + entry.getValue() + " flights)");
        }

        return popularRoutes;
    }

    /**
     * Retrieves flights that are nearly full (90% or more seats booked).
     *
     * @return A list of nearly full flights
     */
    public List<Flight> getNearlyFullFlights() {
        List<Flight> nearlyFullFlights = new ArrayList<>();

        for (Flight flight : flights) {
            int bookedPassengers = flight.getPassengers().size();
            int capacity = flight.getCapacity();

            if ((double) bookedPassengers / capacity > 0.9) {
                nearlyFullFlights.add(flight);
            }
        }

        if (nearlyFullFlights.isEmpty()) {
            System.out.println("All flights have sufficient available seats.");
        }

        return nearlyFullFlights;
    }
}
