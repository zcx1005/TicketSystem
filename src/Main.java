import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AirlineCompany airlineCompany = new AirlineCompany("Mamba");
        System.out.println("Welcome to "+ airlineCompany.getName());

        // 创建一个自定义的 DateTimeFormatter 来解析日期时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //创建新航班
        Flight flight1 = new Flight("C919","China",
                "New York", LocalDateTime.parse("2024-11-11 10:00", formatter),LocalDateTime.parse("2024-11-11 15:00",formatter),160,new ArrayList<>(),new ArrayList<>());
        Flight flight2 = new Flight("C999","Beijing",
                "Guangzhou",LocalDateTime.parse("2024-11-11 08:00", formatter),LocalDateTime.parse("2024-11-11 10:30",formatter),140,new ArrayList<>(),new ArrayList<>());
        //添加新航班
        airlineCompany.addFlight(flight1);
        airlineCompany.addFlight(flight2);

        System.out.println();
        //显示所有航班状态
        List<Flight> allFlights =  airlineCompany.getAllFlights();
        //打印当前所有航班列表
        if (allFlights.isEmpty()) {
            System.out.println("No flights available.");
        } else {
            System.out.println("All flights in the airline:");
            for (Flight flight : allFlights) {
                System.out.println(flight.toString()); // Assuming toString() method is implemented in Flight class
            }
        }

        System.out.println();
        //选择航班单独展示
        Flight getF1 = airlineCompany.getFlightDetails("C919");
        //Flight getF2 = airlineCompany.getFlightDetails("C918");
        System.out.println(getF1.toString());

        System.out.println();
        //设置航班延误
        airlineCompany.delayFlight("C919",LocalDateTime.parse("2024-11-11 10:00", formatter),LocalDateTime.parse("2024-11-11 10:00", formatter));
        System.out.println(getF1);

        System.out.println();

        //airlineCompany.cancelFlight("C919");

        //新旅客
        Passenger passenger1 = new Passenger("zz",new ArrayList<>());

        //乘客预定座位类型
        System.out.println(flight1.bookSeat(passenger1,"FirstClass","ser1"));
        System.out.println();
        //打印旅客的预定航班表
        List<Reservation> p1res = passenger1.getReservations();
        for (Reservation reservation : p1res) {
            System.out.println(reservation.toString());
        }

        System.out.println();
        //修改航班信息
        passenger1.modifyReservation(flight1,"Economy","ser2");

        System.out.println();

        // 获取航班的所有乘客
        List<Passenger> flight1Passengers = flight1.getPassengers();
        System.out.println(getF1);
        // 检查航班是否有乘客
        if (flight1Passengers.isEmpty()) {
            System.out.println("No passengers have booked this flight.");
        } else {
            // 遍历每个乘客并打印相关信息
            System.out.println("List of passengers for Flight " + flight1.getFlightNumber() + ":");
            for (Passenger passenger : flight1Passengers) {
                // 遍历乘客的预定信息
                for (Reservation reservation : passenger.getReservations()) {
                    if (reservation.getMyFlight().equals(flight1)) {
                        // 打印座位类型和增值服务
                        System.out.println("Passenger Name: " + passenger.getName() + " | " +
                                "Seat Type: " + reservation.getMySeatType() + " | " +
                                "Service: " + reservation.getMyService());
                    }
                }
            }
        }

        System.out.println();

        //取消航班
        passenger1.cancelReservation(flight1);
        System.out.println(getF1);


        System.out.println();
        //打印热门航线
        List<String> popRoute =  airlineCompany.getPopularRoutes();
        for (String route : popRoute) {
            System.out.println(route);
        }
        System.out.println();
        //打印快满员的航班
        List<Flight> nearlyFull =  airlineCompany.getNearlyFullFlights();

    }
}