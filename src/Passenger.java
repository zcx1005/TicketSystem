import java.util.Iterator;
import java.util.List;

public class Passenger {
    private String name;
    List<Reservation> reservations;

    public Passenger(String name, List<Reservation> reservations) {
        this.name=name;
        this.reservations=reservations;
    }

    public String getName() {
        return name;
    }
    public List<Reservation> getReservations(){ return  reservations; }

    //订票成功后更新预定信息名单
    public void setReservations(Flight flight, String sType, String ser){
        Reservation res=new Reservation(flight,sType,ser);
        reservations.add(res);
    }

    //判断与已预订航班是否冲突
    public boolean isConflict(Flight flight) {
        for(Reservation res:reservations){
            if(res.getMyFlight().conflictsWith(flight)){ return false; }
        }
        return true;
    }

    // 根据航班，取消现有预定
    public void cancelReservation(Flight flight){
        for(Reservation res:reservations){
            if(res.getMyFlight().equals(flight)){
                reservations.remove(res);
                System.out.println("Flight: " + flight.getFlightNumber()
                        + " canceled successfully.");
                flight.update(res.getMySeatType(),this);
                return;
            }
        }
        System.out.println("Flight " + flight.getFlightNumber()
                + " not found in reservations.");
        return;
    }




    //修改预定的航班
    public void modifyReservation(Flight curFlight, String seatType, String service) {
        // 检查座位类型是否有效
        if (!seatType.equalsIgnoreCase("FirstClass") &&!seatType.equalsIgnoreCase("Economy")) {
            System.out.println("Invalid seat type. Please choose 'FirstClass' or 'Economy'.");
            return;
        }

        // 遍历当前乘客的所有预定信息
        for (Reservation res : reservations) {
            if (res.getMyFlight().equals(curFlight)) {
                // 如果座位类型没变化，直接退出
                if (res.getMySeatType().equals(seatType)) {
                    System.out.println("You have already booked this type of seat.");
                    return;
                }

                // 如果预定中有想修改的航班，并且想修改的座位类型有空位
                if ((seatType.equalsIgnoreCase("FirstClass") && curFlight.getFirstClassCapacity() > 0) ||
                        (seatType.equalsIgnoreCase("Economy") && curFlight.getEconomyClassCapacity() > 0)) {

                    // 修改航班中的座位数量，并更新预定信息
                    curFlight.modify(seatType, this);
                    res.setMySeatType(seatType);  // 修改预定信息中的座位类型
                    res.setMyService(service);    // 修改预定信息中的增值服务

                    // 打印成功信息
                    System.out.println("You have successfully modified your reservation.");
                    return;
                } else {
                    System.out.println("There are not enough available seat types to make the modification.");
                    return;
                }
            }
        }

        // 如果没有找到该航班的预定信息
        System.out.println("Flight " + curFlight.getFlightNumber() + " not found in reservations.");
    }


    //加入该航线的vip名单
    public void registerVip(Flight flight){
        flight.getVip().add(this);
    }

    @Override
    public String toString(){
        return name + " " + reservations;
    }
}
