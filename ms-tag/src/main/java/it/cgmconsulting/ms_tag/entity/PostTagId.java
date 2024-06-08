package it.cgmconsulting.ms_tag.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class PostTagId {

    private int postId;

    @ManyToOne
    @JoinColumn(name="tag_id", nullable = false)
    private Tag tag;
}
