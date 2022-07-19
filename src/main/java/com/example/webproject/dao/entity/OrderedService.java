package com.example.webproject.dao.entity;

import java.time.LocalDate;
import java.util.Objects;

public class OrderedService extends BaseEntity{
    private Order order;
    private Service service;
    private LocalDate timeAssigned;
    private LocalDate timeRequested;


    public OrderedService(int id, Order order, Service service, LocalDate timeAssigned, LocalDate timeRequested) {
        super(id);
        this.order = order;
        this.service = service;
        this.timeAssigned = timeAssigned;
        this.timeRequested = timeRequested;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public LocalDate getTimeAssigned() {
        return timeAssigned;
    }

    public void setTimeAssigned(LocalDate timeAssigned) {
        this.timeAssigned = timeAssigned;
    }

    public LocalDate getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(LocalDate timeRequested) {
        this.timeRequested = timeRequested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderedService that = (OrderedService) o;
        return Objects.equals(order, that.order) && Objects.equals(service, that.service) && Objects.equals(timeAssigned, that.timeAssigned) && Objects.equals(timeRequested, that.timeRequested);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), order, service, timeAssigned, timeRequested);
    }

    public static OrderedServiceBuilder getBuilder(){
        return new OrderedServiceBuilder();
    }

    public static class OrderedServiceBuilder{
        private int builtId=UNDEFINED_ID;
        private Order builtOrder;
        private Service builtService;
        private LocalDate builtTimeAssigned;
        private LocalDate builtTimeRequested;

        public OrderedServiceBuilder id(int id){
            builtId=id;
            return this;
        }

        public OrderedServiceBuilder order(Order order){
            builtOrder=order;
            return this;
        }

        public OrderedServiceBuilder service(Service service){
            builtService=service;
            return this;
        }

        public OrderedServiceBuilder timeAssigned(LocalDate timeAssigned){
            builtTimeAssigned=timeAssigned;
            return this;
        }

        public OrderedServiceBuilder timeRequested(LocalDate timeRequested){
            builtTimeRequested=timeRequested;
            return this;
        }

        public OrderedService build(){
            return new OrderedService(builtId,builtOrder,builtService,builtTimeAssigned,builtTimeRequested);
        }
    }
}
