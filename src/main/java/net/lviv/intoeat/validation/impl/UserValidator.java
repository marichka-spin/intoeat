package net.lviv.intoeat.validation.impl;

import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.User;
import net.lviv.intoeat.repositories.UserRepository;
import net.lviv.intoeat.utils.IntoeatUtils;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import org.apache.commons.lang3.StringUtils;

public class UserValidator implements BaseValidator<User>, RemovalValidator {

    private static final String TYPE_CLASS = IntoeatUtils.getGenericType(UserValidator.class).getSimpleName();

    private UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(User user) {
        if (StringUtils.isEmpty(user.getUsername())
                || StringUtils.isEmpty(user.getPassword())) {
            throw new InvalidInputParametersException(MANDATORY_PARAMETERS_MISSING_ERROR_MSG);
        }

        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && (user.getId() == null || !existingUser.getId().equals(user.getId()))) {
            throw new DuplicateEntityException("User with username: %s already exists.", user.getUsername());
        }

    }

    @Override
    public void validate(Integer id) {
        if (id == null) {
            throw new InvalidInputParametersException(NOTHING_TO_DELETE_ERROR_MSG);
        }

        if (!userRepository.exists(id)) {
            throw new EntityNotFoundException(ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, TYPE_CLASS, id);
        }
    }

    @Override
    public String type() {
        return TYPE_CLASS;
    }
}
