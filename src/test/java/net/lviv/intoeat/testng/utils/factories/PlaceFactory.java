package net.lviv.intoeat.testng.utils.factories;

import net.lviv.intoeat.vmodels.VContact;
import net.lviv.intoeat.vmodels.VPlace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static net.lviv.intoeat.testng.utils.TestUtils.UNEXISTING_ENTITY_ID;

public class PlaceFactory {

    public static final int TOTAL_PLACES_NUMBER = 3;
    public static final int EXISTING_PLACE_ID_1 = 1;
    public static final int EXISTING_PLACE_ID_2 = 2;
    public static final String EXISTING_PLACE_NAME = "Place name 1";
    public static final String UPDATED_PLACE_DESCRIPTION = "updated-place-description";
    public static final String UNEXISTING_PLACE_NAME = "unexisting-place";

    public static final Integer TAG_NUMBER_OF_PLACE_1 = 1;
    public static final String NEW_PLACE_NAME = "New place name";
    public static final String NEW_PLACE_DESCRIPTION = "New place name";

    public static final String PLACE_ALREADY_EXISTS_ERROR_MESSAGE = String.format("Place with name: %s already exists.", EXISTING_PLACE_NAME);
    public static final String PLACE_NOT_FOUND_ERROR_MESSAGE = String.format("Place with id %d not found.", UNEXISTING_ENTITY_ID);
    public static final String PLACE_BY_NAME_NOT_FOUND_ERROR_MESSAGE = String.format("Place with name %s not found", UNEXISTING_PLACE_NAME);

    public static Map<String, Integer> getPlaceByNameDataProvider() {
        Map<String, Integer> dataProvider = new HashMap<>();
        dataProvider.put("Place name 1", 1);
        dataProvider.put("Place name", 3);
        dataProvider.put("Place", 3);
        return dataProvider;
    }

    public static Map<String, Integer> getPlaceByTagDataProvider() {
        Map<String, Integer> dataProvider = new HashMap<>();
        dataProvider.put("Tag 2", 2);
        dataProvider.put("Tag 3", 1);
        dataProvider.put("Tag", 3);
        return dataProvider;
    }

    public static VPlace createPlace(String name, String description, Integer...tagsIds) {
        return createPlace(null, name, description, tagsIds);
    }

    public static VPlace createPlace(Integer id, String name, String description, Integer...tagsIds) {
        VPlace place = new VPlace();
        if (id != null) {
            place.id = id;
        }
        place.name = name;
        place.description = description;
        place.tagsIds = new HashSet<>(Arrays.asList(tagsIds));
        return place;
    }

    public static VPlace createPlaceWithContacts(String name, String description, VContact ...contacts) {
        return createPlaceWithContacts(null, name, description, contacts);
    }

    public static VPlace createPlaceWithContacts(Integer id, String name, String description, VContact...contacts) {
        VPlace place = new VPlace();
        if (id != null) {
            place.id = id;
        }
        place.name = name;
        place.description = description;
        place.contacts = Arrays.asList(contacts);
        return place;
    }

}
