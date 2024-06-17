package it.cgmconsulting.ms_tag.service;

import it.cgmconsulting.ms_tag.entity.Tag;
import it.cgmconsulting.ms_tag.exception.GenericException;
import it.cgmconsulting.ms_tag.exception.ResourceNotFoundException;
import it.cgmconsulting.ms_tag.repository.PostTagRepository;
import it.cgmconsulting.ms_tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    public ResponseEntity<?> createTag(String tagName) {
        if(tagRepository.existsByTagName(tagName))
            throw new GenericException("Tag name already exists", HttpStatus.CONFLICT);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tagRepository.save(new Tag(tagName)));
    }

    public ResponseEntity<?> getAllTags() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagRepository.getAllTagNames());
    }

    @Transactional
    public ResponseEntity<?> updateTag(int id, String newTagName) {
        if(tagRepository.existsByTagNameAndIdNot(newTagName, id))
            throw new GenericException("Tag name already exists with another id", HttpStatus.CONFLICT);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag","id", id));
        tag.setTagName(newTagName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Tag name edited successfully");
    }

    @Transactional
    public ResponseEntity<?> deleteTag(int id) {
        //postTagRepository.deleteByPostTagIdTagId(id);
        tagRepository.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
