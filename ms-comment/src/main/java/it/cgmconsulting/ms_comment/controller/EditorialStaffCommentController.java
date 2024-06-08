package it.cgmconsulting.ms_comment.controller;

import it.cgmconsulting.ms_comment.payload.request.EditorialStaffCommentRequest;
import it.cgmconsulting.ms_comment.service.EditorialStaffCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EditorialStaffCommentController {

    private final EditorialStaffCommentService escService;

    @PostMapping("/v5")
    public ResponseEntity<?> createEditorialStaffComment(@RequestBody @Valid EditorialStaffCommentRequest request){
        return escService.createEditorialStaffComment(request);
    }

    @PatchMapping("/v5")
    public ResponseEntity<?> updateEditorialStaffComment(@RequestBody @Valid EditorialStaffCommentRequest request){
        return escService.updateEditorialStaffComment(request);
    }

    @PatchMapping("/v5/bis")
    public ResponseEntity<?> updateEditorialStaffCommentBis(@RequestBody @Valid EditorialStaffCommentRequest request){
        return escService.updateEditorialStaffCommentBis(request);
    }

    @DeleteMapping("/v5/{commentId}")
    public ResponseEntity<?> deleteEditorialStaffCommentBis(@PathVariable int commentId){
        return escService.deleteEditorialStaffComment(commentId);
    }
}
