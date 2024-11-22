import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class FlightTest {

    private Flight flight;

    @BeforeEach
    void setUp() {
        List<Passenger> passengers = new ArrayList<>();
        flight = new Flight("CA1234", "Beijing", "Shanghai", "10:00", "12:30", 50, passengers);
    }

    @Test
    void getFlightNumber() {
        assertEquals("CA1234", flight.getFlightNumber());
    }

    @Test
    void getDeparture() {
        assertEquals("Beijing", flight.getDeparture());
    }

    @Test
    void getDestination() {
        assertEquals("Shanghai", flight.getDestination());
    }

    @Test
    void getDepartureTime() {
        assertEquals("10:00", flight.getDepartureTime());
    }

    @Test
    void getArrivalTime() {
        assertEquals("12:30", flight.getArrivalTime());
    }

    @Test
    void setDelay() {
        flight.setDelay(true);
        assertTrue(flight.isDelay());
    }

    @Test
    void getCapacity() {
        assertEquals(50, flight.getCapacity());
    }

    @Test
    void getFirstClassCapacity() {
        assertEquals(5, flight.getFirstClassCapacity()); // 10% of 50
    }

    @Test
    void getEconomyClassCapacity() {
        assertEquals(45, flight.getEconomyClassCapacity());
    }

    @Test
    void getPassengers() {
        assertTrue(flight.getPassengers().isEmpty());
    }

    @Test
    void setDepartureTime() {
        flight.setDepartureTime("11:00");
        assertEquals("11:00", flight.getDepartureTime());
    }

    @Test
    void setArrivalTime() {
        flight.setArrivalTime("13:00");
        assertEquals("13:00", flight.getArrivalTime());
    }

    @Test
    void setOpenForReservation() {
        flight.setOpenForReservation(true);
        assertTrue(flight.getOpenForReservation());
    }

    @Test
    void testToString() {
        String expected = "CA1234 | From Beijing to Shanghai | 10:00-12:30 | Delay: No | Capacity: 50 | " +
                "First: 5·Economy: 45 | Passengers: 0 | Open for Reservation: No";
        assertEquals(expected, flight.toString());
    }
}
