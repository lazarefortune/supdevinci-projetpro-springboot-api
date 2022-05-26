package com.mobilishop.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "role_name_unique", columnNames = "name")
        }
)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true , nullable = false)
    private String name;

    private String description;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
