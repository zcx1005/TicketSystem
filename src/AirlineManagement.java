import java.time.LocalDateTime;
import java.util.List;

public interface AirlineManagement {
    /**
     * 添加航班
     *
     * @param flight 航班对象
     */
    boolean addFlight(Flight flight);

    /**
     * 取消航班
     *
     * @param flightNumber 航班编号
     */
    void cancelFlight(String flightNumber);

    /**
     * 延误航班
     *
     * @param flightNumber     航班编号
     * @param newDepartureTime 新的起飞时间
     * @param newArrivalTime   新的到达时间
     */
    void delayFlight(String flightNumber, LocalDateTime newDepartureTime, LocalDateTime newArrivalTime);

    /**
     * 获取航班详情
     *
     * @param flightNumber 航班编号
     * @return 航班对象
     */
    Flight getFlightDetails(String flightNumber);

    /**
     * 获取所有航班
     *
     * @return 航班列表
     */
    List<Flight> getAllFlights();
}
