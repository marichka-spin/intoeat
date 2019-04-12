package net.lviv.intoeat.validation.impl;


import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.repositories.TagRepository;
import net.lviv.intoeat.utils.IntoeatUtils;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import org.apache.commons.lang3.StringUtils;

public class TagValidator implements BaseValidator<Tag>, RemovalValidator {

    private static final String TYPE_CLASS = IntoeatUtils.getGenericType(TagValidator.class).getSimpleName();

    private TagRepository tagRepository;

    public TagValidator(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void validate(Tag tag) {
        if (StringUtils.isEmpty(tag.getName())) {
            throw new InvalidInputParametersException(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MSG);
        }

        Tag existingTag = tagRepository.findByName(tag.getName());
        if (existingTag != null && (tag.getId() == null || !existingTag.getId().equals(tag.getId()))) {
            throw new DuplicateEntityException(ENTITY_WITH_NAME_EXISTS_ERROR_MSG, TYPE_CLASS, tag.getName());
        }
    }

    @Override
    public void validate(Integer id) {
        if (id == null) {
            throw new InvalidInputParametersException(NOTHING_TO_DELETE_ERROR_MSG);
        }

        if (!tagRepository.exists(id)) {
            throw new EntityNotFoundException(ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, TYPE_CLASS, id);
        }
    }

    @Override
    public String type() {
        return TYPE_CLASS;
    }
}
