package it.cgmconsulting.ms_rating.repository;

import it.cgmconsulting.ms_rating.entity.Rating;
import it.cgmconsulting.ms_rating.entity.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query(value="SELECT COALESCE(AVG(r.rate), 0d) " +
            "FROM Rating r " +
            "WHERE r.ratingId.postId = :postId")
    double getAvg(int postId);
}
