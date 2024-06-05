package it.cgmconsulting.ms_post.repository;

import it.cgmconsulting.ms_post.entity.Post;
import it.cgmconsulting.ms_post.payload.response.PostDetailResponse;
import it.cgmconsulting.ms_post.payload.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT new it.cgmconsulting.ms_post.payload.response.PostResponse(" +
            "p.id, " +
            "p.title, " +
            "p.publicationDate, " +
            "CAST(p.author AS string)" +
            ") FROM Post p " +
            "WHERE p.publicationDate IS NOT NULL " +
            "AND p.publicationDate <= :now")
    Page<PostResponse> getLastPublishedPost(Pageable pageable, LocalDate now);

    @Query(value = "SELECT p.author FROM Post p")
    Set<Integer> getAuthorIds();

    @Query(value="SELECT new it.cgmconsulting.ms_post.payload.response.PostDetailResponse(" +
            "p.id, " +
            "p.title, " +
            "p.publicationDate, " +
            "p.postImage," +
            "CAST(p.author AS string)" +
            ") FROM Post p " +
            "WHERE p.id = :id " +
            "AND (p.publicationDate IS NOT NULL AND p.publicationDate <= :now)")
    Optional<PostDetailResponse> getPostDetail(int id, LocalDate now);

    @Query(value = "SELECT p.author FROM Post p WHERE p.id = :postId ")
    int getAuthorId(int postId);

    Optional<Post> findByIdAndPublicationDateIsNotNullAndPublicationDateLessThanEqual(int id, LocalDate now);

}
