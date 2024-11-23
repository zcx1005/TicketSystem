import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AirlineCompany airlineCompany = new AirlineCompany("Mamba");
        System.out.println("Welcome to " + airlineCompany.getName());

        // 创建一个自定义的 DateTimeFormatter 来解析日期时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //创建新航班
        Flight flight1 = new Flight("C919", "China",
                "New York", LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter), 160, new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("C999", "Beijing",
                "Guangzhou", LocalDateTime.parse("2024-11-11 08:00", formatter), LocalDateTime.parse("2024-11-11 10:30", formatter), 140, new ArrayList<>(), new ArrayList<>());
        //添加新航班
        airlineCompany.addFlight(flight1);
        airlineCompany.addFlight(flight2);
        Flight selectedFlight = null; // 外部声明变量
        Passenger passenger = null;   // 外部声明变量
        List<Passenger> passengers = new ArrayList<>(); // 存储所有创建的乘客实例
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // 打印菜单
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

            // 获取用户选择
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

                case 3://无论预定成功与否都会创建！，就是在passenger中。不一定放到fight中的passenger列表中。
                    // Book a seat
                    System.out.println("3.Create a passenger and book a seat");
                    System.out.println("Enter passenger name: ");
                    String passengerName = scanner.nextLine();
                    passenger = new Passenger(passengerName, new ArrayList<>());

                    passengers.add(passenger);  // 将新的乘客对象添加到列表中

                    System.out.println("Enter flight number to book: ");
                    flightNumber = scanner.nextLine();
                    selectedFlight = airlineCompany.getFlightDetails(flightNumber);
                    if (selectedFlight != null) {
                        System.out.println("Enter seat type (FirstClass/Economy): ");
                        String seatType = scanner.nextLine();
                        System.out.println("Enter service type: ");
                        String serviceType = scanner.nextLine();
                        System.out.println(selectedFlight.bookSeat(passenger, seatType, serviceType));//如果不是Economy和FirstClass，会提示插入不进去
                    } else {
                        System.out.println("Flight not found.");
                    }
                    break;

                case 4:
                    // Modify a reservation and if a passenger is not found, create one
                    System.out.println("4. Modify a reservation and if a passenger is not found, create one and book a seat ");
                    System.out.println("Enter passenger name: ");
                    passengerName = scanner.nextLine();

                    // 查找已有的乘客，如果没有找到则提示并创建新乘客
                    Passenger existingPassenger = null;
                    for (Passenger p : passengers) {
                        if (p.getName().equalsIgnoreCase(passengerName)) {
                            existingPassenger = p;
                            break;
                        }
                    }

                    if (existingPassenger == null) {
                        System.out.println("Passenger not found. Creating a new passenger.");
                        // 如果没有找到该乘客，则创建新乘客实例并进行预定
                        existingPassenger = new Passenger(passengerName, new ArrayList<>());
                        passengers.add(existingPassenger);  // 将新乘客添加到列表中

                        // 进行新的航班预定
                        System.out.println("Enter flight number to book: ");
                        flightNumber = scanner.nextLine();
                        selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                        if (selectedFlight != null) {
                            System.out.println("Enter seat type (FirstClass/Economy): ");
                            String seatType = scanner.nextLine();
                            System.out.println("Enter service type: ");
                            String serviceType = scanner.nextLine();
                            // 进行新的预定
                            System.out.println(selectedFlight.bookSeat(existingPassenger, seatType, serviceType));
                        } else {
                            System.out.println("Flight not found.");
                        }
                    } else {
                        // 如果找到了已有的乘客，则修改预定
                        System.out.println("Passenger found. Modifying reservation.");
                        System.out.println("Enter flight number to modify reservation: ");
                        flightNumber = scanner.nextLine();
                        selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                        if (selectedFlight != null) {
                            System.out.println("Enter new seat type (FirstClass/Economy): ");
                            String newSeatType = scanner.nextLine();
                            System.out.println("Enter new service type: ");
                            String newServiceType = scanner.nextLine();
                            // 修改预定
                            existingPassenger.modifyReservation(selectedFlight, newSeatType, newServiceType);
                        } else {
                            System.out.println("Flight not found.");
                        }
                    }
                    break;

                case 5:
                    // Cancel a reservation, if the passenger is not found, create one
                    System.out.println("5. Cancel a reservation and if a passenger is not found, create one and book a seat");
                    System.out.println("Enter passenger name: ");
                    passengerName = scanner.nextLine();

                    // 查找已有的乘客，如果没有找到则提示并创建新乘客
                    existingPassenger = null;
                    for (Passenger p : passengers) {
                        if (p.getName().equalsIgnoreCase(passengerName)) {
                            existingPassenger = p;
                            break;
                        }
                    }

                    if (existingPassenger == null) {
                        System.out.println("Passenger not found. Creating a new passenger.");
                        // 如果没有找到该乘客，则创建新乘客实例并进行预定
                        existingPassenger = new Passenger(passengerName, new ArrayList<>());
                        passengers.add(existingPassenger);  // 将新乘客添加到列表中

                        // 进行新的航班预定
                        System.out.println("Enter flight number to book: ");
                        flightNumber = scanner.nextLine();
                        selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                        if (selectedFlight != null) {
                            System.out.println("Enter seat type (FirstClass/Economy): ");
                            String seatType = scanner.nextLine();
                            System.out.println("Enter service type: ");
                            String serviceType = scanner.nextLine();
                            // 进行新的预定
                            System.out.println(selectedFlight.bookSeat(existingPassenger, seatType, serviceType));
                        } else {
                            System.out.println("Flight not found.");
                        }
                    } else {
                        // 如果找到了已有的乘客，则取消预定
                        System.out.println("Passenger found. Cancelling reservation.");
                        System.out.println("Enter flight number to cancel reservation: ");
                        flightNumber = scanner.nextLine();
                        selectedFlight = airlineCompany.getFlightDetails(flightNumber);

                        if (selectedFlight != null) {
                            // 执行取消预定
                            existingPassenger.cancelReservation(selectedFlight);
                            System.out.println("Reservation cancelled successfully.");
                        } else {
                            System.out.println("Flight not found.");
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
                        System.out.println("Enter new departure time (yyyy-MM-dd HH:mm): ");//这边异常处理！！！此外两个不能是同一个时间，
                        String newDepartureTime = scanner.nextLine();
                        System.out.println("Enter new arrival time (yyyy-MM-dd HH:mm): ");
                        String newArrivalTime = scanner.nextLine();
                        // 设置航班延误，直接调用 airlineCompany 的 delayFlight 方法
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
                    existingPassenger = null;
                    for (Passenger p : passengers) {
                        if (p.getName().equalsIgnoreCase(passengerName)) {
                            existingPassenger = p;
                            break;
                        }
                    }
                    if (existingPassenger != null) {
                        System.out.println("Passenger " + existingPassenger.getName() + "'s Reservations:");
                        List<Reservation> reservations = existingPassenger.getReservations();
                        if (reservations.isEmpty()) {
                            System.out.println("No reservations found.");
                        } else {
                            for (Reservation reservation : reservations) {
                                System.out.println(reservation.toString());
                            }
                        }
                    } else {
                        System.out.println("Passenger not found.");
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
