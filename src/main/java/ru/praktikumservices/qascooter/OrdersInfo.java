package ru.praktikumservices.qascooter;

import java.util.List;

public class OrdersInfo {
    private List<Order> orders;
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    //private Integer pageInfo;
    //private List<AvailableStation> availableStations;
}
