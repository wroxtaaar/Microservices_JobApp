package com.embarkx.reviewms.review.impl;

import com.embarkx.reviewms.review.Review;
import com.embarkx.reviewms.review.ReviewRepository;
import com.embarkx.reviewms.review.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if (companyId != null && review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        } else {
            throw new IllegalArgumentException("Company with ID " + companyId + " does not exist.");
        }
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

//#####--(Method 1)--#########
//    @Override
//    public boolean updateReview(Long companyId, Long reviewId, Review review) {
//
//        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
//        Review reviewToUpdate = reviews.stream()
//                .filter(r -> r.getId().equals(reviewId))
//                .findFirst()
//                .orElse(null);
//
//        if (reviewToUpdate != null) {
//            reviewToUpdate.setTitle(review.getTitle());
//            reviewToUpdate.setDescription(review.getDescription());
//            reviewToUpdate.setRating(review.getRating());
//            reviewRepository.save(reviewToUpdate);
//            return true;
//        }
//        return false;
//    }



//#####--(Method 2)--#########
//This approach has an issue, it is transferring the review from one company to another
    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null) {
            review.setTitle(updatedReview.getTitle());
            review.setDescription(updatedReview.getDescription());
            review.setRating(updatedReview.getRating());
            review.setCompanyId(updatedReview.getCompanyId());
            reviewRepository.save(review);
            reviewRepository.save(updatedReview);
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null) {
            reviewRepository.deleteById(reviewId);
            return true;
        }        // If the company with the given ID does not exist, return false
        return false;
    }


}
