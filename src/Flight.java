import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.LocalDateTime;

public class Flight {
    private String flightNumber;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private boolean isDelay;
    private int capacity; // 总容量
    private int firstClassCapacity; // 头等舱容量
    private int economyClassCapacity; // 经济舱容量
    private List<Passenger> passengers;
    private boolean isOpenForReservation;
    private List<Passenger> vip;

    // 构造方法
    public Flight(String flightNumber, String departure, String destination,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, int capacity,
                  List<Passenger> passengers,List<Passenger> vip) {
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
        this.vip = vip;
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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setDelay(boolean isDelay) {
        this.isDelay = isDelay;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFirstClassCapacity() {
        return firstClassCapacity;
    }

    public void setFirstClassCapacity(int firstClassCapacity) {
        this.firstClassCapacity = firstClassCapacity;
    }

    public int getEconomyClassCapacity() {
        return economyClassCapacity;
    }

    public void setEconomyClassCapacity(int economyClassCapacity) {
        this.economyClassCapacity = economyClassCapacity;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public List<Passenger> getVip() {
        return vip;
    }

    // Setters
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setOpenForReservation(boolean openForReservation) {
        this.isOpenForReservation = openForReservation;
    }

    // Method to check if the reservations should be closed
    private void checkReservationStatus() {
        if (passengers.size() >= capacity) {
            this.isOpenForReservation = false; // Close reservations if capacity is full
        }
    }


    @Override
    public String toString() {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return flightNumber + " | " +
                departure + " -> " + destination + " | " +
                departureTime.format(formatter) + " ~ " + arrivalTime.format(formatter) + " | " +
                "Delay: " + (isDelay ? "Yes" : "No") + " | " +
                "Capacity: " + capacity + " | " +
                "First: " + getFirstClassCapacity() +" · "+ "Economy: " + getEconomyClassCapacity() + " | " +
                "Passengers: " + (passengers.isEmpty() ? 0 : passengers.size()) + " | " +
                "Open for Reservation: " + (isOpenForReservation ? "Yes" : "No");
    }


    //判断航班是否冲突
    public boolean conflictsWith(Flight other) {
        return !(this.arrivalTime.isBefore(other.getDepartureTime()) ||
                this.departureTime.isAfter(other.getArrivalTime()));
    }

    public String bookSeat(Passenger passenger, String seatType, String service) {
        // 检查航班是否开放预定
        if (!isOpenForReservation) {
            return "Reservation is closed for this flight.";
        }

        // 检查座位类型是否合法
        if (!seatType.equalsIgnoreCase("FirstClass") && !seatType.equalsIgnoreCase("Economy")) {
            return "Invalid seat type. Please choose 'FirstClass' or 'Economy'.";
        }

        //检查预定与现有预定是否冲突
        if(!passenger.isConflict(this)){
            return "Conflict detected: Cannot book flight " + flightNumber;
        }

        // 根据座位类型检查容量
        if (seatType.equalsIgnoreCase("FirstClass")) {
            if (firstClassCapacity > 0) {
                firstClassCapacity--; // 减少头等舱容量
            } else {
                return "No remaining seats in FirstClass.";
            }
        } else if (seatType.equalsIgnoreCase("Economy")) {
            if (economyClassCapacity > 0) {
                economyClassCapacity--; // 减少经济舱容量
            } else {
                return "No remaining seats in Economy.";
            }
        }

        // 添加乘客到列表
        passengers.add(passenger);

        //更新乘客自己的预定名单
        passenger.setReservations(this,seatType,service);

        // 检查是否需要关闭预定
        checkReservationStatus();

        if(vip.contains(passenger)){
            return "Seat successfully booked for " + passenger.getName() + " in " + seatType + " and "
                    +"you can enjoy a 15% discount on the ticket price.";
        }else{
            return "Seat successfully booked for " + passenger.getName() + " in " + seatType + ".";
        }
    }

    //用户取消预定后，修改对应座位数,vip用户免除此次费用
    public void update(String seatType,Passenger passenger){
        if(vip.contains(passenger)){
            System.out.println("You are a VIP, so we will waive the service fee for you this time");
        }
        if(seatType.equalsIgnoreCase("FirstClass")){
            firstClassCapacity++;
            System.out.println("You need to pay an additional 15% of the first-class ticket price as a handling fee");
        }else{
            economyClassCapacity++;
            System.out.println("You need to pay an additional 10% of the economy-class ticket price as a handling fee");
        }
        // 从乘客列表中删除该乘客
        if (passengers.contains(passenger)) {
            passengers.remove(passenger);
            System.out.println("Passenger " + passenger.getName() + " has been removed from the flight.");
        } else {
            System.out.println("Passenger not found in the flight.");
        }
    }

    //用户修改预定后
    public void modify(String newSeatType, Passenger passenger){
        if(newSeatType.equalsIgnoreCase("FirstClass")){
            economyClassCapacity++;
            firstClassCapacity--;
            System.out.println("Please pay the upgrade fee.");
        }else{
            firstClassCapacity++;
            economyClassCapacity--;
            System.out.println("The fare difference will be refunded to your account.");
        }
        if(vip.contains(passenger)){
            System.out.println("You are a VIP, so we will waive the service fee for you this time");
        }else{
            System.out.println("You need to pay a 5% service fee on the ticket price.");
        }

    }

}
