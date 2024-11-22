import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirlineCompanyTest {

    private AirlineCompany airlineCompany;
    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    void setUp() {
        airlineCompany = new AirlineCompany("TestAirline");
        flight1 = new Flight("FL001", "New York", "Los Angeles", "08:00", "11:00", 100, new ArrayList<>());
        flight2 = new Flight("FL002", "San Francisco", "Chicago", "09:00", "13:00", 200, new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        airlineCompany = null;
        flight1 = null;
        flight2 = null;
    }

    @Test
    void getName() {
        String name = airlineCompany.getName();
        assertEquals("TestAirline AirlineCompany", name, "Airline name should match.");
    }

    @Test
    void addFlight() {
        airlineCompany.addFlight(flight1);
        assertTrue(airlineCompany.getAllFlights().contains(flight1), "Flight1 should be added to the list.");
        assertTrue(flight1.getOpenForReservation(), "Flight1 should be open for reservation.");
    }

    @Test
    void cancelFlight() {
        airlineCompany.addFlight(flight1);
        airlineCompany.cancelFlight(flight1.getFlightNumber());
        assertFalse(airlineCompany.getAllFlights().contains(flight1), "Flight1 should be removed from the list.");
    }

    @Test
    void delayFlight() {
        airlineCompany.addFlight(flight1);
        airlineCompany.delayFlight(flight1.getFlightNumber(), "10:00", "13:00");
        Flight updatedFlight = airlineCompany.getFlightDetails(flight1.getFlightNumber());
        assertNotNull(updatedFlight, "Updated flight should not be null.");
        assertEquals("10:00", updatedFlight.getDepartureTime(), "Departure time should be updated.");
        assertEquals("13:00", updatedFlight.getArrivalTime(), "Arrival time should be updated.");
        assertTrue(updatedFlight.isDelay(), "Flight should be marked as delayed.");
    }

    @Test
    void getFlightDetails() {
        airlineCompany.addFlight(flight1);
        Flight retrievedFlight = airlineCompany.getFlightDetails(flight1.getFlightNumber());
        assertNotNull(retrievedFlight, "Retrieved flight should not be null.");
        assertEquals(flight1, retrievedFlight, "Retrieved flight should match the added flight.");
    }

    @Test
    void getAllFlights() {
        airlineCompany.addFlight(flight1);
        airlineCompany.addFlight(flight2);
        List<Flight> flights = airlineCompany.getAllFlights();
        assertEquals(2, flights.size(), "There should be 2 flights in the list.");
        assertTrue(flights.contains(flight1) && flights.contains(flight2), "Flights list should contain both added flights.");
    }

    @Test
    void getPopularRoutes() {
        airlineCompany.addFlight(flight1);
        airlineCompany.addFlight(new Flight("FL003", "New York", "Los Angeles", "12:00", "15:00", 150, new ArrayList<>()));
        List<String> popularRoutes = airlineCompany.getPopularRoutes();
        assertEquals(1, popularRoutes.size(), "There should be 1 popular route.");
        assertTrue(popularRoutes.getFirst().contains("New York-Los Angeles"), "Popular route should match added flights.");
    }

    @Test
    void getNearlyFullFlights() {
        airlineCompany.addFlight(flight1);
        for (int i = 0; i < 90; i++) {
            flight1.getPassengers().add(new Passenger());
        }
        List<Flight> nearlyFullFlights = airlineCompany.getNearlyFullFlights();
        assertEquals(1, nearlyFullFlights.size(), "There should be 1 nearly full flight.");
        assertEquals(flight1, nearlyFullFlights.getFirst(), "Nearly full flight should match flight1.");
    }
}
