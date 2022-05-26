package com.mobilishop.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(name = "category_name_unique",  columnNames = "name")
        }
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "image", nullable = true)
    private String image;

}
