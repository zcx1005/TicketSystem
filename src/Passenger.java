import java.util.List;

public class Passenger {
    private String id;
    private String name;
    List<Reservation> reservations;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
