package com.project.team9.service;

import com.project.team9.model.Tag;
import com.project.team9.repo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository repository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.repository = tagRepository;
    }

    public void addTag(Tag tag) {
        repository.save(tag);
    }

    public void saveAll(List<Tag> tags) {
        repository.saveAll(tags);
    }
}
