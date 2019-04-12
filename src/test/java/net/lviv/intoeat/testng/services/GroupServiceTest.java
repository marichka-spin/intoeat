package net.lviv.intoeat.testng.services;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.services.GroupService;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.testng.utils.factories.TagFactory;
import net.lviv.intoeat.vmodels.VGroup;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static net.lviv.intoeat.testng.utils.factories.GroupFactory.*;
import static net.lviv.intoeat.testng.utils.factories.TagFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static net.lviv.intoeat.testng.utils.TestUtils.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/group_tag.sql"})
public class GroupServiceTest {
	
	@Autowired
    private GroupService groupService;

    @Autowired
    private TagService tagService;

    @Rule
    public ExpectedException rule = ExpectedException.none();
    
    @Test
    public void testGetVGroupById() {
    	VGroup group = groupService.getVGroupById(EXISTING_GROUP_ID_1);

    	assertThat(group.name, is(EXISTING_GROUP_NAME));
    	assertThat(group.description, is(EXISTING_GROUP_DESCRIPTION));
    	assertThat(group.tags.size(), is(TAG_NUMBER_OF_GROUP_1));
    }
    
    @Test
    public void testGetVGroupByNonExistingIdThrowEntityNotFoundException() {
    	rule.expect(EntityNotFoundException.class);
        rule.expectMessage(GROUP_NOT_FOUND_ERROR_MESSAGE);
        groupService.getVGroupById(NON_EXISTING_GROUP_ID);
    }

    @Test
    public void testGetVGroupByIdAdmin() {
        VGroup group = groupService.getVGroupByIdAdmin(EXISTING_GROUP_ID_1);

        assertThat(group.name, is(EXISTING_GROUP_NAME));
        assertThat(group.description, is(EXISTING_GROUP_DESCRIPTION));
        assertThat(group.tagsIds.size(), is(TAG_NUMBER_OF_GROUP_1));
    }

    @Test
    public void testGetVGroupByNonExistingIdThrowEntityNotFoundExceptionAdmin() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(GROUP_NOT_FOUND_ERROR_MESSAGE);
        groupService.getVGroupByIdAdmin(NON_EXISTING_GROUP_ID);
    }
    
    @Test
    public void testGetVGroupByName() {
    	VGroup group = groupService.getVGroupByName(EXISTING_GROUP_NAME);

        assertThat(group.name, is(EXISTING_GROUP_NAME));
        assertThat(group.description, is(EXISTING_GROUP_DESCRIPTION));
        assertThat(group.tags.size(), is(TAG_NUMBER_OF_GROUP_1));
    }
    
    @Test
    public void testGetVGroupByNonExistingNameThrowEntityNotFoundException() {
    	rule.expect(EntityNotFoundException.class);
        rule.expectMessage(GROUP_BY_NAME_NOT_FOUND_ERROR_MESSAGE);
        groupService.getVGroupByName(NON_EXISTING_GROUP_NAME);
    }

    @Test
    public void testGetVGroupByNameAdmin() {
        VGroup group = groupService.getVGroupByNameAdmin(EXISTING_GROUP_NAME);

        assertThat(group.name, is(EXISTING_GROUP_NAME));
        assertThat(group.description, is(EXISTING_GROUP_DESCRIPTION));
        assertThat(group.tagsIds.size(), is(TAG_NUMBER_OF_GROUP_1));
    }

    @Test
    public void testGetVGroupByNonExistingNameThrowEntityNotFoundExceptionAdmin() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(GROUP_BY_NAME_NOT_FOUND_ERROR_MESSAGE);
        groupService.getVGroupByNameAdmin(NON_EXISTING_GROUP_NAME);
    }
    
    @Test
    public void testGetVGroups() {
    	List<VGroup> groups = groupService.getVGroups();
    	assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE));
    }

    @Test
    public void testGetVGroupsAdmin() {
        List<VGroup> groups = groupService.getVGroupsAdmin();
        assertThat(groups.size(), is(EXPECTED_GROUP_LIST_SIZE));
    }

    @Test
    public void testAddVGroupWithExistingTags() {
        VGroup group = createGroup(NEW_GROUP_NAME, NEW_GROUP_DESCRIPTION, EXISTING_TAG_ID_1, EXISTING_TAG_ID_2);

        Group savedGroup = groupService.saveGroup(group);

        assertThat(groupService.getVGroups().size(), is(EXPECTED_GROUP_LIST_SIZE + 1));
        assertThat(tagService.getTags().size(), is(TagFactory.TOTAL_TAGS_NUMBER));

        assertThat(savedGroup.getName(), is(NEW_GROUP_NAME));
        assertThat(savedGroup.getDescription(), is(NEW_GROUP_DESCRIPTION));

        List<Tag> groupTags = savedGroup.getTags();
        assertThat(groupTags, notNullValue());
        assertThat(groupTags.size(), is(TAG_NUMBER_OF_GROUP_1 + 1));
    }
    
    @Test
    public void testAddVGroupWithoutMandatoryFieldThrowInvalidInputParametersException() {
    	VGroup group = createGroup(null, NEW_GROUP_DESCRIPTION);
    	
    	rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
    	groupService.saveGroup(group);
    }
    
    @Test
    public void testAddExistingVGroupThrowInvalidInputParametersException() {
    	VGroup group = createGroup(EXISTING_GROUP_NAME, NEW_GROUP_DESCRIPTION);
    	
    	rule.expect(DuplicateEntityException.class);
        rule.expectMessage(GROUP_ALREADY_EXISTS_ERROR_MESSAGE);
    	groupService.saveGroup(group);
    }

    @Test
    public void testEditVGroupDataAndAddingExistingTag() {
        VGroup group = createGroup(EXISTING_GROUP_ID_1, NEW_GROUP_NAME, NEW_GROUP_DESCRIPTION, EXISTING_TAG_ID_1, EXISTING_TAG_ID_2);

        Group savedGroup = groupService.saveGroup(group);

        assertThat(groupService.getVGroups().size(), is(EXPECTED_GROUP_LIST_SIZE));
        assertThat(tagService.getTags().size(), is(TagFactory.TOTAL_TAGS_NUMBER));

        assertThat(savedGroup.getName(), is(NEW_GROUP_NAME));
        assertThat(savedGroup.getDescription(), is(NEW_GROUP_DESCRIPTION));

        List<Tag> groupTags = savedGroup.getTags();
        assertThat(groupTags, notNullValue());
        assertThat(groupTags.size(), is(TAG_NUMBER_OF_GROUP_1 + 1));
    }

    @Test
    public void testEditVGroupDataAndRemoveTagFromVGroup() {
        VGroup group = createGroup(EXISTING_GROUP_ID_1, NEW_GROUP_NAME, NEW_GROUP_DESCRIPTION);

        Group savedGroup = groupService.saveGroup(group);

        assertThat(groupService.getVGroups().size(), is(EXPECTED_GROUP_LIST_SIZE));
        assertThat(tagService.getTags().size(), is(TagFactory.TOTAL_TAGS_NUMBER));

        assertThat(savedGroup.getName(), is(NEW_GROUP_NAME));
        assertThat(savedGroup.getDescription(), is(NEW_GROUP_DESCRIPTION));

        List<Tag> groupTags = savedGroup.getTags();
        assertThat(groupTags, notNullValue());
        assertThat(groupTags.size(), is(TAG_NUMBER_OF_GROUP_1 - 1));
    }

    @Test
    public void testEditVGroupWithoutMandatoryFieldThrowInvalidInputParametersException() {
        VGroup group = createGroup(EXISTING_GROUP_ID_1, null, NEW_GROUP_DESCRIPTION);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        groupService.saveGroup(group);
    }

    @Test
    public void testEditExistingVGroupThrowInvalidInputParametersException() {
        VGroup group = createGroup(EXISTING_GROUP_ID_2, EXISTING_GROUP_NAME, NEW_GROUP_DESCRIPTION);

        rule.expect(DuplicateEntityException.class);
        rule.expectMessage(GROUP_ALREADY_EXISTS_ERROR_MESSAGE);
        groupService.saveGroup(group);
    }
    
    @Test
    public void testDeleteVGroup() {
    	groupService.deleteGroup(EXISTING_GROUP_ID_1);
    	
    	assertThat(groupService.getVGroups().size(), is(EXPECTED_GROUP_LIST_SIZE - 1));
    }
    
    @Test
    public void testDeleteVGroupNullIdThrowInvalidInputParametersException() {
    	rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(EXCEPTION_NOTHING_TO_DELETE);
    	
        groupService.deleteGroup(null);
    }
    
    @Test
    public void testDeleteVGroupNullIdThrowEntityNotFoundException() {
    	rule.expect(EntityNotFoundException.class);
        rule.expectMessage(GROUP_NOT_FOUND_ERROR_MESSAGE);
    	
        groupService.deleteGroup(NON_EXISTING_GROUP_ID);
    }
}
