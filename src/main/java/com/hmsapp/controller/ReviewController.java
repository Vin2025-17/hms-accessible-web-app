package com.hmsapp.controller;

import com.hmsapp.entity.Property;
import com.hmsapp.entity.Reviews;
import com.hmsapp.entity.User;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.ReviewsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private PropertyRepository properties;
    private ReviewsRepository  reviewsRepository;
    public ReviewController(PropertyRepository properties, ReviewsRepository reviewsRepository) {
        this.properties = properties;
        this.reviewsRepository = reviewsRepository;
    }

    @PostMapping("/addReview")
    public String addReview(@RequestBody  Reviews reviews, @RequestParam long propertyid
    ,@AuthenticationPrincipal User user) {
        Property property = properties.findById(propertyid).get();
        Reviews reviewsStat = reviewsRepository.findByPropertyAndUser(property, user);
        if(reviewsStat!=null) {
            return "Review Already Given!!";
        }else{
            reviews.setProperty(property);
            reviews.setUser(user);
            reviewsRepository.save(reviews);
            return "added";
        }
    }
    @GetMapping("/getReviews")
    public List<Reviews> getReviews(@AuthenticationPrincipal User user) {
       return reviewsRepository.findByUser(user);
    }
}
