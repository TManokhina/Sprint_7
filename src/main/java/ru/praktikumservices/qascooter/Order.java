package ru.praktikumservices.qascooter;

public class Order {
    String firstName; //Имя заказчика
    String lastName; //фамилия заказчика
    String address; //адрес заказчика
    String metroStation; //Ближайшая к заказчику станция метро
    String phone; //Телефон заказчика
    Number rentTime; //Количество дней аренды
    String deliveryDate; //Дата доставки
    String comment; //Комментарий от заказчика
    String[] color; //Предпочитаемые цвета

    public Order(String firstName, String lastName, String address, String metroStation, String phone, Number rentTime,
                 String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

}
