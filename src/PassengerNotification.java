public interface PassengerNotification {
    /**
     * 通知所有预订特定航班的乘客
     *
     * @param flightNumber 航班编号
     * @param message      通知消息
     */
    void sendNotification(String flightNumber, String message, String type);
}
