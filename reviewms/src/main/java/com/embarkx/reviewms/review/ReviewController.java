package com.embarkx.reviewms.review;

import com.embarkx.reviewms.review.messaging.ReviewMessageProducer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;
    private ReviewMessageProducer reviewMessageProducer;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId) {
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<String> createReview(@RequestParam Long companyId, @RequestBody Review review) {
        boolean isReviewSaved = reviewService.addReview(companyId, review);
        if (isReviewSaved) {
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>("Review created successfully!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Company not found!", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        if(review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }


    @PutMapping("{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
                                               @RequestBody Review review) {
//        boolean isUpdated = reviewService.updateReview( review.getCompanyId(), reviewId, review);\
        boolean isUpdated = reviewService.updateReview(reviewId, review);
        if(!isUpdated) {
            return new ResponseEntity<>("Review not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Review updated successfully!", HttpStatus.OK);

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        boolean isDeleted = reviewService.deleteReview(reviewId);
        if(isDeleted) {
            return new ResponseEntity<>("Review deleted successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not found!", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/averageRating")
    public Double getAverageRating(@RequestParam Long companyId) {
        List<Review> reviewList = reviewService.getAllReviews(companyId);
        double average = reviewList.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        return Math.round(average * 10.0) / 10.0;
    }


}
