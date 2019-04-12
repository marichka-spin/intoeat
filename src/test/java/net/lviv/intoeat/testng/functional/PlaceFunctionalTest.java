package net.lviv.intoeat.testng.functional;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.configuration.WebAppConfig;
import net.lviv.intoeat.repositories.ContactRepository;
import net.lviv.intoeat.services.PlaceService;
import net.lviv.intoeat.vmodels.VPlace;
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
import static net.lviv.intoeat.testng.utils.factories.ContactFactory.*;
import static net.lviv.intoeat.testng.utils.factories.PlaceFactory.*;
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
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/places.sql"})
public class PlaceFunctionalTest {

    private static final String GET_PLACE_ENDPOINT_PATH = "/place";
    private static final String SEARCH_PLACE_ENDPOINT_PATH = "/place/search";
    private static final String SAVE_PLACE_ENDPOINT_PATH = "/admin/place/save";
    private static final String REMOVE_PLACE_ENDPOINT_PATH = "/admin/place/remove";

    private static final String ADMIN_ENDPOINT_PATH = "/admin";

    private static final String SEARCH_PARAMETER = "arg";


    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ContactRepository contactRepository;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testGetVPlaceByName() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_PLACE_ENDPOINT_PATH).param("name", EXISTING_PLACE_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_PLACE_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_PLACE_NAME)));
    }

    @Test
    public void testGetVPlaceByInvalidName() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_PLACE_ENDPOINT_PATH).param("name", UNEXISTING_PLACE_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(PLACE_BY_NAME_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetVPlaceById() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_PLACE_ENDPOINT_PATH).param("id", "1").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_PLACE_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_PLACE_NAME)));
    }

    @Test
    public void testGetVPlaceByInvalidId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_PLACE_ENDPOINT_PATH).param("id", String.valueOf(UNEXISTING_ENTITY_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(PLACE_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetVPlaceByEmptyId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_PLACE_ENDPOINT_PATH).param("id", "").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(NULL_ID_ERROR_MESSAGE)));
    }

    @Test
    public void testGetVPlaceByNameOrTagWithExistingName() throws Exception {
        Map<String, Integer> dataProvider = getPlaceByNameDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String placeName = it.next();
            Integer expectedVPlacesNumber = dataProvider.get(placeName);
            ResultActions resultActions = mockMvc.perform(
                    get(SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, placeName)
                            .accept(APPLICATION_JSON));
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedVPlacesNumber)));
        }
    }

    @Test
    public void testGetVPlaceByNameOrTagWithExistingTag() throws Exception {
        Map<String, Integer> dataProvider = getPlaceByTagDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String tag = it.next();
            Integer expectedVPlacesNumber = dataProvider.get(tag);
            ResultActions resultActions = mockMvc.perform(
                    get(SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, tag)
                            .accept(APPLICATION_JSON));
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedVPlacesNumber)));
        }
    }

    @Test
    public void testGetVPlaceByNameOrTagWithUnexistingName() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, UNEXISTING_PLACE_NAME)
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetVPlaceByNameOrTagWithUnexistingTag() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, UNEXISTING_NAME)
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetVPlaceByNameOrTagWithEmptyData() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, "")
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(TOTAL_PLACES_NUMBER)));
    }

    @Test
    public void testGetAllVPlaces() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_PLACE_ENDPOINT_PATH + "/all").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(TOTAL_PLACES_NUMBER)));
    }

    @Test
    public void testGetAllVPlacesPageable() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(GET_PLACE_ENDPOINT_PATH + "/pages")
                        .param("page", "0")
                        .param("size", "2")
//                        .param("sort", "name,DESC") TODO just for example
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(TOTAL_PLACES_NUMBER - 1)));
    }

    @Test
    public void testGetVPlaceByNameAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_PLACE_ENDPOINT_PATH).param("name", EXISTING_PLACE_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_PLACE_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_PLACE_NAME)))
                .andExpect(jsonPath("$.contacts", hasSize(1)))
                .andExpect(jsonPath("$.tagsIds", hasSize(1)));
    }

    @Test
    public void testGetVPlaceByInvalidNameAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_PLACE_ENDPOINT_PATH).param("name", UNEXISTING_PLACE_NAME).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(PLACE_BY_NAME_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetVPlaceByIdAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_PLACE_ENDPOINT_PATH).param("id", "1").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(EXISTING_PLACE_ID_1)))
                .andExpect(jsonPath("$.name", is(EXISTING_PLACE_NAME)))
                .andExpect(jsonPath("$.contacts", hasSize(1)))
                .andExpect(jsonPath("$.tagsIds", hasSize(1)));
    }

    @Test
    public void testGetVPlaceByInvalidIdAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_PLACE_ENDPOINT_PATH).param("id", String.valueOf(UNEXISTING_ENTITY_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is(PLACE_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testGetVPlaceByEmptyIdAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_PLACE_ENDPOINT_PATH).param("id", "").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(NULL_ID_ERROR_MESSAGE)));
    }

    @Test
    public void testGetVPlaceByNameOrTagWithExistingNameAdmin() throws Exception {
        Map<String, Integer> dataProvider = getPlaceByNameDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String placeName = it.next();
            Integer expectedVPlacesNumber = dataProvider.get(placeName);
            ResultActions resultActions = mockMvc.perform(
                    get(ADMIN_ENDPOINT_PATH + SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, placeName)
                            .accept(APPLICATION_JSON));
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedVPlacesNumber)));
        }
    }

    @Test
    public void testGetVPlaceByNameOrTagWithExistingTagAdmin() throws Exception {
        Map<String, Integer> dataProvider = getPlaceByTagDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String tag = it.next();
            Integer expectedVPlacesNumber = dataProvider.get(tag);
            ResultActions resultActions = mockMvc.perform(
                    get(ADMIN_ENDPOINT_PATH + SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, tag)
                            .accept(APPLICATION_JSON));
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(expectedVPlacesNumber)));
        }
    }

    @Test
    public void testGetVPlaceByNameOrTagWithUnexistingNameAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, UNEXISTING_PLACE_NAME)
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetVPlaceByNameOrTagWithUnexistingTagAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, UNEXISTING_NAME)
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetVPlaceByNameOrTagWithEmptyDataAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + SEARCH_PLACE_ENDPOINT_PATH).param(SEARCH_PARAMETER, "")
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(TOTAL_PLACES_NUMBER)));
    }

    @Test
    public void testGetAllVPlacesAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_PLACE_ENDPOINT_PATH + "/all").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(TOTAL_PLACES_NUMBER)));
    }

    @Test
    public void testGetAllVPlacesPageableAdmin() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get(ADMIN_ENDPOINT_PATH + GET_PLACE_ENDPOINT_PATH + "/pages")
                        .param("page", "0")
                        .param("size", "2")
//                        .param("sort", "name,DESC") TODO just for example
                        .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(TOTAL_PLACES_NUMBER - 1)));
    }

    @Test
    public void testAddVPlaceWithExistingTags() throws Exception {
        String jsonVPlace = convertToJson(createPlace(NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, EXISTING_TAG_ID_1, EXISTING_TAG_ID_2));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER + 1));
    }

    @Test
    public void testAddVPlaceWithNewContact() throws Exception {
        String jsonVPlace = convertToJson(createPlaceWithContacts(NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, NEW_VCONTACT));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER + 1));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER + 1));
    }

    @Test
    public void testAddVPlaceWithEmptyData() throws Exception {
        String jsonVPlace = convertToJson(createPlace("", NEW_PLACE_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE)));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
    }

    @Test
    public void testAddVPlaceWithExistingName() throws Exception {
        String jsonVPlace = convertToJson(createPlace(EXISTING_PLACE_NAME, NEW_PLACE_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(PLACE_ALREADY_EXISTS_ERROR_MESSAGE)));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
    }

    @Test
    public void testEditVPlaceDataAndAddingExistingTag() throws Exception {
        int tagsNumberBefore = placeService.getVPlaceByIdAdmin(EXISTING_PLACE_ID_1).tagsIds.size();
        String jsonVPlace = convertToJson(createPlace(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, EXISTING_TAG_ID_1, EXISTING_TAG_ID_2));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(EXISTING_PLACE_ID_1)));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
        VPlace place = placeService.getVPlaceByIdAdmin(EXISTING_PLACE_ID_1);
        assertThat(place.tagsIds.size(), is(tagsNumberBefore + 1));
    }

    @Test
    public void testEditVPlaceWithExistingName() throws Exception {
        String jsonVPlace = convertToJson(createPlace(EXISTING_PLACE_ID_2, EXISTING_PLACE_NAME, UPDATED_PLACE_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(PLACE_ALREADY_EXISTS_ERROR_MESSAGE)));
    }

    @Test
    public void testEditVPlaceWithEmptyData() throws Exception {
        String jsonVPlace = convertToJson(createPlace(EXISTING_PLACE_ID_1, null, UPDATED_PLACE_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE)));
    }

    @Test
    public void testEditVPlaceDataAndAddingNewContact() throws Exception {
        String jsonVPlace = convertToJson(createPlaceWithContacts(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, EXISTING_VCONTACT_1, NEW_VCONTACT));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER + 1));
    }

    @Test
    public void testEditVPlaceAddingExistingContacts() throws Exception {
        String jsonVPlace = convertToJson(createPlaceWithContacts(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, EXISTING_VCONTACT_1, EXISTING_VCONTACT_2));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER));
    }

    @Test
    public void testEditVPlaceUpdatingExistingContacts() throws Exception {
        String jsonVPlace = convertToJson(createPlaceWithContacts(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, UPDATED_VCONTACT_1));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER));
    }

    @Test
    public void testEditVPlaceRemovingContactFromVPlace() throws Exception {
        String jsonVPlace = convertToJson(createPlaceWithContacts(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION));
        ResultActions resultActions = mockMvc.perform(
                post(SAVE_PLACE_ENDPOINT_PATH).accept(APPLICATION_JSON_UTF8).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonVPlace));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", notNullValue()));

        List<VPlace> places = placeService.getVPlaces();
        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER - 1));
    }

    @Test
    public void testDeleteVPlace() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_PLACE_ENDPOINT_PATH).param("id", String.valueOf(EXISTING_PLACE_ID_1)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.objectId", is(EXISTING_PLACE_ID_1)));
    }

    @Test
    public void testDeleteVPlaceByInvalidVPlaceId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_PLACE_ENDPOINT_PATH).param("id", String.valueOf(UNEXISTING_ENTITY_ID)).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(PLACE_NOT_FOUND_ERROR_MESSAGE)));
    }

    @Test
    public void testDeleteVPlaceByEmptyVPlaceId() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                delete(REMOVE_PLACE_ENDPOINT_PATH).param("id", "").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(JSON_MIME_TYPE))
                .andExpect(jsonPath("$.message", is(NOTHING_TO_DELETE_ERROR_MESSAGE)));
    }
}
