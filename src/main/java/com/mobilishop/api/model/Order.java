package com.mobilishop.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "order_id_unique", columnNames = "orderNumber")
        }
)
public class Order {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String orderNumber;
    private String orderDate;
    private String orderStatus;
    private String orderTotal;
    private String orderShipping;
    private String orderTax;
    private String orderPayment;
    private String orderShippingAddress;
    private String orderShippingCity;
    private String orderShippingState;
    private String orderShippingCountry;
    private String orderShippingZip;
}
