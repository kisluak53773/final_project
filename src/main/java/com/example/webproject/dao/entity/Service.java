package com.example.webproject.dao.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Service extends BaseEntity{
    private BigDecimal price;
    private String serviceName;
    private String serviceDescription;
    private String serviceImage;

    public Service(int id, BigDecimal price, String serviceName, String serviceDescription, String serviceImage) {
        super(id);
        this.price = price;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceImage = serviceImage;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Service service = (Service) o;
        return Objects.equals(price, service.price) && Objects.equals(serviceName, service.serviceName) && Objects.equals(serviceDescription, service.serviceDescription) && Objects.equals(serviceImage, service.serviceImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), price, serviceName, serviceDescription, serviceImage);
    }

    public static ServiceBuilder getBuilder(){
        return new ServiceBuilder();
    }

    public static class ServiceBuilder {
        private int builtId = UNDEFINED_ID;
        private BigDecimal builtPrice;
        private String builtServiceName;
        private String builtServiceDescription;
        private String builtServiceImage;

        public ServiceBuilder id(int id) {
            builtId = id;
            return this;
        }

        public ServiceBuilder price(BigDecimal price) {
            builtPrice = price;
            return this;
        }

        public ServiceBuilder serviceName(String serviceName) {
            builtServiceName = serviceName;
            return this;
        }

        public ServiceBuilder serviceDescription(String serviceDescription) {
            builtServiceDescription = serviceDescription;
            return this;
        }

        public ServiceBuilder serviceImage(String image) {
            builtServiceImage = image;
            return this;
        }

        public Service build() {
            return new Service(builtId, builtPrice, builtServiceName, builtServiceDescription, builtServiceImage);
        }
    }
}
