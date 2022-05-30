package com.mobilishop.api.model;

import lombok.Data;

import javax.persistence.*;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@Table(
    name = "orders_items",
    uniqueConstraints = {
        @UniqueConstraint(name = "order_item_id_unique", columnNames = "id")
    }
)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.EAGER)
    private Product product;

    private Integer quantity;
}
