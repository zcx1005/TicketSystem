import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AirlineCompany airlineCompany = new AirlineCompany("Mamba");
        System.out.println("Welcome to " + airlineCompany.getName());

        // Create a custom VNet to parse date and time strings
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Create a new flight
        Flight flight1 = new Flight("C919", "China",
                "New York", LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter), 160, new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("C999", "Beijing",
                "Guangzhou", LocalDateTime.parse("2024-11-11 08:00", formatter), LocalDateTime.parse("2024-11-11 10:30", formatter), 140, new ArrayList<>(), new ArrayList<>());
        // Add a new flight
        airlineCompany.addFlight(flight1);
        airlineCompany.addFlight(flight2);
        Flight selectedFlight = null; // External declaration variables
        Passenger passenger = null;   // External declaration variables
        List<Passenger> passengers = new ArrayList<>(); // Store all created passenger instances
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // Print menu
            System.out.println("\n=== Airline Booking System ===");
            System.out.println("1. View all flights");
            System.out.println("2. View flight details");
            System.out.println("3. Create a passenger and book a seat");
            System.out.println("4. Modify a reservation and if a passenger is not found, create one and book a seat ");
            System.out.println("5. Cancel a reservation and if a passenger is not found, create one and book a seat");
            System.out.println("6. Set flight delay");
            System.out.println("7. View popular routes");
            System.out.println("8. View nearly full flights");
            System.out.println("9. View passenger reservations");
            System.out.println("10. View all passengers for a flight");
            System.out.println("11. Exit");
            System.out.println("Choose an option: ");

            // Obtain user selection
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // View all flights
                    System.out.println("1. View all flights");
                    List<Flight> allFlights = airlineCompany.getAllFlights();
                    if (allFlights.isEmpty()) {
                        System.out.println("No flights available.");
                    } else {
                        System.out.println("All flights in the airline（All available flights）:");
                        for (Flight flight : allFlights) {
                            System.out.println(flight.toString());
                        }
                    }
                    break;

                case 2:
                    // View flight details
                    System.out.println("2. View flight details");
                    System.out.println(" Viewing Flight Details by entering flight number");
                    String flightNumber = scanner.nextLine();
                    selectedFlight = airlineCompany.getFlightDetails(flightNumber);
                    if (selectedFlight != null) {
                        System.out.println("\nFlight Details:");
                        System.out.println(selectedFlight.toString());
                    } else {
                        System.out.println("\nFlight not found.");
                    }
                    break;

                case 3:// Whether the reservation is successful or not, it will be created!,
                    // It's in the passenger. Not necessarily included in the passenger list in Fight.
                    // Book a seat
                    System.out.println("3.Create a passenger and book a seat");
                    System.out.println("Enter passenger name: ");
                    String passengerName = scanner.nextLine();
                    passenger = new Passenger(passengerName, new ArrayList<>());

                    passengers.add(passenger);  // Add a new passenger object to the list

                    System.out.println("Enter flight number to book: ");
                    flightNumber = scanner.nextLine();
                    selectedFlight = airlineCompany.getFlightDetails(flightNumber);
                    if (selectedFlight != null) {
                        System.out.println("Enter seat type (FirstClass/Economy): ");
                        String seatType = scanner.nextLine();
                        System.out.println("Enter service type: ");
                        String serviceType = scanner.nextLine();
                        System.out.println(selectedFlight.bookSeat(passenger, seatType, serviceType));//If it is not Economy and FirstClass, it will prompt that it cannot be inserted
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case 4:
                    // Modify a reservation and if a passenger is not found, create one
                    System.out.println("4. Modify a reservation and if a passenger is not found, create one and book a seat ");
                    System.out.println("Enter passenger name: ");
                    passengerName = scanner.nextLine();
                    // Find all passengers who match the input name
                    List<Passenger> foundPassengersToModify = new ArrayList<>();
                    // Search for existing passengers, if not found, prompt and create a new passenger
                    //Passenger existingPassenger = null;
                    for (Passenger p : passengers) {
                        if (p.getName().equalsIgnoreCase(passengerName)) {
                            foundPassengersToModify.add(p); // Add all matching passengers to the list
                        }
                    }

                    if (foundPassengersToModify.isEmpty()) {
                        System.out.println("Passenger not found. Creating a new passenger.");
                        // If the passenger cannot be found, create a new passenger instance and make a reservation
                        Passenger newPassenger = new Passenger(passengerName, new ArrayList<>());
                        passengers.add(newPassenger);  // Add new passengers to the passenger list

                        // Make a new flight reservation
                        System.out.println("Enter flight number to book: ");
                        flightNumber = scanner.nextLine();
                        selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                        if (selectedFlight != null) {
                            System.out.println("Enter seat type (FirstClass/Economy): ");
                            String seatType = scanner.nextLine();
                            System.out.println("Enter service type: ");
                            String serviceType = scanner.nextLine();
                            // Make a new reservation
                            System.out.println(selectedFlight.bookSeat(newPassenger, seatType, serviceType));
                        } else {
                            System.out.println("Flight not found.");
                        }
                    } else {
                        // If multiple passengers with the same name are found, modify their reservations one by one
                        System.out.println("Found the following passengers with the same name as '" + passengerName + "':");
                        for (Passenger passengerForModify : foundPassengersToModify) {
                            System.out.println("Passenger: " + passengerForModify.getName() + " is modifying the reservation...");
                            System.out.println("Please enter the flight number to modify: ");
                            flightNumber = scanner.nextLine();
                            selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                            if (selectedFlight != null) {
                                System.out.println("Enter new seat type (FirstClass/Economy): ");
                                String newSeatType = scanner.nextLine();
                                System.out.println("Enter new service type: ");
                                String newServiceType = scanner.nextLine();
                                // Modify reservation
                                passengerForModify.modifyReservation(selectedFlight, newSeatType, newServiceType);
                            } else {
                                System.out.println("Flight not found.");
                            }
                        }
                    }
                    break;

                case 5:
                    // Cancel a reservation, if the passenger is not found, create one
                    System.out.println("5. Cancel a reservation and if a passenger is not found, create one and book a seat");
                    System.out.println("Enter passenger name: ");
                    passengerName = scanner.nextLine();
                    // Find all passengers who match the input name
                    List<Passenger> foundPassengersToCancel = new ArrayList<>();
                    // Search for existing passengers, if not found, prompt and create a new passenger
                    //existingPassenger = null;
                    for (Passenger p : passengers) {
                        if (p.getName().equalsIgnoreCase(passengerName)) {
                            foundPassengersToCancel.add(p);

                        }
                    }

                    if (foundPassengersToCancel.isEmpty()) {
                        System.out.println("Passenger not found. Creating a new passenger.");
                        // If the passenger cannot be found, create a new passenger instance and make a reservation
                        Passenger existingPassenger = new Passenger(passengerName, new ArrayList<>());
                        passengers.add(existingPassenger);  // Add new passengers to the list

                        // Make a new flight reservation
                        System.out.println("Enter flight number to book: ");
                        flightNumber = scanner.nextLine();
                        selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                        if (selectedFlight != null) {
                            System.out.println("Enter seat type (FirstClass/Economy): ");
                            String seatType = scanner.nextLine();
                            System.out.println("Enter service type: ");
                            String serviceType = scanner.nextLine();
                            // Make a new reservation
                            System.out.println(selectedFlight.bookSeat(existingPassenger, seatType, serviceType));
                        } else {
                            System.out.println("Flight not found.");
                        }
                    } else {
                        // If a passenger with the same name is found, cancel their reservation
                        System.out.println("Found the following passengers with the same name as '" + passengerName + "':");
                        for (Passenger passengerForCancel : foundPassengersToCancel) {
                            System.out.println("Passenger: " + passengerForCancel.getName() + " is canceling the reservation...");
                            System.out.println("Please enter the flight number to cancel the reservation: ");
                            flightNumber = scanner.nextLine();
                            selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                            if (selectedFlight != null) {
                                // Execute the cancel reservation operation
                                passengerForCancel.cancelReservation(selectedFlight);
                                System.out.println("The reservation has been successfully cancelled.");
                            } else {
                                System.out.println("Flight not found.");
                            }
                        }
                    }
                    break;

                case 6:
                    // Set flight delay
                    System.out.println("6. Set flight delay");
                    System.out.println("Enter flight number to delay: ");
                    flightNumber = scanner.nextLine();
                    selectedFlight = airlineCompany.getFlightDetails(flightNumber);
                    if (selectedFlight != null) {
                        System.out.println("Enter new departure time (yyyy-MM-dd HH:mm): ");
                        String newDepartureTime = scanner.nextLine();
                        System.out.println("Enter new arrival time (yyyy-MM-dd HH:mm): ");
                        String newArrivalTime = scanner.nextLine();
                        // Set flight delay and directly call the delayFlight method of airlineCompany
                        airlineCompany.delayFlight(flightNumber,
                                LocalDateTime.parse(newDepartureTime, formatter),
                                LocalDateTime.parse(newArrivalTime, formatter));
                        System.out.println("Flight delay updated.");
                        //selectedFlight.setDelay(true);
                        //selectedFlight.setDepartureTime(LocalDateTime.parse(newDepartureTime, formatter));
                        //selectedFlight.setArrivalTime(LocalDateTime.parse(newArrivalTime, formatter));

                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case 7:
                    // View popular routes
                    List<String> popularRoutes = airlineCompany.getPopularRoutes();
                    if (popularRoutes.isEmpty()) {
                        System.out.println("No popular routes available.");
                    } else {
                        System.out.println("Popular routes:");
                        for (String route : popularRoutes) {
                            System.out.println(route);
                        }
                    }
                    break;

                case 8:
                    // View nearly full flights
                    List<Flight> nearlyFullFlights = airlineCompany.getNearlyFullFlights();
                    if (nearlyFullFlights.isEmpty()) {
                        System.out.println("No nearly full flights available.");
                    } else {
                        System.out.println("Nearly full flights:");
                        for (Flight flight : nearlyFullFlights) {
                            System.out.println(flight.toString());
                        }
                    }
                    break;

                case 9:
                    // View passenger reservations
                    System.out.println("9. View passenger reservations");
                    System.out.println("Enter passenger name: ");
                    passengerName = scanner.nextLine();

                    // Find all passengers who match the input name
                    List<Passenger> foundPassengers = new ArrayList<>();
                    for (Passenger p : passengers) {
                        if (p.getName().equalsIgnoreCase(passengerName)) {
                            foundPassengers.add(p); // Add matching passengers to the list
                        }
                    }

                    if (!foundPassengers.isEmpty()) {
                        // If a passenger with the same name is found, display their booking information
                        System.out.println("Here are the reservation details for passengers with the name '" + passengerName + "':");
                        for (Passenger foundPassenger : foundPassengers) {
                            System.out.println("Passenger: " + foundPassenger.getName() + " Reservation Details:");
                            List<Reservation> reservations = foundPassenger.getReservations();
                            if (reservations.isEmpty()) {
                                System.out.println("No reservations found.");
                            } else {
                                for (Reservation reservation : reservations) {
                                    System.out.println(reservation.toString());
                                }
                            }
                        }
                    } else {
                        System.out.println("No passengers found with the name '" + passengerName + "'.");
                    }
                    break;

                case 10:
                    // View all passengers for a flight
                    System.out.println("10. View all passengers for a flight");
                    System.out.println("Enter flight number to view passengers: ");
                    flightNumber = scanner.nextLine();
                    selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                    if (selectedFlight != null) {
                        System.out.println("List of passengers for Flight " + selectedFlight.getFlightNumber() + ":");
                        List<Passenger> flightPassengers = selectedFlight.getPassengers();
                        if (flightPassengers.isEmpty()) {
                            System.out.println("No passengers have booked this flight.");
                        } else {
                            for (Passenger passengerForFlight : flightPassengers) {
                                for (Reservation reservation : passengerForFlight.getReservations()) {
                                    if (reservation.getMyFlight().equals(selectedFlight)) {
                                        System.out.println("Passenger: " + passengerForFlight.getName()
                                                + " | Seat Type: " + reservation.getMySeatType()
                                                + " | Service: " + reservation.getMyService());
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case 11:
                    // Exit program
                    System.out.println("Exiting the program.");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
