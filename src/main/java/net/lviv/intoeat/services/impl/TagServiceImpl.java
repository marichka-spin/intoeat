package net.lviv.intoeat.services.impl;


import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.repositories.TagRepository;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import net.lviv.intoeat.vmodels.VTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BaseValidator<Tag> tagValidator;

    @Override
    public VTag getVTagById(Integer id) {
        Tag tag = tagRepository.findOne(id);
        if (tag != null) {
            return VTag.transform(tag);
        } else {
            throw new EntityNotFoundException(RemovalValidator.ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, tagValidator.type(), id);
        }
    }

    @Override
    public VTag getVTagByName(String name) {
        Tag tag = tagRepository.findByName(name);
        if (tag != null) {
            return VTag.transform(tag);
        } else {
            throw new EntityNotFoundException("Tag with name %s not found.", name);
        }
    }

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<VTag> getVTags() {
        List<Tag> tags = tagRepository.findAll();
        List<VTag> vTags = new LinkedList<>();
        for (Tag tag : tags) {
            vTags.add(VTag.transform(tag));
        }
        return vTags;
    }

    @Override
    public List<VTag> getTagsByName(String arg) {
        List<Tag> tags = tagRepository.findTagsByName("%" + arg + "%");
        List<VTag> vTags = new LinkedList<>();
        for (Tag tag : tags) {
            vTags.add(VTag.transform(tag));
        }
        return vTags;
    }

    @Override
    public Tag saveTag(VTag vTag) {
        Tag tag = VTag.transformAdmin(vTag);
        tagValidator.validate(tag);
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Integer id) {
        ((RemovalValidator)tagValidator).validate(id);
        tagRepository.delete(id);
    }
}
