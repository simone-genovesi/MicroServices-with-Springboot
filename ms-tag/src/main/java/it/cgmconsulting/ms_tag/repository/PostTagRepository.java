package it.cgmconsulting.ms_tag.repository;

import it.cgmconsulting.ms_tag.entity.PostTag;
import it.cgmconsulting.ms_tag.entity.PostTagId;
import it.cgmconsulting.ms_tag.entity.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    @Query(value="SELECT pt.postTagId.tag FROM PostTag pt WHERE pt.postTagId.postId = :postId")
    Set<Tag> getTags(int postId);

    @Query("SELECT pt FROM PostTag pt WHERE pt.postTagId.postId = :postId")
    Set<PostTag> getPostTags(int postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostTag pt WHERE pt.postTagId.tag IN :deletableTags")
    void deleteOldPostTags(Set<Tag> deletableTags);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostTag pt WHERE pt.postTagId.postId = :postId")
    void deleteByPostId(int postId);

    @Query(value = "SELECT pt.postTagId.tag.tagName " +
            "FROM PostTag pt " +
            "WHERE pt.postTagId.postId = :postId " +
            "ORDER BY pt.postTagId.tag.tagName")
    Set<String> getTagsByPost(int postId);
}
