package com.mobilishop.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(
        name = "cards",
        uniqueConstraints = {
                @UniqueConstraint(name = "card_number_unique", columnNames = "cardNumber")
        }
)
public class Card {
    @Id
    private Long Id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private String expiryDate;

    @Column(nullable = false)
    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingZip;
}
