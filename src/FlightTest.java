import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {

    @Test
    void conflictsWith_NoConflict() {
        // 创建两个完全不重叠的航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 200,
                new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "New York", "San Francisco",
                LocalDateTime.of(2024, 11, 24, 15, 0), LocalDateTime.of(2024, 11, 24, 17, 0), 200,
                new ArrayList<>(), new ArrayList<>());

        assertFalse(flight1.conflictsWith(flight2));
    }

    @Test
    void conflictsWith_CompleteConflict() {
        // 创建两个完全重叠的航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 200,
                new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 200,
                new ArrayList<>(), new ArrayList<>());

        assertTrue(flight1.conflictsWith(flight2));
    }

    @Test
    void bookSeat_ReservationClosed() {
        // 模拟航班已满
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 1,
                new ArrayList<>(), new ArrayList<>());

        flight.setOpenForReservation(false); // 设置航班为不可预定
        Passenger passenger = new Passenger("John Doe", new ArrayList<>());

        String result = flight.bookSeat(passenger, "Economy", "Meal");
        assertEquals("Reservation is closed for this flight.", result);
    }

    @Test
    void bookSeat_InvalidSeatType() {
        // 测试无效座位类型
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger passenger = new Passenger("John Doe", new ArrayList<>());
        String result = flight.bookSeat(passenger, "Business", "Meal"); // 无效座位类型
        assertEquals("Invalid seat type. Please choose 'FirstClass' or 'Economy'.", result);
    }

    @Test
    void bookSeat_FullEconomy() {
        // 模拟经济舱座位已满
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 1,
                new ArrayList<>(), new ArrayList<>());

        Passenger passenger = new Passenger("John Doe", new ArrayList<>());
        flight.bookSeat(passenger, "Economy", "Meal");
        String result = flight.bookSeat(new Passenger("Jane Smith", new ArrayList<>()), "Economy", "Meal");
        assertEquals("Reservation is closed for this flight.", result);
    }

    @Test
    void bookSeat_VIPDiscount() {
        // VIP用户预定时测试
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger vipPassenger = new Passenger("VIP John", new ArrayList<>()); // 创建VIP乘客
        flight.bookSeat(vipPassenger, "FirstClass", "Meal");

        String result = flight.bookSeat(new Passenger("Regular Jane", new ArrayList<>()), "Economy", "Meal");
        vipPassenger.registerVip(flight);
        assertEquals("Seat successfully booked for Regular Jane in Economy.", result);
    }

    @Test
    void update_CancelNonVIP() {
        // 非VIP用户取消预定
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger nonVIPPassenger = new Passenger("John Doe", new ArrayList<>());
        flight.bookSeat(nonVIPPassenger, "Economy", "Meal");
        flight.update("Economy", nonVIPPassenger); // 取消预定并处理费用
    }

    @Test
    void modify_ChangeToFirstClass() {
        // 将经济舱更改为头等舱
        Flight flight = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.of(2024, 11, 24, 10, 0), LocalDateTime.of(2024, 11, 24, 14, 0), 200,
                new ArrayList<>(), new ArrayList<>());

        Passenger passenger = new Passenger("John Doe", new ArrayList<>());
        flight.bookSeat(passenger, "Economy", "Meal");
        flight.modify("FirstClass", passenger); // 升级到头等舱
    }
}
