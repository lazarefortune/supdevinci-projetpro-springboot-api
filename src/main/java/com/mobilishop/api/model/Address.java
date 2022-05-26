package com.mobilishop.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String country;
    private String city;
    private Integer postalCode;
    private String street;
    private Integer apartmentNumber;
}
