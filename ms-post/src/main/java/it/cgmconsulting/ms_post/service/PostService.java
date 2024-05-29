package it.cgmconsulting.ms_post.service;

import it.cgmconsulting.ms_post.entity.Post;
import it.cgmconsulting.ms_post.exception.ResourceNotFoundException;
import it.cgmconsulting.ms_post.payload.request.PostRequest;
import it.cgmconsulting.ms_post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public ResponseEntity<?> createPost(PostRequest request, int author) {
        Post post = new Post(request.getTitle(), request.getPostImage(), author);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postRepository.save(post));
    }

    public Post findById(int id){
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }
}
