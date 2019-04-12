package net.lviv.intoeat.testng.utils.factories;

import net.lviv.intoeat.vmodels.VGroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GroupFactory {

    public static final Integer EXPECTED_GROUP_LIST_SIZE = 3;
    public static final Integer EXISTING_GROUP_ID_1 = 1;
    public static final Integer EXISTING_GROUP_ID_2 = 2;
    public static final Integer NON_EXISTING_GROUP_ID = 55555;
    public static final String EXISTING_GROUP_NAME = "Group 1";
    public static final String EXISTING_GROUP_DESCRIPTION = "Group description 1";
    public static final String NON_EXISTING_GROUP_NAME = "Non existing group";
    public static final String UPDATED_GROUP_NAME = "updated-group_name";
    public static final String UPDATED_GROUP_DESCRIPTION = "Updated-group-description";

    public static final Integer TAG_NUMBER_OF_GROUP_1 = 1;
    public static final String NEW_GROUP_NAME = "New Group name";
    public static final String NEW_GROUP_DESCRIPTION = "New Group description";

    public static final String GROUP_ALREADY_EXISTS_ERROR_MESSAGE = String.format("Group with name: %s already exists.", EXISTING_GROUP_NAME);
    public static final String GROUP_NOT_FOUND_ERROR_MESSAGE = String.format("Group with id %s not found.", NON_EXISTING_GROUP_ID);
    public static final String GROUP_BY_NAME_NOT_FOUND_ERROR_MESSAGE = String.format("Group with name %s not found", NON_EXISTING_GROUP_NAME);
    public static final String EXCEPTION_MANDATORY_FIELD_IS_MISSING ="Mandatory parameter 'name' is missing.";
    public static final String EXCEPTION_NOTHING_TO_DELETE ="Object id is null. Nothing to delete.";

    public static Map<String, Integer> getGroupByNameDataProvider() {
        Map<String, Integer> dataProvider = new HashMap<>();
        dataProvider.put("Group 1", 1);
        dataProvider.put("group", 3);
        dataProvider.put("Group", 3);
        return dataProvider;
    }

	public static VGroup createGroup(String name, String description, Integer...tagsIds) {
		return createGroup(null, name, description, tagsIds);
	}
	
	public static VGroup createGroup(Integer id, String name, String description, Integer...tagsIds) {
        VGroup group = new VGroup();
		
		if(id != null) {
			group.id = id;
		}
		
		group.name = name;
		group.description = description;
		group.tagsIds = new HashSet<>(Arrays.asList(tagsIds));
		
		return group;
	}
}
