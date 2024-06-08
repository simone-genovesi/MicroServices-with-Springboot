package it.cgmconsulting.ms_comment.controller;

import it.cgmconsulting.ms_comment.payload.request.CommentRequest;
import it.cgmconsulting.ms_comment.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/v3")
    public ResponseEntity<?> createComment(
            @RequestBody @Valid CommentRequest request,
            @RequestHeader("userId") int author
            ){
        return commentService.createComment(request, author);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getComment(@PathVariable @Min(1) int id){
        return commentService.getComment(id);
    }

    @GetMapping("/v0/{id}")
    public ResponseEntity<?> getComments(@PathVariable @Min(1) int postId){
        return commentService.getComments(postId);
    }

    // modifica del testo del commento possibile se effettuato entro 60 secondi dla primo salvataggio
    @PatchMapping("/v3/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable @Min(1) int commentId,
            @RequestParam @NotBlank @Size(min = 1, max = 255) String comment,
            @RequestHeader("userId") int author
    ){
        return commentService.updateComment(commentId, comment, author);
    }

    // delete del commento possibile se effettuata entro 60 secondi dal primo salvataggio
    @DeleteMapping("/v3/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable @Min(1) int commentId,
            @RequestHeader("userId") int author
    ){
        return commentService.deleteComment(commentId, author);
    }

    /** elenco commenti + eventuali risposte redazione **/
    @GetMapping("/v0/full/{postId}")
    public ResponseEntity<?> getFullComments(@PathVariable @Min(1) int postId){
        return commentService.getFullComments(postId);
    }

    /** elenco commenti + eventuali risposte redazione **/
    @GetMapping("/v0/full/bis/{postId}")
    public ResponseEntity<?> getFullCommentsBis(@PathVariable @Min(1) int postId){
        return commentService.getFullCommentsBis(postId);
    }

}
