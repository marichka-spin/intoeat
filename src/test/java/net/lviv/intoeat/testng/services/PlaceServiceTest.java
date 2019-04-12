package net.lviv.intoeat.testng.services;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Contact;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.repositories.ContactRepository;
import net.lviv.intoeat.services.PlaceService;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.testng.utils.factories.TagFactory;
import net.lviv.intoeat.vmodels.VPlace;
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.lviv.intoeat.testng.utils.TestUtils.*;
import static net.lviv.intoeat.testng.utils.factories.ContactFactory.*;
import static net.lviv.intoeat.testng.utils.factories.PlaceFactory.*;
import static net.lviv.intoeat.testng.utils.factories.TagFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/places.sql"})
public class PlaceServiceTest {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ContactRepository contactRepository;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @Test
    public void testGetPlaces() {
        List<VPlace> places = placeService.getVPlaces();

        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
    }

    @Test
    public void testGetPlaceById() {
        VPlace place = placeService.getVPlaceById(EXISTING_PLACE_ID_1);

        assertThat(place, notNullValue());
        assertThat(place.id, is(EXISTING_PLACE_ID_1));
        assertThat(place.name, is(EXISTING_PLACE_NAME));
    }

    @Test
    public void testGetPlaceByUnexistingId() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(PLACE_NOT_FOUND_ERROR_MESSAGE);

        placeService.getVPlaceById(UNEXISTING_ENTITY_ID);
    }

    @Test
    public void testGetPlaceByNullId() {
        rule.expect(InvalidDataAccessApiUsageException.class);
        rule.expectMessage(NULL_ID_ERROR_MESSAGE);

        placeService.getVPlaceById(null);
    }

    @Test
    public void testGetPlaceByName() {
        VPlace place = placeService.getVPlaceByName(EXISTING_PLACE_NAME);

        assertThat(place, notNullValue());
        assertThat(place.id, is(EXISTING_PLACE_ID_1));
        assertThat(place.name, is(EXISTING_PLACE_NAME));
    }


    @Test
    public void testGetPlaceByUnexistingName() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(PLACE_BY_NAME_NOT_FOUND_ERROR_MESSAGE);

        placeService.getVPlaceByName(UNEXISTING_PLACE_NAME);
    }

    @Test
    public void testGetPlaceNullName() {
        rule.expect(EntityNotFoundException.class);

        placeService.getVPlaceByName(null);
    }

    @Test
    public void testGetPlaceEmptyName() {
        rule.expect(EntityNotFoundException.class);

        placeService.getVPlaceByName("");
    }

    @Test
    public void testGetPlaceByNameOrTagWithExistingName() {
        Map<String, Integer> dataProvider = getPlaceByNameDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String placeName = it.next();
            Integer expectedPlacesNumber = dataProvider.get(placeName);

            List<VPlace> places = placeService.getVPlaceByNameOrTag(placeName);

            assertThat(places.size(), is(expectedPlacesNumber));
        }
    }

    @Test
    public void testGetPlaceByNameOrTagWithExistingTag() {
        Map<String, Integer> dataProvider = getPlaceByTagDataProvider();
        Iterator<String> it = dataProvider.keySet().iterator();
        while (it.hasNext()) {
            String tag = it.next();
            Integer expectedPlacesNumber = dataProvider.get(tag);

            List<VPlace> places = placeService.getVPlaceByNameOrTag(tag);

            assertThat(places.size(), is(expectedPlacesNumber));
        }
    }

    @Test
    public void testGetPlaceByNameOrTagWithUnexistingName() {
        List<VPlace> places = placeService.getVPlaceByNameOrTag(UNEXISTING_PLACE_NAME);

        assertThat(places.size(), is(0));
    }

    @Test
    public void testGetPlaceByNameOrTagWithUnexistingTag() {
        List<VPlace> places = placeService.getVPlaceByNameOrTag(TagFactory.UNEXISTING_NAME);

        assertThat(places.size(), is(0));
    }

    @Test
    public void testGetPlaceByNameOrTagWithEmptyData() {
        List<VPlace> places = placeService.getVPlaceByNameOrTag("");

        assertThat(places.size(), is(TOTAL_PLACES_NUMBER));
    }

    @Test
    public void testGetPlaceByNameOrTagWithNullData() {
        List<VPlace> places = placeService.getVPlaceByNameOrTag(null);

        assertThat(places.size(), is(0));
    }

    @Test
    public void testAddPlaceWithExistingTags() {
        VPlace place = createPlace(NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, EXISTING_TAG_ID_1, EXISTING_TAG_ID_2);

        Place savedPlace = placeService.savePlace(place);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER + 1));
        assertThat(tagService.getTags().size(), is(TOTAL_TAGS_NUMBER));

        assertThat(savedPlace.getName(), is(NEW_PLACE_NAME));
        assertThat(savedPlace.getDescription(), is(NEW_PLACE_DESCRIPTION));

        List<Tag> placeTags = savedPlace.getTags();
        assertThat(placeTags, notNullValue());
        assertThat(placeTags.size(), is(TAG_NUMBER_OF_PLACE_1 + 1));
    }

    @Test
    public void testAddPlaceWithNewContact() {
        VPlace place = createPlaceWithContacts(NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, NEW_VCONTACT);

        Place savedPlace = placeService.savePlace(place);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER + 1));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER + 1));

        assertThat(savedPlace.getName(), is(NEW_PLACE_NAME));
        assertThat(savedPlace.getDescription(), is(NEW_PLACE_DESCRIPTION));

        List<Contact> placeContacts = savedPlace.getContacts();
        assertThat(placeContacts, notNullValue());
        assertThat(placeContacts.size(), is(CONTACTS_NUMBER_OF_PLACE_1));
        Contact placeContact = placeContacts.get(0);
        assertThat(placeContact.getAddress(), is(NEW_CONTACT_ADDRESS));
        assertThat(placeContact.getEmail(), is(NEW_CONTACT_EMAIL));
    }

    @Test
    public void testAddPlaceWithExistingName() throws Exception {
        VPlace place = createPlace(EXISTING_PLACE_NAME, NEW_PLACE_DESCRIPTION);

        rule.expect(DuplicateEntityException.class);
        rule.expectMessage(PLACE_ALREADY_EXISTS_ERROR_MESSAGE);
        placeService.savePlace(place);
    }

    @Test
    public void testAddPlaceWithNullData() throws Exception {
        VPlace place = createPlace(null, NEW_PLACE_DESCRIPTION);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        placeService.savePlace(place);
    }

    @Test
    public void testAddPlaceWithEmptyData() throws Exception {
        VPlace place = createPlace("", NEW_PLACE_DESCRIPTION);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        placeService.savePlace(place);
    }

    @Test
    public void testEditPlaceDataAndAddingExistingTag() {
        VPlace place = createPlace(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, EXISTING_TAG_ID_1, EXISTING_TAG_ID_2);

        Place savedPlace = placeService.savePlace(place);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER));
        assertThat(tagService.getTags().size(), is(TOTAL_TAGS_NUMBER));

        assertThat(savedPlace.getName(), is(NEW_PLACE_NAME));
        assertThat(savedPlace.getDescription(), is(NEW_PLACE_DESCRIPTION));

        List<Tag> placeTags = savedPlace.getTags();
        assertThat(placeTags, notNullValue());
        assertThat(placeTags.size(), is(TAG_NUMBER_OF_PLACE_1 + 1));
    }

    @Test
    public void testEditPlaceDataAndRemoveTagFromPlace() {
        VPlace place = createPlace(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION);

        Place savedPlace = placeService.savePlace(place);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER));
        assertThat(tagService.getTags().size(), is(TOTAL_TAGS_NUMBER));

        assertThat(savedPlace.getName(), is(NEW_PLACE_NAME));
        assertThat(savedPlace.getDescription(), is(NEW_PLACE_DESCRIPTION));

        List<Tag> placeTags = savedPlace.getTags();
        assertThat(placeTags, notNullValue());
        assertThat(placeTags.size(), is(TAG_NUMBER_OF_PLACE_1 - 1));
    }

    @Test
    public void testEditPlaceDataAndAddingNewContact() {
        VPlace place = createPlaceWithContacts(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, EXISTING_VCONTACT_1, NEW_VCONTACT);

        Place savedPlace = placeService.savePlace(place);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER + 1));

        assertThat(savedPlace.getName(), is(NEW_PLACE_NAME));
        assertThat(savedPlace.getDescription(), is(NEW_PLACE_DESCRIPTION));

        List<Contact> placeContacts = savedPlace.getContacts();
        assertThat(placeContacts, notNullValue());
        assertThat(placeContacts.size(), is(CONTACTS_NUMBER_OF_PLACE_1 + 1));
        Contact placeContact = placeContacts.get(1);
        assertThat(placeContact.getAddress(), is(NEW_CONTACT_ADDRESS));
        assertThat(placeContact.getEmail(), is(NEW_CONTACT_EMAIL));
    }

    @Test
    public void testEditPlaceUpdatingExistingContacts() {
        VPlace place = createPlaceWithContacts(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION, UPDATED_VCONTACT_1);

        Place savedPlace = placeService.savePlace(place);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER));

        assertThat(savedPlace.getName(), is(NEW_PLACE_NAME));
        assertThat(savedPlace.getDescription(), is(NEW_PLACE_DESCRIPTION));

        List<Contact> placeContacts = savedPlace.getContacts();
        assertThat(placeContacts, notNullValue());
        assertThat(placeContacts.size(), is(CONTACTS_NUMBER_OF_PLACE_1));
        Contact placeContact = placeContacts.get(0);
        assertThat(placeContact.getAddress(), is(NEW_CONTACT_ADDRESS));
        assertThat(placeContact.getEmail(), is(NEW_CONTACT_EMAIL));
    }

    @Test
    public void testEditPlaceRemovingContactFromPlace() {
        VPlace place = createPlaceWithContacts(EXISTING_PLACE_ID_1, NEW_PLACE_NAME, NEW_PLACE_DESCRIPTION);

        Place savedPlace = placeService.savePlace(place);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER));
        assertThat(contactRepository.findAll().size(), is(TOTAL_CONTACTS_NUMBER - 1));

        assertThat(savedPlace.getName(), is(NEW_PLACE_NAME));
        assertThat(savedPlace.getDescription(), is(NEW_PLACE_DESCRIPTION));

        List<Contact> placeContacts = savedPlace.getContacts();
        assertThat(placeContacts, notNullValue());
        assertThat(placeContacts.size(), is(0));
    }

    @Test
    public void testEditPlaceWithExistingName() throws Exception {
        VPlace place = createPlace(EXISTING_PLACE_ID_2, EXISTING_PLACE_NAME, UPDATED_PLACE_DESCRIPTION);

        rule.expect(DuplicateEntityException.class);
        rule.expectMessage(PLACE_ALREADY_EXISTS_ERROR_MESSAGE);
        placeService.savePlace(place);
    }

    @Test
    public void testEditPlaceWithNullData() throws Exception {
        VPlace place = createPlace(EXISTING_PLACE_ID_1, null, UPDATED_PLACE_DESCRIPTION);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        placeService.savePlace(place);
    }

    @Test
    public void testEditPlaceWithEmptyData() throws Exception {
        VPlace place = createPlace(EXISTING_PLACE_ID_1, "", UPDATED_PLACE_DESCRIPTION);

        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MESSAGE);
        placeService.savePlace(place);
    }

    @Test
    public void testDeletePlace() {
        placeService.deletePlace(EXISTING_PLACE_ID_1);

        assertThat(placeService.getVPlaces().size(), is(TOTAL_PLACES_NUMBER - 1));
    }

    @Test
    public void testDeletePlaceByUnexistingId() {
        rule.expect(EntityNotFoundException.class);
        rule.expectMessage(PLACE_NOT_FOUND_ERROR_MESSAGE);

        placeService.deletePlace(UNEXISTING_ENTITY_ID);
    }

    @Test
    public void testDeletePlaceByNullId() {
        rule.expect(InvalidInputParametersException.class);
        rule.expectMessage(NOTHING_TO_DELETE_ERROR_MESSAGE);

        placeService.deletePlace(null);
    }

}
