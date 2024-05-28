package it.cgmconsulting.ms_post.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    Set<Section> sections = new HashSet<>();

    @Column(length = 255)
    private String postimage;

    private int author;

    public Post addSection(Section s){
        sections.add(s);
        s.setPost(this);
        return this;
    }

    public Post removeSection(Section s){
        sections.remove(s);
        s.setPost(null);
        return this;
    }
}
