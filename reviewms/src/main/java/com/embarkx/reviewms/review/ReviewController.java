package com.embarkx.reviewms.review;

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

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId) {
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }

//    @GetMapping("/reviews/{reviewId}")
//    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) {
//        Review review = reviewService.getReviewById(reviewId);
//        if(review == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(review, HttpStatus.OK);
//    }


    @PostMapping
    public ResponseEntity<String> createReview(@RequestParam Long companyId, @RequestBody Review review) {
        boolean IsReviewSaved = reviewService.addReview(companyId, review);
        if (IsReviewSaved) {
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




}
