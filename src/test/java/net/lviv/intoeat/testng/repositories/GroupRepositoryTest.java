package net.lviv.intoeat.testng.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static net.lviv.intoeat.testng.utils.factories.GroupFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.repositories.GroupRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/group_tag.sql"})
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void testFindGroupByExistingName() {
        Group group = groupRepository.findByName(EXISTING_GROUP_NAME);
        assertThat(group, notNullValue());
    }

    @Test
    public void testFindGroupByUnexistingName() {
        Group group = groupRepository.findByName(NON_EXISTING_GROUP_NAME);
        assertThat(group, nullValue());
    }

    @Test
    public void testFindGroupByEmptyName() {
        Group group = groupRepository.findByName("");
        assertThat(group, nullValue());
    }

    @Test
    public void testFindGroupByNullName() {
        Group group = groupRepository.findByName(null);
        assertThat(group, nullValue());
    }

}
