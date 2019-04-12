package net.lviv.intoeat.testng.functional;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.configuration.WebAppConfig;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.testng.utils.factories.GroupFactory;
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.lviv.intoeat.testng.utils.TestUtils.*;
import static net.lviv.intoeat.testng.utils.factories.TagFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class, WebAppConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/group_tag.sql"})
public class TagFunctionalTest {

    private static final String GET_TAG_ENDPOINT_PATH = "/tag";
    private static final String SEARCH_TAG_ENDPOINT_PATH = "/admin/tag/search";
    private static final String SAVE_TAG_ENDPOINT_PATH = "/admin/tag/save";
    private static final String REMOVE_TAG_ENDPOINT_PATH = "/admin/tag/remove";

    private static final String ADMIN_ENDPOINT_PATH = "/admin";

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Autowired
    private TagService tagService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testGetTagByName() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_TAG_ENDPOINT_PATH).param("name", EXISTING_TAG_1_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_TAG_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_TAG_1_NAME)));
    }

    @Test
    public void testGetTagByNameAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_TAG_ENDPOINT_PATH).param("name", EXISTING_TAG_1_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_TAG_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_TAG_1_NAME)));
    }

    @Test
    public void testGetTagByInvalidTagName() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_TAG_ENDPOINT_PATH).param("name", UNEXISTING_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(TAG_BY_NAME_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetTagById() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_TAG_ENDPOINT_PATH).param("id", "1").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_TAG_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_TAG_1_NAME)));
    }

    @Test
    public void testGetTagByIdAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_TAG_ENDPOINT_PATH).param("id", "1").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_TAG_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_TAG_1_NAME)));
    }

    @Test
    public void testGetTagByInvalidId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_TAG_ENDPOINT_PATH).param("id", String.valueOf(UNEXISTING_ENTITY_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(TAG_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetTagByEmptyId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_TAG_ENDPOINT_PATH).param("id", "").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(NULL_ID_ERROR_MESSAGE)));
    }

    @Test
    public void testGetAllTags() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_TAG_ENDPOINT_PATH + "/all").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(TOTAL_TAGS_NUMBER)));
    }

    @Test
    public void testGetAllTagsAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_TAG_ENDPOINT_PATH + "/all").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(TOTAL_TAGS_NUMBER)));
    }

    @Test
    public void testGetTagsByName() throws Exception {
        Map<String, Integer> dataProvider = getTagByNameDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String tagName = it.next();
            Integer expectedTagNumber = dataProvider.get(tagName);
            ResultActions resultActions = mockMvc.perform(
                    get(SEARCH_TAG_ENDPOINT_PATH).param("arg", tagName)
                            .accept(APPLICATION_JSON));
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedTagNumber)));
        }
    }

    @Test
    public void testAddTag() throws Exception {
        String jsonTag = convertToJson(createTag(TEST_TAG_NAME, TEST_TAG_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_TAG_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonTag));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<Tag> tags = tagService.getTags();
        assertThat(tags.size(), is(TOTAL_TAGS_NUMBER + 1));
    }

    @Test
    public void testAddTagWithEmptyData() throws Exception {
        String jsonTag = convertToJson(createTag("", ""));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_TAG_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonTag));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE)));

        List<Tag> tags = tagService.getTags();
        assertThat(tags.size(), is(TOTAL_TAGS_NUMBER));
    }

    @Test
    public void testAddTagWithExistingName() throws Exception {
        String jsonTag = convertToJson(createTag(EXISTING_TAG_1_NAME, TEST_TAG_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_TAG_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonTag));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(TAG_ALREADY_EXISTS_ERROR_MESSAGE)));

        List<Tag> tags = tagService.getTags();
        assertThat(tags.size(), is(TOTAL_TAGS_NUMBER));
    }

    @Test
    public void testEditTag() throws Exception {
        String jsonTag = convertToJson(createTag(EXISTING_TAG_ID_1, UPDATED_TAG_NAME, TEST_TAG_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_TAG_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonTag));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(EXISTING_TAG_ID_1)));
    }

    @Test
    public void testEditTagWithExistingName() throws Exception {
        String jsonTag = convertToJson(createTag(EXISTING_TAG_ID_2, EXISTING_TAG_1_NAME, TEST_TAG_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_TAG_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonTag));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(TAG_ALREADY_EXISTS_ERROR_MESSAGE)));
    }

    @Test
    public void testEditTagWithEmptyData() throws Exception {
        String jsonTag = convertToJson(createTag(EXISTING_TAG_ID_1, null, null));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_TAG_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonTag));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE)));
    }

    @Test
    public void testDeleteTag() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_TAG_ENDPOINT_PATH).param("id", String.valueOf(EXISTING_TAG_ID_1))
                        .param("groupId", GroupFactory.EXISTING_GROUP_ID_1.toString()).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(EXISTING_TAG_ID_1)));
    }

    @Test
    public void testDeleteTagWithoutGroup() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_TAG_ENDPOINT_PATH).param("id", String.valueOf(TAG_WITHOUT_GROUP_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(TAG_WITHOUT_GROUP_ID)));
    }

    @Test
    public void testDeleteTagByInvalidTagId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_TAG_ENDPOINT_PATH).param("id", String.valueOf(UNEXISTING_ENTITY_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(TAG_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testDeleteTagByEmptyTagId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_TAG_ENDPOINT_PATH).param("id", "").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(NOTHING_TO_DELETE_ERROR_MESSAGE)));
    }
}
