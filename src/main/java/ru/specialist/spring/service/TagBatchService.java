package ru.specialist.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TagBatchService {

    private final TagService tagService;

    @Autowired
    public TagBatchService(TagService tagService) {
        this.tagService = tagService;
    }

    public void createTags(List<String> tags) {
        tags.forEach(tagService::create);
    }
}
