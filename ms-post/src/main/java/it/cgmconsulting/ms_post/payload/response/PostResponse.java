package it.cgmconsulting.ms_post.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class PostResponse {

    private int id;
    private String title;
    private LocalDate publicationDate;
    private String author;

}
