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

    public String  getName() {
        return this.name + " AirlineCompany";
    }


    /**
     * 添加航班
     * @param flight 要添加的航班对象
     */
    @Override
    public void addFlight(Flight flight) {
        // 验证航班信息是否完整
        if (flight.getDepartureTime() == null || flight.getArrivalTime() == null ||
                flight.getDeparture() == null || flight.getDestination() == null ||
                flight.getCapacity() <= 0) {
            System.out.println("Incomplete flight information. Please provide all details.");
            return; // 航班信息不完整，终止操作
        }

        // 将航班添加到航班列表中
        flights.add(flight);
        flight.setOpenForReservation(true); // 设置航班开放预订
        System.out.println("Flight " + flight.getFlightNumber() + " from " + flight.getDeparture() +
                " to " + flight.getDestination() + " has been successfully added.");
    }

    /**
     * 取消航班
     * @param flightNumber 要取消的航班编号
     */
    @Override
    public void cancelFlight(String flightNumber) {
        // 获取目标航班
        Flight targetFlight = getFlightDetails(flightNumber);

        if (targetFlight != null) {
            // 检查是否有乘客预定此航班
            if (targetFlight.getPassengers().isEmpty()) {
                System.out.println("No passengers have booked this flight. The flight has been successfully cancelled.");
            } else {
                // 移除航班并通知乘客
                flights.remove(targetFlight);
                System.out.println("Flight " + flightNumber + " has been cancelled.");

                passengerNotification = new CancelNotificationStrategy(flights); // 使用取消策略
                passengerNotification.sendNotification(flightNumber,
                        "We apologize for the inconvenience. Please contact our customer service for assistance.");

                System.out.println("All passengers who have booked this flight have been notified about the cancellation.");
            }
        } else {
            System.out.println("Flight " + flightNumber + " not found."); // 未找到航班
        }
    }


    /**
     * 延误航班
     * @param flightNumber 要延误的航班编号
     * @param newDepartureTime 新的起飞时间
     * @param newArrivalTime 新的到达时间
     */
    @Override
    public void delayFlight(String flightNumber, String newDepartureTime, String newArrivalTime) {
        // 获取目标航班
        Flight targetFlight = getFlightDetails(flightNumber);

        if (targetFlight != null) {
            // 更新航班时间并通知乘客
            targetFlight.setDepartureTime(newDepartureTime);
            targetFlight.setArrivalTime(newArrivalTime);
            targetFlight.setDelay(true);
            System.out.println("Flight " + flightNumber + " has been delayed. New departure time: "
                    + newDepartureTime + ", new arrival time: " + newArrivalTime);

            // 检查是否有乘客预定
            if (targetFlight.getPassengers().isEmpty()) {
                System.out.println("No passengers have booked this flight.");
            } else {
                // 使用延误通知策略通知乘客
                passengerNotification = new DelayNotificationStrategy(flights); // 使用延误通知策略
                passengerNotification.sendNotification(flightNumber, "The flight has been delayed. New departure time: "
                        + newDepartureTime + ", new arrival time: " + newArrivalTime);

                System.out.println("All passengers who have booked this flight have been notified about the delay.");
            }

        } else {
            System.out.println("Flight " + flightNumber + " not found."); // 未找到航班
        }
    }


    /**
     * 获取航班详细信息
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
     * @return 所有航班的列表
     */
    @Override
    public List<Flight> getAllFlights() {
        return flights; // 返回航班列表
    }

    /**
     * 获取热门航线，根据航班数量返回前几个热门航线
     * @return 热门航线及其航班数量列表
     */
    public List<String> getPopularRoutes() {
        Map<String, Integer> routeCountMap = new HashMap<>();

        // 统计每条航线的航班数量
        for (Flight flight : flights) {
            String route = flight.getDeparture() + "-" + flight.getDestination();
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
     * @return 快满员的航班列表
     */
    public List<Flight> getNearlyFullFlights() {
        List<Flight> nearlyFullFlights = new ArrayList<>();

        // 遍历所有航班，计算预定座位比例
        for (Flight flight : flights) {
            int bookedPassengers = flight.getPassengers().size();
            int capacity = flight.getCapacity();

            // 判断是否超过 90%
            if ((double) bookedPassengers / capacity > 0.9) {
                nearlyFullFlights.add(flight); // 添加到快满员列表
            }
        }

        return nearlyFullFlights;
    }
}
