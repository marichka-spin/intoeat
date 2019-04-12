package net.lviv.intoeat.testng.utils.factories;

import net.lviv.intoeat.models.User;
import net.lviv.intoeat.testng.utils.StubUser;

import static net.lviv.intoeat.testng.utils.TestUtils.UNEXISTING_ENTITY_ID;

public class UserFactory {

    public static final int TOTAL_RECORD_NUMBER = 5;
    public static final int EXISTING_USER_ID_1 = 1;
    public static final int EXISTING_USER_ID_2 = 2;
    public static final String EXISTING_USER_NAME = "user1";
    public static final String UPDATED_USER_NAME = "updated-user";
    public static final String UNEXISTING_USERNAME = "unexisting-user";

    public static final String TEST_USERNAME = "test_user";
    public static final String TEST_PASSWORD = "test_password";

    public static final String USER_ALREADY_EXISTS_ERROR_MESSAGE = String.format("User with username: %s already exists.", EXISTING_USER_NAME);
    public static final String USER_NOT_FOUND_ERROR_MESSAGE = String.format("User with id %d not found.", UNEXISTING_ENTITY_ID);
    public static final String USER_BY_USERNAME_NOT_FOUND_ERROR_MESSAGE = String.format("User with username %s not found", UNEXISTING_USERNAME);

    public static User createUser(String username, String password) {
        return createUser(null, username, password);
    }

    public static User createUser(Integer id, String username, String password) {
        User user = new User();
        if (id != null) {
            user.setId(id);
        }
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public static StubUser createStubUser(String username, String password) {
        return createStubUser(null, username, password);
    }

    public static StubUser createStubUser(Integer id, String username, String password) {
        StubUser user = new StubUser();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
