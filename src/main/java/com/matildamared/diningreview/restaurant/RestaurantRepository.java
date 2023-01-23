package com.matildamared.diningreview.restaurant;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Optional<Restaurant> findRestaurantsByNameAndPostalCode(String name, String postalCode);

    List<Restaurant> findRestaurantsByPostalCodeAndPeanutScoreNotNullOrderByPeanutScore(String zipcode);

    List<Restaurant> findRestaurantsByPostalCodeAndDairyScoreNotNullOrderByDairyScore(String zipcode);

    List<Restaurant> findRestaurantsByPostalCodeAndEggScoreNotNullOrderByEggScore(String zipcode);
}
