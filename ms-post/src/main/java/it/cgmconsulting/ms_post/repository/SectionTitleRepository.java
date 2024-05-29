package it.cgmconsulting.ms_post.repository;

import it.cgmconsulting.ms_post.entity.SectionTitle;
import it.cgmconsulting.ms_post.payload.response.SectionTitleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionTitleRepository extends JpaRepository<SectionTitle, Byte> {

    boolean existsBySectionTitle (String sectionTitle);
    boolean existsBySectionTitleAndIdNot (String sectionTitle, byte id);

    @Query(value="SELECT new it.cgmconsulting.ms_post.payload.response.SectionTitleResponse(" +
            "s.id, " +
            "sectionTitle) " +
            "FROM SectionTitle s WHERE visible")
    List<SectionTitleResponse> getAllVisible();
}
