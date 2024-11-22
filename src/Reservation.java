public class Reservation {
    /*   private String reservationID; 预定信息是一个用户有多个信息，存在一个列表里
    调用的时候，直接用列表的index调用就行了，这个预定ID多余*/
    /*    private Passenger me;  问题同上，多余 */
    private Flight myFlight;

    private String mySeatType ;
    private String myService;

    //构造函数，初始化的时候可以输入预定的航班，航班的票务类型
    public Reservation(Flight flight, String seatType, String service) {
        this.myFlight = flight;
        this.myService = service;
        this.mySeatType = seatType;
    }

    //获取航班
    public Flight getMyFlight(){
        return  myFlight; }
    //获取该航班票务类型
    public String getMySeatType(){
        return  mySeatType; }
    //获取该航班所选的增值服务
    public String getService(){
        return myService; }

    public void setMySeatType(String newSeatType){
        mySeatType=newSeatType;
    }

    public void setMyService(String newService){
        myService=newService;
    }

    public String getMyService(){
        return myService; }

    @Override
    public String toString() {
        return "Flight: " + myFlight.getFlightNumber() + " | " +
                "Seat Type: " + mySeatType + " | " +
                "Service: " + myService;
    }

}