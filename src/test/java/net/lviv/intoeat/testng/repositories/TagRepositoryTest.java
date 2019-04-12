package net.lviv.intoeat.testng.repositories;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.repositories.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static net.lviv.intoeat.testng.utils.factories.TagFactory.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/group_tag.sql"})
public class TagRepositoryTest {

    @Autowired
    private TagRepository userRepository;

    @Test
    public void testFindName() {
        Tag tag = userRepository.findByName(EXISTING_TAG_1_NAME);
        assertThat(tag, notNullValue());
        assertThat(tag.getName(), is(EXISTING_TAG_1_NAME));
    }

    @Test
    public void testFindByUnexistingTagName() {
        Tag tag = userRepository.findByName(UNEXISTING_NAME);
        assertThat(tag, nullValue());
    }

    @Test
    public void testFindByNullTagName() {
        Tag tag = userRepository.findByName(null);
        assertThat(tag, nullValue());
    }

    @Test
    public void testFindByEmptyTagName() {
        Tag tag = userRepository.findByName("");
        assertThat(tag, nullValue());
    }
}
