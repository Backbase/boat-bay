package com.backbase.oss.boat.bay.web.views.tag;

import com.backbase.oss.boat.bay.domain.Tag;
import com.backbase.oss.boat.bay.repository.TagRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boat/")
@Transactional
@RequiredArgsConstructor
public class TagManagerResource {

    private final TagRepository tagRepository;

    @PostMapping("tags/hide")
    public ResponseEntity<Void> hideTag(@RequestBody String tagName) {
        Tag tag = tagRepository.findOne(Example.of(new Tag().name(tagName))).orElseThrow(EntityNotFoundException::new);
        tag.setHide(true);
        tagRepository.save(tag);
        return ResponseEntity.accepted().build();
    }
}
