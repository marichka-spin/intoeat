package net.lviv.intoeat.testng.services;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.User;
import net.lviv.intoeat.services.UserService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static net.lviv.intoeat.testng.utils.factories.UserFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static net.lviv.intoeat.testng.utils.TestUtils.*;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/users.sql"})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @Test
    public void testGetUsers() {
        List<User> users = userService.getUsers();

        assertThat(users.size(), is(TOTAL_RECORD_NUMBER));
    }

    @Test
    public void testGetUserById() {
        User user = userService.getUserById(EXISTING_USER_ID_1);

        assertThat(user, notNullValue());
        assertThat(user.getId(), is(EXISTING_USER_ID_1));
        assertThat(user.getUsername(), is(EXISTING_USER_NAME));
    }

    @Test
    public void testGetUserByUnexistingId() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(USER_NOT_FOUND_ERROR_MESSAGE);

        userService.getUserById(UNEXISTING_ENTITY_ID);
    }

    @Test
    public void testGetUserByNullId() {
        rule.expect(InvalidDataAccessApiUsageException.class);
        rule.expectMessage(NULL_ID_ERROR_MESSAGE);

        userService.getUserById(null);
    }

    @Test
    public void testGetUserByUsername() {
        User user = userService.getUserByUsername(EXISTING_USER_NAME);

        assertThat(user, notNullValue());
        assertThat(user.getId(), is(EXISTING_USER_ID_1));
        assertThat(user.getUsername(), is(EXISTING_USER_NAME));
    }

    @Test
    public void testGetUserByUnexistingUsername() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(USER_BY_USERNAME_NOT_FOUND_ERROR_MESSAGE);

        userService.getUserByUsername(UNEXISTING_USERNAME);
    }

    @Test
    public void testGetUserNullUsername() {
        rule.expect(EntityNotFoundException.class);

        userService.getUserByUsername(null);
    }

    @Test
    public void testGetUserEmptyUsername() {
        rule.expect(EntityNotFoundException.class);

        userService.getUserByUsername("");
    }

    @Test
    public void testAddUser() {
        User user = createUser(TEST_USERNAME, TEST_PASSWORD);

        User userFromDB = userService.saveUser(user);
        assertThat(userService.getUsers().size(), is(TOTAL_RECORD_NUMBER + 1));
        assertThat(userFromDB, is(user));
    }

    @Test
    public void testAddUserWithExistingUsername() throws Exception {
        User user = createUser(EXISTING_USER_NAME, TEST_PASSWORD);

        rule.expect(DuplicateEntityException.class);
        rule.expectMessage(USER_ALREADY_EXISTS_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testAddUserWithNullData() throws Exception {
        User user = createUser(null, null);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testAddUserWithEmptyData() throws Exception {
        User user = createUser("", "");

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testAddUserWithNullUsername() throws Exception {
        User user = createUser(null, TEST_PASSWORD);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testAddUserWithNullPassword() throws Exception {
        User user = createUser(TEST_USERNAME, null);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testEditUser() {
        User user = createUser(EXISTING_USER_ID_1, UPDATED_USER_NAME, TEST_PASSWORD);

        User userFromDB = userService.saveUser(user);
        assertThat(userFromDB.getUsername(), is(UPDATED_USER_NAME));
        assertThat(userFromDB.getPassword(), is(TEST_PASSWORD));
    }

    @Test
    public void testEditUserWithExistingUsername() throws Exception {
        User user = createUser(EXISTING_USER_ID_2, EXISTING_USER_NAME, TEST_PASSWORD);

        rule.expect(DuplicateEntityException.class);
        rule.expectMessage(USER_ALREADY_EXISTS_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testEditUserWithNullData() throws Exception {
        User user = createUser(EXISTING_USER_ID_1, null, null);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testEditUserWithEmptyData() throws Exception {
        User user = createUser(EXISTING_USER_ID_1, "", "");

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testEditUserWithNullUsername() throws Exception {
        User user = createUser(EXISTING_USER_ID_1, null, TEST_PASSWORD);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testEditUserWithNullPassword() throws Exception {
        User user = createUser(EXISTING_USER_ID_1, TEST_USERNAME, null);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE);
        userService.saveUser(user);
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(EXISTING_USER_ID_1);

        assertThat(userService.getUsers().size(), is(TOTAL_RECORD_NUMBER - 1));
    }

    @Test
    public void testDeleteUserByUnexistingId() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(USER_NOT_FOUND_ERROR_MESSAGE);

        userService.deleteUser(UNEXISTING_ENTITY_ID);
    }

    @Test
    public void testDeleteUserByNullId() {
        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(NOTHING_TO_DELETE_ERROR_MESSAGE);

        userService.deleteUser(null);
    }

}
