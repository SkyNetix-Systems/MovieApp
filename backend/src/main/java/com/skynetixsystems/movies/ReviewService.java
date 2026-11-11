package com.skynetixsystems.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    public Review createReview(String reviewBody, String imdbId) {

        // Create a new review with timestamps
        LocalDateTime now = LocalDateTime.now();

        Review review = repository.insert(
                new Review(reviewBody, now, now)
        );

        // Add the review ID to the Movie's reviews reference field
        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviews").value(review.getId()))
                .first();

        return review;
    }
}
