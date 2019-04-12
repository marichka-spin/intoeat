package net.lviv.intoeat.testng.services;


import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.testng.utils.factories.GroupFactory;
import net.lviv.intoeat.vmodels.VTag;
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

import static net.lviv.intoeat.testng.utils.TestUtils.*;
import static net.lviv.intoeat.testng.utils.factories.TagFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/group_tag.sql"})
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @Test
    public void testGetTags() {
        List<Tag> tags = tagService.getTags();

        assertThat(tags.size(), is(TOTAL_TAGS_NUMBER));
    }

    @Test
    public void testGetTagById() {
        VTag tag = tagService.getVTagById(EXISTING_TAG_ID_1);

        assertThat(tag, notNullValue());
        assertThat(tag.id, is(EXISTING_TAG_ID_1));
        assertThat(tag.name, is(EXISTING_TAG_1_NAME));
    }

    @Test
    public void testGetTagByUnexistingId() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(TAG_NOT_FOUND_ERROR_MESSAGE);

        tagService.getVTagById(UNEXISTING_ENTITY_ID);
    }

    @Test
    public void testGetTagByNullId() {
        rule.expect(InvalidDataAccessApiUsageException.class);
        rule.expectMessage(NULL_ID_ERROR_MESSAGE);

        tagService.getVTagById(null);
    }

    @Test
    public void testGetTagByName() {
        VTag tag = tagService.getVTagByName(EXISTING_TAG_1_NAME);

        assertThat(tag, notNullValue());
        assertThat(tag.id, is(EXISTING_TAG_ID_1));
        assertThat(tag.name, is(EXISTING_TAG_1_NAME));
    }

    @Test
    public void testGetTagByUnexistingName() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(TAG_BY_NAME_NOT_FOUND_ERROR_MESSAGE);

        tagService.getVTagByName(UNEXISTING_NAME);
    }

    @Test
    public void testGetTagNullName() {
        rule.expect(EntityNotFoundException.class);

        tagService.getVTagByName(null);
    }

    @Test
    public void testGetTagEmptyName() {
        rule.expect(EntityNotFoundException.class);

        tagService.getVTagByName("");
    }

    @Test
    public void testAddTag() {
        VTag tag = createTag(TEST_TAG_NAME , TEST_TAG_DESCRIPTION);

        tagService.saveTag(tag);
        assertThat(tagService.getTags().size(), is(TOTAL_TAGS_NUMBER + 1));
    }

    @Test
    public void testAddTagWithExistingName() throws Exception {
        VTag tag = createTag(EXISTING_TAG_1_NAME, TEST_TAG_DESCRIPTION);

        rule.expect(DuplicateEntityException.class);
        rule.expectMessage(TAG_ALREADY_EXISTS_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testAddTagWithNullData() throws Exception {
        VTag tag = createTag(null, null);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testAddTagWithEmptyData() throws Exception {
        VTag tag = createTag("", "");

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testAddTagWithNullName() throws Exception {
        VTag tag = createTag(null, TEST_TAG_DESCRIPTION);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testEditTag() {
        VTag tag = createTag(EXISTING_TAG_ID_1, UPDATED_TAG_NAME, TEST_TAG_DESCRIPTION);

        Tag tagFromDB = tagService.saveTag(tag);
        assertThat(tagFromDB.getName(), is(UPDATED_TAG_NAME));
    }

    @Test
    public void testEditTagWithExistingName() throws Exception {
        VTag tag = createTag(EXISTING_TAG_ID_2, EXISTING_TAG_1_NAME, TEST_TAG_DESCRIPTION);

        rule.expect(DuplicateEntityException.class);
        rule.expectMessage(TAG_ALREADY_EXISTS_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testEditTagWithNullData() throws Exception {
        VTag tag = createTag(EXISTING_TAG_ID_1, null, null);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testEditTagWithEmptyData() throws Exception {
        VTag tag = createTag(EXISTING_TAG_ID_1, "", "");

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testEditTagWithNullName() throws Exception {
        VTag tag = createTag(EXISTING_TAG_ID_1, null, TEST_TAG_DESCRIPTION);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        tagService.saveTag(tag);
    }

    @Test
    public void testDeleteTag() {
        tagService.deleteTag(EXISTING_TAG_ID_1);

        assertThat(tagService.getTags().size(), is(TOTAL_TAGS_NUMBER - 1));
    }

    @Test
    public void testDeleteTagByUnexistingId() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(TAG_NOT_FOUND_ERROR_MESSAGE);

        tagService.deleteTag(UNEXISTING_ENTITY_ID);
    }

    @Test
    public void testDeleteTagByNullId() {
        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(NOTHING_TO_DELETE_ERROR_MESSAGE);

        tagService.deleteTag(null);
    }
}
