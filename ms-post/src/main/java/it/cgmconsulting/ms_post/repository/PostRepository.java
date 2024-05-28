package it.cgmconsulting.ms_post.repository;

import it.cgmconsulting.ms_post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
