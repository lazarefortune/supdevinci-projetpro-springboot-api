package com.mobilishop.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(name = "product_name_unique",  columnNames = "name")
        }
)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    //@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id", nullable = false)
    //private User author;

    private Double price;

    private String image;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
