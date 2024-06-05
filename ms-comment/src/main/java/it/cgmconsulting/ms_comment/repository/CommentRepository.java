package it.cgmconsulting.ms_comment.repository;

import it.cgmconsulting.ms_comment.entity.Comment;
import it.cgmconsulting.ms_comment.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT new it.cgmconsulting.ms_comment.payload.response.CommentResponse(" +
            "c.id, " +
            "c.comment, " +
            "CAST(c.author AS string), " +
            "c.post " +
            ") FROM Comment c " +
            "WHERE c.id = :id")
    Optional<CommentResponse> getComment(int id);

    @Query(value = "SELECT c.author FROM Comment c")
    Set<Integer> getAuthorIds();

}
