package it.cgmconsulting.ms_comment.repository;

import it.cgmconsulting.ms_comment.entity.EditorialStaffComment;
import it.cgmconsulting.ms_comment.entity.EditorialStaffCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorialStaffCommentRepository extends JpaRepository<EditorialStaffComment, EditorialStaffCommentId> {
}
