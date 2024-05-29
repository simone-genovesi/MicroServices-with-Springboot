package it.cgmconsulting.ms_post.controller;

import it.cgmconsulting.ms_post.payload.request.SectionRequest;
import it.cgmconsulting.ms_post.payload.request.SectionUpdateRequest;
import it.cgmconsulting.ms_post.service.SectionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping("/v2/section")
    public ResponseEntity<?> createSection(
            @RequestBody @Valid SectionRequest request,
            @RequestHeader("userId") int author
    ){
        return sectionService.createSection(request, author);
    }

    @PutMapping("/v2/section/{id}")
    public ResponseEntity<?> updateSection(
            @RequestBody @Valid SectionUpdateRequest request,
            @RequestHeader("userId") int author,
            @PathVariable @Min(1) int id
    ){
        return sectionService.updateSection(request, author, id);
    }
}
