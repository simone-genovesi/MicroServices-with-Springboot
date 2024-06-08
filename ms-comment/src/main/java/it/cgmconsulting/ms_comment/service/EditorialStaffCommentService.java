package it.cgmconsulting.ms_comment.service;

import it.cgmconsulting.ms_comment.entity.Comment;
import it.cgmconsulting.ms_comment.entity.EditorialStaffComment;
import it.cgmconsulting.ms_comment.entity.EditorialStaffCommentId;
import it.cgmconsulting.ms_comment.exception.ResourceNotFoundException;
import it.cgmconsulting.ms_comment.payload.request.EditorialStaffCommentRequest;
import it.cgmconsulting.ms_comment.payload.response.EditorialStaffCommentResponse;
import it.cgmconsulting.ms_comment.repository.EditorialStaffCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EditorialStaffCommentService {

    private final EditorialStaffCommentRepository escRepository;
    private final CommentService commentService;

    public ResponseEntity<?> createEditorialStaffComment(EditorialStaffCommentRequest request) {
        Comment c = commentService.findById(request.getCommentId());
        EditorialStaffComment esc = new EditorialStaffComment(
                new EditorialStaffCommentId(c),
                request.getComment()
        );
        escRepository.save(esc);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(EditorialStaffCommentResponse
                        .fromEntityToResponse(esc));
    }

    public ResponseEntity<?> updateEditorialStaffComment(EditorialStaffCommentRequest request) {
        int result = escRepository.updateEditorialStaffComment(
                request.getComment(),
                request.getCommentId(),
                LocalDateTime.now()
        );
        if(result == 0)
            throw new ResourceNotFoundException("Editorial Staff Comment", "id", request.getCommentId());
        return ResponseEntity.ok("Editorial Staff Comment has been updated");
    }

    @Transactional
    public ResponseEntity<?> updateEditorialStaffCommentBis(EditorialStaffCommentRequest request) {
        EditorialStaffComment esc = escRepository.getById(request.getCommentId())
                .orElseThrow(()-> new ResourceNotFoundException("Editorial Staff Comment", "id", request.getCommentId()));
        esc.setComment(request.getComment());
        esc.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(EditorialStaffCommentResponse.fromEntityToResponse(esc));
    }

    public ResponseEntity<?> deleteEditorialStaffComment(int commentId) {
        int result = escRepository.deleteEditorialStaffCommentJPQL(commentId);
        if (result == 0)
            throw new ResourceNotFoundException("Editorial Staff Comment", "id", commentId);
        return ResponseEntity.ok("Editorial Staff Comment deleted successfully");
    }

}