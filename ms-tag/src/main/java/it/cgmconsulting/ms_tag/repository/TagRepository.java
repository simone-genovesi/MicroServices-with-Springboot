package it.cgmconsulting.ms_tag.repository;

import it.cgmconsulting.ms_tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    boolean existsByTagName(String tagName);
    boolean existsByTagNameAndIdNot(String newTagName, int id);

    @Query("SELECT t.tagName FROM Tag t ORDER BY t.tagName")
    List<String> getAllTagNames();

    @Query(value = "SELECT t FROM Tag t WHERE t.tagName IN(:tags)")
    Set<Tag> getTags(Set<String> tags);
}
