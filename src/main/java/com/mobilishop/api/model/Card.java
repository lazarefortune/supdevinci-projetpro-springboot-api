package com.mobilishop.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
    name = "cards",
    uniqueConstraints = {
      @UniqueConstraint(name = "card_number_unique", columnNames = "cardNumber")
    })
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long Id;

  @Column(nullable = false, unique = true)
  private String cardNumber;

  @Column(nullable = false)
  private String cardHolderName;

  @Column(nullable = false)
  private String expirationDate;

  @Column(nullable = false)
  private String cvv;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
