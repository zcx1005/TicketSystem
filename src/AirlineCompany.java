import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AirlineCompany implements AirlineManagement {
    private final String name; // 航空公司名称
    private List<Flight> flights; // 航班列表
    private PassengerNotification passengerNotification; // 通知策略，用于通知乘客航班的变更信息

    // 构造方法，初始化航空公司名称和航班列表
    public AirlineCompany(String name) {
        this.name = name;
        this.flights = new ArrayList<>();
    }

    public String getName() {
        return this.name + " AirlineCompany";
    }


    /**
     * 添加航班
     *
     * @param flight 要添加的航班对象
     */
    @Override
    public boolean addFlight(Flight flight) {
        // 验证航班信息是否完整
        if (flight.getFlightNumber().isEmpty() || flight.getDepartureTime() == null || flight.getArrivalTime() == null ||
                flight.getDeparture() == null || flight.getDestination() == null ||
                flight.getCapacity() <= 0) {
            System.out.println("Incomplete flight information. Please provide all details.");
            return false; // 航班信息不完整，终止操作
        }

        // 将航班添加到航班列表中
        flights.add(flight);
        flight.setOpenForReservation(true); // 设置航班开放预订
        System.out.println("Flight " + flight.getFlightNumber() + " from " + flight.getDeparture() +
                " to " + flight.getDestination() + " has been successfully added.");
        return true;
    }

    /**
     * 取消航班
     *
     * @param flightNumber 要取消的航班编号
     */
    @Override
    public void cancelFlight(String flightNumber) {
        try {
            // 获取目标航班
            Flight targetFlight = getFlightDetails(flightNumber);

            if (targetFlight == null) {
                System.out.println("Flight " + flightNumber + " not found.");
                return;
            }

            // 如果没有乘客预订
            if (targetFlight.getPassengers().isEmpty()) {
                flights.remove(targetFlight); // 从航班列表中移除航班
                System.out.println("No passengers have booked this flight. The flight has been successfully cancelled.");
            } else {
                // 标记航班为已取消
                targetFlight.setStatus("Cancelled");
                System.out.println("Flight " + flightNumber + " has been marked as cancelled.");

                // 通知乘客
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
     * 延误航班
     *
     * @param flightNumber     要延误的航班编号
     * @param newDepartureTime 新的起飞时间
     * @param newArrivalTime   新的到达时间
     */
    @Override
    public void delayFlight(String flightNumber, LocalDateTime newDepartureTime, LocalDateTime newArrivalTime) {
        // 获取目标航班
        Flight targetFlight = getFlightDetails(flightNumber);

        if (targetFlight == null) {
            throw new IllegalArgumentException("Flight " + flightNumber + " not found.");
        }

        // 检查新的起飞时间和到达时间是否晚于当前时间
        if (newDepartureTime.isAfter(targetFlight.getDepartureTime())
                && newArrivalTime.isAfter(targetFlight.getArrivalTime())) {

            // 更新航班时间
            targetFlight.setDepartureTime(newDepartureTime);
            targetFlight.setArrivalTime(newArrivalTime);
            targetFlight.setDelay(true);

            // 输出更新后的航班信息
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            System.out.println("Flight " + flightNumber + " has been delayed. New departure time: "
                    + newDepartureTime.format(formatter) + ", new arrival time: " + newArrivalTime.format(formatter));

            // 检查是否有乘客预定
            if (targetFlight.getPassengers().isEmpty()) {
                System.out.println("No passengers have booked this flight.");
            } else {
                // 使用统一的通知策略发送延误通知
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
     * 获取航班详细信息
     *
     * @param flightNumber 航班编号
     * @return 航班对象，未找到返回 null
     */
    @Override
    public Flight getFlightDetails(String flightNumber) {
        // 遍历航班列表寻找匹配的航班
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight; // 找到后直接返回
            }
        }
        return null; // 未找到返回 null
    }

    /**
     * 获取所有航班列表
     *
     * @return 所有航班的列表
     */
    @Override
    public List<Flight> getAllFlights() {
        return flights; // 返回航班列表
    }

    /**
     * 获取热门航线，根据航班数量返回前几个热门航线
     *
     * @return 热门航线及其航班数量列表
     */
    public List<String> getPopularRoutes() {
        Map<String, Integer> routeCountMap = new HashMap<>();

        // 统计每条航线的航班数量
        for (Flight flight : flights) {
            String route = flight.getDeparture() + " - " + flight.getDestination();
            routeCountMap.put(route, routeCountMap.getOrDefault(route, 0) + 1);
        }

        // 按航班数量降序排序
        List<Map.Entry<String, Integer>> sortedRoutes = new ArrayList<>(routeCountMap.entrySet());
        sortedRoutes.sort((entry1, entry2) -> entry2.getValue() - entry1.getValue());

        // 获取前几个热门航线（调整返回数量可以修改 3）
        List<String> popularRoutes = new ArrayList<>();
        for (int i = 0; i < Math.min(3, sortedRoutes.size()); i++) {
            Map.Entry<String, Integer> entry = sortedRoutes.get(i);
            popularRoutes.add(entry.getKey() + " (" + entry.getValue() + " flights)");
        }

        return popularRoutes;
    }

    /**
     * 查看快满员的航班
     *
     * @return 快满员的航班列表
     */
    public List<Flight> getNearlyFullFlights() {
        List<Flight> nearlyFullFlights = new ArrayList<>();
        List<Flight> allFlights = new ArrayList<>();  // 用来保存所有航班

        // 遍历所有航班，计算预定座位比例
        for (Flight flight : flights) {
            int bookedPassengers = flight.getPassengers().size();
            int capacity = flight.getCapacity();

            // 判断是否超过 90%
            if ((double) bookedPassengers / capacity > 0.9) {
                nearlyFullFlights.add(flight); // 添加到快满员列表
            } else {
                allFlights.add(flight); // 如果座位充足，添加到所有航班列表
            }
        }

        if (nearlyFullFlights.isEmpty()) {
            // 如果没有快满员航班，返回所有航班并提示座位充足
            System.out.println("All flights have sufficient available seats.");
            return allFlights;
        }

        // 返回快满员航班列表
        return nearlyFullFlights;
    }

}
