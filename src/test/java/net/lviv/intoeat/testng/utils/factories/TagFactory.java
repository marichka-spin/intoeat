package net.lviv.intoeat.testng.utils.factories;

import net.lviv.intoeat.vmodels.VTag;

import java.util.HashMap;
import java.util.Map;

import static net.lviv.intoeat.testng.utils.TestUtils.UNEXISTING_ENTITY_ID;

public class TagFactory {

    public static final int TOTAL_TAGS_NUMBER = 4;
    public static final int EXISTING_TAG_ID_1 = 1;
    public static final int EXISTING_TAG_ID_2 = 2;
    public static final int TAG_WITHOUT_GROUP_ID = 4;
    public static final String EXISTING_TAG_1_NAME = "Tag 1";
    public static final String UNEXISTING_NAME = "unexisting-tag";
    public static final String UPDATED_TAG_NAME = "updated-tag";

    public static final String TEST_TAG_NAME = "test_tag";
    public static final String TEST_TAG_DESCRIPTION = "test_tag_description";

    public static final String TAG_ALREADY_EXISTS_ERROR_MESSAGE = String.format("Tag with name: %s already exists.", EXISTING_TAG_1_NAME);
    public static final String TAG_NOT_FOUND_ERROR_MESSAGE = String.format("Tag with id %d not found.", UNEXISTING_ENTITY_ID);
    public static final String TAG_BY_NAME_NOT_FOUND_ERROR_MESSAGE = String.format("Tag with name %s not found.", UNEXISTING_NAME);

    public static Map<String, Integer> getTagByNameDataProvider() {
        Map<String, Integer> dataProvider = new HashMap<>();
        dataProvider.put("Tag 1", 1);
        dataProvider.put("tag", 4);
        dataProvider.put("Tag", 4);
        return dataProvider;
    }

    public static VTag createTag(String name, String description) {
		return createTag(null, name, description);
	}

	public static VTag createTag(Integer id, String name, String description) {
        VTag tag = new VTag();
        if(id != null) {
            tag.id = id;
        }

        tag.name = name;
        tag.description = description;

        return tag;
	}

}
