package it.cgmconsulting.ms_post.service;

import it.cgmconsulting.ms_post.configuration.BeanManagement;
import it.cgmconsulting.ms_post.entity.Post;
import it.cgmconsulting.ms_post.exception.GenericException;
import it.cgmconsulting.ms_post.exception.ResourceNotFoundException;
import it.cgmconsulting.ms_post.payload.request.PostRequest;
import it.cgmconsulting.ms_post.payload.response.PostDetailResponse;
import it.cgmconsulting.ms_post.payload.response.PostResponse;
import it.cgmconsulting.ms_post.payload.response.SectionResponse;
import it.cgmconsulting.ms_post.repository.PostRepository;
import it.cgmconsulting.ms_post.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    @Value("${application.security.internalToken}")
    String internalToken;

    private final PostRepository postRepository;
    private final SectionRepository sectionRepository;
    private final Map<String,String> getWriters;


    public ResponseEntity<?> createPost(PostRequest request, int author) {
        Post post = new Post(request.getTitle(), request.getPostImage(), author);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postRepository.save(post));
    }

    public ResponseEntity<?> getPostDetail(int id) {
        // recuperare il post se esistente e pubblicato
        PostDetailResponse p = postRepository.getPostDetail(id, LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        // recuperare le sezioni relative al post in questione
        Set<SectionResponse> sections = sectionRepository.getSectionsResponse(id);
        p.setSections(sections);

        // recuperare lo username dell'autore
        p.setAuthor(getWriters.get(p.getAuthor()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(p);
    }

    public ResponseEntity<?> getPostDetailBis(int id) {
        Post p = postRepository.findByIdAndPublicationDateIsNotNullAndPublicationDateLessThanEqual(id, LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        PostDetailResponse pdr = new PostDetailResponse(
                p.getId(),
                p.getTitle(),
                p.getPublicationDate(),
                p.getPostImage()
        );
        Set<SectionResponse> sections = p.getSections()
                .stream()
                .map(SectionResponse::mapToResponse)
                .collect(Collectors.toSet());

        pdr.setSections(sections);
        pdr.setAuthor(getWriters.get(String.valueOf(p.getAuthor())));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pdr);
    }

//    private ResponseEntity<String> getUsername(int userId){
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization-Internal", internalToken);
//
//        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
//
//        String url = "http://localhost:9090/ms-auth/v99/" + userId;
//
//        ResponseEntity<String> banner = null;
//        try{
//            banner = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//        } catch (RestClientException e){
//            log.error(e.getMessage());
//            return ResponseEntity.status(500).body(null);
//        }
//        return banner;
//    }

    public ResponseEntity<?> publish(int id, LocalDate publicationDate) {
        // se publicationDate non viene passata = LocalDate.now()
        // altrimenti deve essere una data nel futuro
        // per poter pubblicare bisogna anche verificare che ci sia almeno una sezione associata al post
        Post p = findById(id);
        if (p.getSections().isEmpty())
            throw new GenericException("No post's sections found", HttpStatus.CONFLICT);

        if (publicationDate != null && publicationDate.isBefore(LocalDate.now()))
            throw new GenericException("The date must be NOW or in the future", HttpStatus.CONFLICT);
        if(publicationDate == null)
            p.setPublicationDate(LocalDate.now());
        else
            p.setPublicationDate(publicationDate);

        p.setUpdatedAt(LocalDateTime.now());
        postRepository.save(p);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Post published");
    }

    public Post findById(int id){
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    public ResponseEntity<?> getLastPublishedPost(int pageNumber, int pageSize, String sortBy, String direction) {
        // return List<PostResponse>
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostResponse> posts = postRepository.getLastPublishedPost(pageable, LocalDate.now());
        List<PostResponse> list  = posts.getContent();
        Map<String,String> m = getWriters;
        for(PostResponse p : list)
            p.setAuthor(getWriters.get(p.getAuthor()));
        return ResponseEntity.ok(list);
    }
}
