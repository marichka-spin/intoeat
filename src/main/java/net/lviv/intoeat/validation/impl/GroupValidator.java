package net.lviv.intoeat.validation.impl;

import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.repositories.GroupRepository;
import net.lviv.intoeat.utils.IntoeatUtils;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import org.apache.commons.lang3.StringUtils;

public class GroupValidator implements BaseValidator<Group>, RemovalValidator {

    private static final String TYPE_CLASS = IntoeatUtils.getGenericType(GroupValidator.class).getSimpleName();
	
	private GroupRepository groupRepository;

    public GroupValidator(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

	@Override
	public void validate(Group group) {
		if (StringUtils.isEmpty(group.getName())) {
			throw new InvalidInputParametersException(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MSG);
		}

		Group existingGroup = groupRepository.findByName(group.getName());
		if (existingGroup != null && (group.getId() == null || !existingGroup.getId().equals(group.getId()))) {
			throw new DuplicateEntityException(ENTITY_WITH_NAME_EXISTS_ERROR_MSG, TYPE_CLASS, group.getName());
		}
	}

    @Override
	public void validate(Integer id) {
		if (id == null) {
			throw new InvalidInputParametersException(NOTHING_TO_DELETE_ERROR_MSG);
		}

		if (!groupRepository.exists(id)) {
			throw new EntityNotFoundException(ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, TYPE_CLASS, id);
		}
	}

    @Override
    public String type() {
        return TYPE_CLASS;
    }
}
