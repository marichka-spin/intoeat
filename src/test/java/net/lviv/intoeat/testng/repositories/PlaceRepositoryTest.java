package net.lviv.intoeat.testng.repositories;

import net.lviv.intoeat.configuration.TestProfileConfig;
import net.lviv.intoeat.configuration.ValidationConfig;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.repositories.PlaceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestProfileConfig.class, ValidationConfig.class})
@Sql({"classpath:db/truncateSchema.sql", "classpath:db/places.sql"})
public class PlaceRepositoryTest {

    private static final String EXISTING_PLACE_NAME = "Place name 1";
    private static final String UNEXISTING_PLACE_NAME = "Unexisting Place name";
    private static final String EXISTING_TAG_NAME = "Tag 2";
    private static final String UNEXISTING_TAG_NAME = "Unexisting Tag";

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void testGetPlaceByNameOrTagWithExistingPlaceName() {
        List<Place> places = placeRepository.findPlaceByNameOrTag(EXISTING_PLACE_NAME);
        assertThat(places.size(), is(1));

        places = placeRepository.findPlaceByNameOrTag(EXISTING_PLACE_NAME.toUpperCase());
        assertThat(places.size(), is(1));

        places = placeRepository.findPlaceByNameOrTag(EXISTING_PLACE_NAME.toLowerCase());
        assertThat(places.size(), is(1));
    }

    @Test
    public void testGetPlaceByNameOrTagWithUnexistingPlaceName() {
        List<Place> places = placeRepository.findPlaceByNameOrTag(UNEXISTING_PLACE_NAME);
        assertThat(places.size(), is(0));

        places = placeRepository.findPlaceByNameOrTag(UNEXISTING_PLACE_NAME.toUpperCase());
        assertThat(places.size(), is(0));

        places = placeRepository.findPlaceByNameOrTag(UNEXISTING_PLACE_NAME.toLowerCase());
        assertThat(places.size(), is(0));
    }

    @Test
    public void testGetPlaceByNameOrTagWithExistingTagName() {
        List<Place> places = placeRepository.findPlaceByNameOrTag(EXISTING_TAG_NAME);
        assertThat(places.size(), is(2));

        places = placeRepository.findPlaceByNameOrTag(EXISTING_TAG_NAME.toUpperCase());
        assertThat(places.size(), is(2));

        places = placeRepository.findPlaceByNameOrTag(EXISTING_TAG_NAME.toLowerCase());
        assertThat(places.size(), is(2));
    }

    @Test
    public void testGetPlaceByNameOrTagWithUnexistingTagName() {
        List<Place> places = placeRepository.findPlaceByNameOrTag(UNEXISTING_TAG_NAME);
        assertThat(places.size(), is(0));

        places = placeRepository.findPlaceByNameOrTag(UNEXISTING_TAG_NAME.toUpperCase());
        assertThat(places.size(), is(0));

        places = placeRepository.findPlaceByNameOrTag(UNEXISTING_TAG_NAME.toLowerCase());
        assertThat(places.size(), is(0));
    }

    @Test
    public void testGetPlaceByNameOrTagWithNullValue() {
        List<Place> places = placeRepository.findPlaceByNameOrTag(null);
        assertThat(places.size(), is(0));
    }

    @Test
    public void testGetPlaceByNameOrTagWithEmptyValue() {
        List<Place> places = placeRepository.findPlaceByNameOrTag("");
        assertThat(places.size(), is(0));
    }

}
