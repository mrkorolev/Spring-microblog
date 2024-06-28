package ru.specialist.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.specialist.spring.repository.TagRepository;
import ru.specialist.spring.entity.Tag;


@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // REQUIRED (default) - creates a new transaction, but if there is a running transaction,
    // the new transaction is not created!

    // We want to still save the tags that come before the null tag, but this won't work
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        tagRepository.save(tag);
    }

    @Transactional(readOnly = true)                 // if there is a write operation, it gets cancelled
    public long tagCount() {
        return tagRepository.count();
    }
}
