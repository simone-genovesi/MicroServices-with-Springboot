package it.cgmconsulting.ms_comment.payload.response;

import it.cgmconsulting.ms_comment.entity.EditorialStaffComment;
import it.cgmconsulting.ms_comment.utils.Consts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class EditorialStaffCommentResponse {

    private int escId;
    private String comment;
    private LocalDateTime createdAt;
    private String escAuthor = Consts.REDAZIONE;

    public EditorialStaffCommentResponse(int escId, String comment, LocalDateTime createdAt) {
        this.escId = escId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public static EditorialStaffCommentResponse fromEntityToResponse(EditorialStaffComment e){
        return new EditorialStaffCommentResponse(
                e.getId().getCommentId().getId(),
                e.getComment(),
                LocalDateTime.now()
        );
    }
}
