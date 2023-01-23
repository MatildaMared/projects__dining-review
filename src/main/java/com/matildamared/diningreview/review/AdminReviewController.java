package com.matildamared.diningreview.review;

import com.matildamared.diningreview.restaurant.Restaurant;
import com.matildamared.diningreview.restaurant.RestaurantRepository;
import com.matildamared.diningreview.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews/admin")
public class AdminReviewController {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public AdminReviewController(ReviewRepository reviewRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/reviews")
    public List<Review> getReviewsByStatus(@RequestParam ReviewStatus status) {
        return reviewRepository.findReviewsByStatus(status);
    }

    @PutMapping("/reviews/{reviewId}")
    public void performReviewAction(@PathVariable(name = "reviewId") Long id, @RequestBody AdminReviewAction adminReviewAction) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Review review = optionalReview.get();

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(review.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Restaurant restaurant = optionalRestaurant.get();

        if (adminReviewAction.getAccept()) {
            review.setStatus(ReviewStatus.ACCEPTED);
        } else {
            review.setStatus(ReviewStatus.REJECTED);
        }

        reviewRepository.save(review);
        restaurantRepository.save(restaurant);
        this.updateRestaurantScores(restaurant);
    }

    private void updateRestaurantScores(Restaurant restaurant) {
        List<Review> reviews = this.reviewRepository.findReviewsByRestaurantIdAndStatus(restaurant.getId(), ReviewStatus.ACCEPTED);
        if (reviews.size() == 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        int peanutTotalScore = 0;
        int peanutTotalCount = 0;
        int eggTotalScore = 0;
        int eggTotalCount = 0;
        int dairyTotalScore = 0;
        int dairyTotalCount = 0;

        for (Review review : reviews) {
            if (!ObjectUtils.isEmpty(review.getPeanutScore())) {
                peanutTotalScore += review.getPeanutScore();
                peanutTotalCount++;
            }

            if (!ObjectUtils.isEmpty(review.getEggScore())) {
                eggTotalScore += review.getEggScore();
                eggTotalCount++;
            }

            if (!ObjectUtils.isEmpty(review.getDairyScore())) {
                dairyTotalScore += review.getDairyScore();
                dairyTotalCount++;
            }
        }

        int totalScore = peanutTotalScore + eggTotalScore + dairyTotalScore;
        int totalCount = peanutTotalCount + eggTotalCount + dairyTotalCount;

        float overallScore = (float) totalScore / totalCount;
        restaurant.setOverallScore(overallScore);

        if (peanutTotalCount > 0) {
            restaurant.setPeanutScore(peanutTotalScore / peanutTotalCount);
        }

        if (eggTotalCount > 0) {
            restaurant.setEggScore(eggTotalScore / eggTotalCount);
        }

        if (dairyTotalCount > 0) {
            restaurant.setDairyScore(dairyTotalScore / dairyTotalCount);
        }

        restaurantRepository.save(restaurant);
    }
}
