package net.lviv.intoeat.testng.repositories;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.models.User;
import net.lviv.intoeat.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static net.lviv.intoeat.testng.utils.factories.UserFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/users.sql"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = userRepository.findByUsername(EXISTING_USER_NAME);
        assertThat(user, notNullValue());
        assertThat(user.getUsername(), is(EXISTING_USER_NAME));
    }

    @Test
    public void testFindByUnexistingUsername() {
        User user = userRepository.findByUsername(UNEXISTING_USERNAME);
        assertThat(user, nullValue());
    }

    @Test
    public void testFindByNullUsername() {
        User user = userRepository.findByUsername(null);
        assertThat(user, nullValue());
    }

    @Test
    public void testFindByEmptyUsername() {
        User user = userRepository.findByUsername("");
        assertThat(user, nullValue());
    }

}
