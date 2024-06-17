package it.cgmconsulting.ms_rating.service;

import it.cgmconsulting.ms_rating.entity.Rating;
import it.cgmconsulting.ms_rating.entity.RatingId;
import it.cgmconsulting.ms_rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    // insert and update del voto
    public byte rate(int postId, int userId, byte rate) {
        RatingId ratingId = new RatingId(postId, userId);
        Rating rating = null;
        rating = ratingRepository.findById(ratingId).orElse(null);
        if(rating != null) {
            rating.setUpdatedAt(LocalDateTime.now());
            rating.setRate(rate);
        } else {
            rating = new Rating(ratingId, rate);
        }
        return ratingRepository.save(rating).getRate();
    }

    public double getAvg(int postId){
        return ratingRepository.getAvg(postId);
    }
}
