package net.lviv.intoeat.testng.functional;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.configuration.WebAppConfig;
import net.lviv.intoeat.models.User;
import net.lviv.intoeat.services.UserService;
import net.lviv.intoeat.testng.utils.factories.UserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static net.lviv.intoeat.testng.utils.factories.UserFactory.*;
import static net.lviv.intoeat.testng.utils.TestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class, WebAppConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/users.sql"})
public class UserFunctionalTest {
    
    private static final String GET_USER_ENDPOINT_PATH = "/user";
    private static final String SAVE_USER_ENDPOINT_PATH = "/user/save";
    private static final String REMOVE_USER_ENDPOINT_PATH = "/user/remove";


    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_USER_ENDPOINT_PATH).param("username", EXISTING_USER_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_USER_ID_1)))
                .andExpect(jsonPath("$.username", is(EXISTING_USER_NAME)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void testGetUserByInvalidUsername() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_USER_ENDPOINT_PATH).param("username", UNEXISTING_USERNAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(USER_BY_USERNAME_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetUserById() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_USER_ENDPOINT_PATH).param("id", "1").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_USER_ID_1)))
                .andExpect(jsonPath("$.username", is(EXISTING_USER_NAME)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void testGetUserByInvalidId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_USER_ENDPOINT_PATH).param("id", String.valueOf(UNEXISTING_ENTITY_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(USER_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetUserByEmptyId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_USER_ENDPOINT_PATH).param("id", "").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(NULL_ID_ERROR_MESSAGE)));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_USER_ENDPOINT_PATH + "/all").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(TOTAL_RECORD_NUMBER)));
    }

    @Test
    public void testAddUser() throws Exception {
        String jsonUser = convertToJson(createStubUser(TEST_USERNAME, TEST_PASSWORD));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_USER_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonUser));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<User> users = userService.getUsers();
        assertThat(users.size(), is(TOTAL_RECORD_NUMBER + 1));
    }

    @Test
    public void testAddUserWithEmptyData() throws Exception {
        String jsonUser = convertToJson(UserFactory.createStubUser("", ""));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_USER_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonUser));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE)));

        List<User> users = userService.getUsers();
        assertThat(users.size(), is(TOTAL_RECORD_NUMBER));
    }

    @Test
    public void testAddUserWithExistingUsername() throws Exception {
        String jsonUser = convertToJson(UserFactory.createStubUser(EXISTING_USER_NAME, TEST_PASSWORD));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_USER_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonUser));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(USER_ALREADY_EXISTS_ERROR_MESSAGE)));

        List<User> users = userService.getUsers();
        assertThat(users.size(), is(TOTAL_RECORD_NUMBER));
    }

    @Test
    public void testEditUser() throws Exception {
        String jsonUser = convertToJson(createStubUser(EXISTING_USER_ID_1, UPDATED_USER_NAME, TEST_PASSWORD));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_USER_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonUser));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(EXISTING_USER_ID_1)));
    }

    @Test
    public void testEditUserWithExistingUsername() throws Exception {
        String jsonUser = convertToJson(createStubUser(EXISTING_USER_ID_2, EXISTING_USER_NAME, TEST_PASSWORD));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_USER_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonUser));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(USER_ALREADY_EXISTS_ERROR_MESSAGE)));
    }

    @Test
    public void testEditUserWithEmptyData() throws Exception {
        String jsonUser = convertToJson(createStubUser(EXISTING_USER_ID_1, null, null));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_USER_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonUser));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(MANDATORY_PARAMETERS_MISSING_ERROR_MESSAGE)));
    }

    @Test
    public void testDeleteUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_USER_ENDPOINT_PATH).param("id", String.valueOf(EXISTING_USER_ID_1)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(EXISTING_USER_ID_1)));
    }

    @Test
    public void testDeleteUserByInvalidUserId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_USER_ENDPOINT_PATH).param("id", String.valueOf(UNEXISTING_ENTITY_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(USER_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testDeleteUserByEmptyUserId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_USER_ENDPOINT_PATH).param("id", "").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(NOTHING_TO_DELETE_ERROR_MESSAGE)));
    }

}
