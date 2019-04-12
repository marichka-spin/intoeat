package net.lviv.intoeat.testng.functional;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.configuration.WebAppConfig;
import net.lviv.intoeat.services.GroupService;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.testng.utils.factories.GroupFactory;
import net.lviv.intoeat.testng.utils.factories.TagFactory;
import net.lviv.intoeat.vmodels.VGroup;
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
import static net.lviv.intoeat.testng.utils.factories.GroupFactory.*;
import static net.lviv.intoeat.testng.utils.factories.TagFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class, WebAppConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/group_tag.sql"})
public class GroupFunctionalTest {

    private static final String GET_GROUP_ENDPOINT_PATH = "/group";
    private static final String SEARCH_GROUP_ENDPOINT_PATH = "/admin/group/search";
    private static final String SAVE_GROUP_ENDPOINT_PATH = "/admin/group/save";
    private static final String REMOVE_GROUP_ENDPOINT_PATH = "/admin/group/remove";

    private static final String ADMIN_ENDPOINT_PATH = "/admin";

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mockMvc;
	
	@Autowired
	private GroupService groupService;

    @Autowired
    private TagService tagService;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	public void testGetGroupByName() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(GET_GROUP_ENDPOINT_PATH).param("name", EXISTING_GROUP_NAME).accept(APPLICATION_JSON_UTF8));
		resultActions.andExpect(status().isOk())
		    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
		    .andExpect(jsonPath("$.id", is(EXISTING_GROUP_ID_1)))
		    .andExpect(jsonPath("$.name", is(EXISTING_GROUP_NAME)))
		    .andExpect(jsonPath("$.description", is(EXISTING_GROUP_DESCRIPTION)));
	}
	
	@Test
	public void testGetGroupByNotExistedName() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(GET_GROUP_ENDPOINT_PATH).param("name", NON_EXISTING_GROUP_NAME).accept(APPLICATION_JSON_UTF8));
		resultActions.andExpect(status().isNotFound())
	        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	        .andExpect(jsonPath("$.message", is(GROUP_BY_NAME_NOT_FOUND_ERROR_MESSAGE)));
	}
	
	@Test
	public void testGetGroupById() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(GET_GROUP_ENDPOINT_PATH).param("id", EXISTING_GROUP_ID_1.toString()).accept(APPLICATION_JSON_UTF8));
		resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_GROUP_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_GROUP_NAME)))
                .andExpect(jsonPath("$.description", is(EXISTING_GROUP_DESCRIPTION)));
	}
	
	@Test
	public void testGetGroupByNotExistedId() throws Exception {
		ResultActions resultActions = mockMvc.perform(get(GET_GROUP_ENDPOINT_PATH).param("id", NON_EXISTING_GROUP_ID.toString()).accept(APPLICATION_JSON_UTF8));
		resultActions.andExpect(status().isNotFound())
	        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
	        .andExpect(jsonPath("$.message", is(GROUP_NOT_FOUND_ERROR_MESSAGE)));
	}
 
	@Test
	public void testGetAllGroups() throws Exception {
		 ResultActions resultActions = mockMvc.perform(get(GET_GROUP_ENDPOINT_PATH + "/all").accept(APPLICATION_JSON));
		 resultActions.andExpect(status().isOk())
			.andExpect(content().contentType(APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", hasSize(EXPECTED_GROUP_LIST_SIZE)));
	}

    @Test
    public void testGetGroupByNameAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ADMIN_ENDPOINT_PATH + GET_GROUP_ENDPOINT_PATH).
                param("name", EXISTING_GROUP_NAME).accept(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_GROUP_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_GROUP_NAME)))
                .andExpect(jsonPath("$.description", is(EXISTING_GROUP_DESCRIPTION)))
                .andExpect(jsonPath("$.tagsIds", hasSize(TAG_NUMBER_OF_GROUP_1)));
    }

    @Test
    public void testGetGroupByNotExistedNameAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ADMIN_ENDPOINT_PATH + GET_GROUP_ENDPOINT_PATH)
                .param("name", NON_EXISTING_GROUP_NAME).accept(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(GROUP_BY_NAME_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetGroupByIdAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ADMIN_ENDPOINT_PATH + GET_GROUP_ENDPOINT_PATH)
                .param("id", EXISTING_GROUP_ID_1.toString()).accept(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_GROUP_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_GROUP_NAME)))
                .andExpect(jsonPath("$.description", is(EXISTING_GROUP_DESCRIPTION)))
                .andExpect(jsonPath("$.tagsIds", hasSize(TAG_NUMBER_OF_GROUP_1)));
    }

    @Test
    public void testGetGroupByNotExistedIdAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ADMIN_ENDPOINT_PATH + GET_GROUP_ENDPOINT_PATH)
                .param("id", NON_EXISTING_GROUP_ID.toString()).accept(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(GROUP_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetAllGroupsAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(ADMIN_ENDPOINT_PATH + GET_GROUP_ENDPOINT_PATH + "/all").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(EXPECTED_GROUP_LIST_SIZE)));
    }

    @Test
    public void testGetGroupsByName() throws Exception {
        Map<String, Integer> dataProvider = getGroupByNameDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String tagName = it.next();
            Integer expectedTagNumber = dataProvider.get(tagName);
            ResultActions resultActions = mockMvc.perform(
                    get(SEARCH_GROUP_ENDPOINT_PATH).param("arg", tagName)
                            .accept(APPLICATION_JSON));
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedTagNumber)));
        }
    }
	
	@Test
	public void testAddGroupWithTag() throws Exception {
		String jsonGroup = convertToJson(createGroup(NEW_GROUP_NAME, NEW_GROUP_DESCRIPTION, EXISTING_TAG_ID_1));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonGroup));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<VGroup> groups = groupService.getVGroups();
        assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE + 1));
    }

    @Test
    public void testAddEmptyGroup() throws Exception {
        String jsonGroup = convertToJson(createGroup("", ""));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonGroup));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(EXCEPTION_MANDATORY_FIELD_IS_MISSING)));

        List<VGroup> groups = groupService.getVGroups();
        assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE));
    }

    @Test
    public void testAddDuplicateGroup() throws Exception {
        String jsonGroup = convertToJson(createGroup(EXISTING_GROUP_NAME, EXISTING_GROUP_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonGroup));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(GROUP_ALREADY_EXISTS_ERROR_MESSAGE)));

        List<VGroup> groups = groupService.getVGroups();
        assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE));
    }

    @Test
    public void testAddGroupWithoutRequiredParam() throws Exception {
        String jsonGroup = convertToJson(GroupFactory.createGroup(null, NEW_GROUP_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonGroup));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE));

        List<VGroup> groups = groupService.getVGroups();
        assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE));
    }

    @Test
    public void testEditGroupDataAndAddingExistingTag() throws Exception {
        String jsonGroup = convertToJson(createGroup(EXISTING_GROUP_ID_1, NEW_GROUP_NAME, NEW_GROUP_DESCRIPTION, EXISTING_TAG_ID_1));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonGroup));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(EXISTING_GROUP_ID_1)));

        List<VGroup> groups = groupService.getVGroups();
        assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE));
        assertThat(tagService.getTags().size(), is(TagFactory.TOTAL_TAGS_NUMBER));
    }

    @Test
    public void testEditGroupWithEmptyData() throws Exception {
        String jsonGroup = convertToJson(createGroup(EXISTING_GROUP_ID_1, "", ""));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonGroup));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(EXCEPTION_MANDATORY_FIELD_IS_MISSING)));
    }

    @Test
    public void testEditGroupWithExistingName() throws Exception {
        String jsonGroup = convertToJson(createGroup(EXISTING_GROUP_ID_2, EXISTING_GROUP_NAME, UPDATED_GROUP_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonGroup));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(GROUP_ALREADY_EXISTS_ERROR_MESSAGE)));
    }

    @Test
    public void testEditGroupWithoutRequiredParam() throws Exception {
        String jsonGroup = convertToJson(createGroup(EXISTING_GROUP_ID_1, null, NEW_GROUP_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_GROUP_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonGroup));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE)));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(REMOVE_GROUP_ENDPOINT_PATH).param("id", EXISTING_GROUP_ID_1.toString())
                .accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.objectId", is(EXISTING_GROUP_ID_1)));

        List<VGroup> groups = groupService.getVGroups();
        assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE - 1));
    }


    @Test
    public void testDeleteGroupByInvalidGroupId() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(REMOVE_GROUP_ENDPOINT_PATH).param("id", NON_EXISTING_GROUP_ID.toString())
                .accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(GROUP_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testDeleteGroupByEmptyTagId() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(REMOVE_GROUP_ENDPOINT_PATH).param("id", "")
                .accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(NOTHING_TO_DELETE_ERROR_MESSAGE)));
    }
}
