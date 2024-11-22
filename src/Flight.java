import java.util.List;

public class Flight {
    private String flightNumber;
    private String departure;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private boolean isDelay;
    private int capacity; // 总容量
    private int firstClassCapacity; // 头等舱容量
    private int economyClassCapacity; // 经济舱容量
    private List<Passenger> passengers;
    private boolean isOpenForReservation;

    // 构造方法
    public Flight(String flightNumber, String departure, String destination,
                  String departureTime, String arrivalTime, int capacity,
                  List<Passenger> passengers) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.isDelay = false;
        this.capacity = capacity;
        this.firstClassCapacity = (int) (capacity * 0.1); // 头等舱容量为总容量的20%
        this.economyClassCapacity = capacity - firstClassCapacity; // 剩余为经济舱容量
        this.isOpenForReservation = false;
        this.passengers = passengers;
        checkReservationStatus(); // Check if reservation should be closed
    }

    // Getters
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setDelay(boolean isDelay) {
        this.isDelay = isDelay;
    }

    public boolean isDelay() {
        return isDelay;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFirstClassCapacity() {
        return firstClassCapacity;
    }

    public int getEconomyClassCapacity() {
        return economyClassCapacity;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }


    // Setters
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setOpenForReservation(boolean openForReservation) {
        this.isOpenForReservation = openForReservation;
    }

    public boolean getOpenForReservation() {
        return isOpenForReservation;
    }

    // Method to check if the reservations should be closed
    private void checkReservationStatus() {
        if (passengers.size() >= capacity) {
            this.isOpenForReservation = false; // Close reservations if capacity is full
        }
    }


    @Override
    public String toString() {
        return flightNumber + " | " +
                "From " + departure + " to " + destination + " | " +
                departureTime + "-" + arrivalTime + " | " +
                "Delay: " + (isDelay ? "Yes" : "No") + " | " +
                "Capacity: " + capacity + " | " +
                "First: " + getFirstClassCapacity() +"·"+ "Economy: " + getEconomyClassCapacity() + " | " +
                "Passengers: " + (passengers.isEmpty() ? 0 : passengers.size()) + " | " +
                "Open for Reservation: " + (isOpenForReservation ? "Yes" : "No");
    }

}
