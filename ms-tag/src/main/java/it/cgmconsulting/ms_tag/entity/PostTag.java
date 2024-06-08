package it.cgmconsulting.ms_tag.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class PostTag {

    @EmbeddedId
    private PostTagId postTagId;
}
