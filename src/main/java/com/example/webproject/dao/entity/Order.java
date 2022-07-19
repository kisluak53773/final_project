package com.example.webproject.dao.entity;

import java.util.Objects;

public class Order extends BaseEntity{
    private User user;
    private OrderState orderState;

    public Order(int id, User user, OrderState orderState) {
        super(id);
        this.user = user;
        this.orderState = orderState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Order order = (Order) o;
        return Objects.equals(user, order.user) && orderState == order.orderState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, orderState);
    }

    public enum OrderState{
        REQUESTED,
        APPROVED,
        COMPLETED,
        CANCELED
    }

    public static OrderBuilder getBuilder(){
        return new OrderBuilder();
    }

    public static class OrderBuilder{
        private int builtId=UNDEFINED_ID;
        private User builtUser;
        private OrderState builtOrderState;

        public OrderBuilder id(int id){
            builtId=id;
            return this;
        }

        public OrderBuilder user(User user){
            builtUser=user;
            return this;
        }

        public OrderBuilder orderState(OrderState orderState){
            builtOrderState=orderState;
            return this;
        }

        public Order build(){
            return new Order(builtId,builtUser,builtOrderState);
        }
    }
}
