package net.lviv.intoeat.configuration;

import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.models.User;
import net.lviv.intoeat.repositories.GroupRepository;
import net.lviv.intoeat.repositories.PlaceRepository;
import net.lviv.intoeat.repositories.TagRepository;
import net.lviv.intoeat.repositories.UserRepository;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.impl.GroupValidator;
import net.lviv.intoeat.validation.impl.PlaceValidator;
import net.lviv.intoeat.validation.impl.TagValidator;
import net.lviv.intoeat.validation.impl.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

    @Bean
    @Autowired
    public BaseValidator<User> userValidator(UserRepository userRepository) {
        return new UserValidator(userRepository);
    }

    @Bean
    @Autowired
    public BaseValidator<Tag> tagValidator(TagRepository tagRepository) {
        return new TagValidator(tagRepository);
    }

    @Bean
    @Autowired
    public BaseValidator<Group> groupValidator(GroupRepository groupRepository) {
        return new GroupValidator(groupRepository);
    }

    @Bean
    @Autowired
    public BaseValidator<Place> placeValidator(PlaceRepository placeRepository) {
        return new PlaceValidator(placeRepository);
    }
}
