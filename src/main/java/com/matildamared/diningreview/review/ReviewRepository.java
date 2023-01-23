package com.matildamared.diningreview.review;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findReviewsByRestaurantIdAndStatus(Long restaurantId, ReviewStatus reviewStatus);

    List<Review> findReviewsByStatus(ReviewStatus reviewStatus);
}
