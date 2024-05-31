package it.cgmconsulting.ms_post.repository;

import it.cgmconsulting.ms_post.entity.Section;
import it.cgmconsulting.ms_post.payload.response.SectionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query(value = "SELECT new it.cgmconsulting.ms_post.payload.response.SectionResponse(" +
            "s.id, " +
            "s.post.id, " +
            "s.title.sectionTitle, " +
            "s.subTitle, " +
            "s.content, " +
            "s.sectionImage" +
            ") FROM Section s " +
            "WHERE s.post.id = :postId")
    Set<SectionResponse> getSectionsResponse(int postId);
}
