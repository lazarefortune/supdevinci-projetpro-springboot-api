package com.mobilishop.api.model;

import com.mobilishop.api.enums.AppUserRole;
import com.mobilishop.api.enums.Gender;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Data
@Entity(name = "user")
@Table(
    name = "users",
    uniqueConstraints = {
      @UniqueConstraint(name = "user_email_unique", columnNames = "email"),
      @UniqueConstraint(name = "user_username_unique", columnNames = "username")
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  /*
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "user_id_seq"
  )
  @Column(
          name = "id",
          unique = true,
          nullable = false,
          updatable = false
  )
  private Long id;
  */

  private String firstName;

  private String lastName;

  private LocalDateTime birthday;

  private String phoneNumber;

  private Gender gender;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Boolean locked = false;

  @Column(nullable = false)
  private Boolean enabled = false;

  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @ManyToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Collection<Role> roles;

  private LocalDateTime createdAt = LocalDateTime.now();

  private LocalDateTime updatedAt = LocalDateTime.now();

  @Embedded private Address address = new Address();

  // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  // private Set<Product> products;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Order> orders;
  /*
      @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
      private Set<Comment> comments;
  */
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<Card> cards;

  public boolean isAdmin() {
    return roles.stream().anyMatch(role -> role.getName().equals(AppUserRole.ROLE_ADMIN));
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean isLocked() {
    return locked;
  }
}
