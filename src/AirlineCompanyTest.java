import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AirlineCompanyTest {

    @Test
    void addFlight() {
        AirlineCompany company = new AirlineCompany("Mamnba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 正常情况
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        assertTrue(company.addFlight(flight1));

        // 异常情况：无效航班号
        Flight flight2 = new Flight("", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> company.addFlight(flight2));
    }

    @Test
    void cancelFlight() {
        AirlineCompany company = new AirlineCompany("Mamnba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 添加航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-11 10:00", formatter), LocalDateTime.parse("2024-11-11 15:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());

        // 正常取消
        company.addFlight(flight1);
        company.cancelFlight("AB123");
        assertNull(company.getFlightDetails("AB123"));

        // 边界情况：取消不存在的航班
        assertDoesNotThrow(() -> company.cancelFlight("CD456"));

        // 异常情况：空航班号
        assertThrows(IllegalArgumentException.class, () -> company.cancelFlight(""));
    }

    @Test
    void delayFlight() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 添加航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);

        // 正常延误
        company.delayFlight("AB123", LocalDateTime.parse("2024-11-24 12:00", formatter), LocalDateTime.parse("2024-11-24 16:00", formatter));
        assertEquals(LocalDateTime.parse("2024-11-24 12:00", formatter), company.getFlightDetails("AB123").getDepartureTime());

        // 边界情况：延误时间早于原时间
        assertThrows(IllegalArgumentException.class, () -> company.delayFlight("AB123",
                LocalDateTime.parse("2024-11-24 08:00", formatter), LocalDateTime.parse("2024-11-24 12:00", formatter)));

        // 异常情况：航班号不存在
        assertThrows(IllegalArgumentException.class, () -> company.delayFlight("CD456",
                LocalDateTime.parse("2024-11-24 12:00", formatter), LocalDateTime.parse("2024-11-24 16:00", formatter)));
    }

    @Test
    void getFlightDetails() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 添加航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);

        // 正常获取航班
        assertNotNull(company.getFlightDetails("AB123"));

        // 异常情况：航班号不存在
        assertNull(company.getFlightDetails("CD456"));
    }

    @Test
    void getAllFlights() {
        AirlineCompany company = new AirlineCompany("Mamba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 添加航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "Beijing", "Shanghai",
                LocalDateTime.parse("2024-11-25 08:00", formatter), LocalDateTime.parse("2024-11-24 11:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);
        company.addFlight(flight2);

        // 获取所有航班
        assertEquals(2, company.getAllFlights().size());
    }

    @Test
    void getPopularRoutes() {
        AirlineCompany company = new AirlineCompany("Mamnba");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 添加航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        Flight flight2 = new Flight("CD456", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        Flight flight3 = new Flight("EF789", "China", "Japan",
                LocalDateTime.parse("2024-11-26 11:30", formatter), LocalDateTime.parse("2024-11-26 14:00", formatter),
                160, new ArrayList<>(), new ArrayList<>());
        company.addFlight(flight1);
        company.addFlight(flight2);
        company.addFlight(flight3);

        // 获取热门航线
        assertEquals("New York - Los Angeles (2 flights)", company.getPopularRoutes().getFirst());
    }

    @Test
    void getNearlyFullFlights() {
        AirlineCompany company = new AirlineCompany("Mamnba");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 添加航班
        Flight flight1 = new Flight("AB123", "New York", "Los Angeles",
                LocalDateTime.parse("2024-11-24 10:00", formatter), LocalDateTime.parse("2024-11-24 14:00", formatter),
                2, new ArrayList<>(), new ArrayList<>());
        //新旅客
        Passenger passenger1 = new Passenger("John Doe", new ArrayList<>());
        Passenger passenger2 = new Passenger("Ben Machiel", new ArrayList<>());
        flight1.bookSeat(passenger1, "FirstClass", "");
        flight1.bookSeat(passenger1, "SecondClass", "");
        company.addFlight(flight1);

        // 获取接近满员的航班
        assertEquals(1, company.getNearlyFullFlights().size());
        assertEquals("AB123", company.getNearlyFullFlights().getFirst().getFlightNumber());
    }

}