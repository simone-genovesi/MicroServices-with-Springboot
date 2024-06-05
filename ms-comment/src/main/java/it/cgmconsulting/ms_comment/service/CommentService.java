package it.cgmconsulting.ms_comment.service;

import it.cgmconsulting.ms_comment.configuration.BeanManagement;
import it.cgmconsulting.ms_comment.entity.Comment;
import it.cgmconsulting.ms_comment.exception.ResourceNotFoundException;
import it.cgmconsulting.ms_comment.payload.request.CommentRequest;
import it.cgmconsulting.ms_comment.payload.response.CommentResponse;
import it.cgmconsulting.ms_comment.repository.CommentRepository;
import it.cgmconsulting.ms_comment.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final Map<String,String> getWriters;
    private final BeanManagement bean;

    @Value("${application.security.internalToken}")
    String internalToken;


    public ResponseEntity<?> createComment(CommentRequest request, int author) {

        if(Boolean.FALSE.equals(existsPost(request.getPost()).getBody()))
            throw new ResourceNotFoundException("Post", "id", request.getPost());

        Comment comment = new Comment(request.getComment(), author, request.getPost());

        commentRepository.save(comment);

        bean.getWriters();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Comment written");
    }

    private ResponseEntity<Boolean> existsPost(int postId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY + "/" + Consts.MS_POST + "/v99/" + postId;

        ResponseEntity<Boolean> response = null;
        try{
            response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Boolean.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
        return response;
    }

    public ResponseEntity<?> getComment(int id) {

        CommentResponse c = commentRepository.getComment(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        c.setAuthor(getWriters.get(String.valueOf(c.getAuthor())));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(c);
    }

    public Comment findById(int id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }
}
