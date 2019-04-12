package net.lviv.intoeat.services.impl;

import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.models.User;
import net.lviv.intoeat.repositories.UserRepository;
import net.lviv.intoeat.services.UserService;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BaseValidator<User> userValidator;

    @Override
    public User getUserById(Integer id) {
        User user = userRepository.findOne(id);
        if (user != null) {
            return user;
        } else {
            throw new EntityNotFoundException(RemovalValidator.ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, userValidator.type(), id);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new EntityNotFoundException("User with username %s not found", username);
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        Iterator<User> usersIt = userRepository.findAll().iterator();
        while (usersIt.hasNext()) {
            users.add(usersIt.next());
        }
        return users;
    }

    @Override
    public User saveUser(User user) {
        userValidator.validate(user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        ((RemovalValidator)userValidator).validate(id);
        userRepository.delete(id);
    }
}
