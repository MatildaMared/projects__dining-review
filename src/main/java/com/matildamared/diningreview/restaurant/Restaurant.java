package com.matildamared.diningreview.restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private String city;
    private String postalCode;
    private String phoneNumber;
    private String website;
    private float overallScore;
    private int peanutScore;
    private int dairyScore;
    private int eggScore;
}
