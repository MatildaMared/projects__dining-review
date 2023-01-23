package com.matildamared.diningreview.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "_USER")
@Getter
@Setter
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String address;
    private String city;
    private String postalCode;

    private Boolean interestedInPeanut;
    private Boolean interestedInDairy;
    private Boolean interestedInEgg;
}
