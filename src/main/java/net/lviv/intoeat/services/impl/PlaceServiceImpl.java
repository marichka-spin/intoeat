package net.lviv.intoeat.services.impl;

import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.models.Contact;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.repositories.PlaceRepository;
import net.lviv.intoeat.services.PlaceService;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.utils.IntoeatUtils;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import net.lviv.intoeat.vmodels.VContact;
import net.lviv.intoeat.vmodels.VPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private Environment environment;
    
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private BaseValidator<Place> placeValidator;

    @Override
    @Transactional(readOnly = true)
    public VPlace getVPlaceById(Integer id) {
        Place place = placeRepository.findOne(id);
        if (place != null) {
            return getTransformed(place);
        } else {
            throw new EntityNotFoundException(RemovalValidator.ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, placeValidator.type(), id);
        }
    }

    @Override
    public VPlace getVPlaceByName(String name) {
        Place place = placeRepository.findByName(name);
        if(place != null){
            return getTransformed(place);
        } else {
            throw new EntityNotFoundException("Place with name %s not found", name);
        }
    }

    @Override
    public List<VPlace> getVPlaceByNameOrTag(String arg) {
        List<Place> places = placeRepository.findPlaceByNameOrTag("%" + arg + "%");
        List<VPlace> vPlaces = new LinkedList<>();
        for (Place place : places) {
            vPlaces.add(getTransformed(place));
        }
        return vPlaces;
    }

    @Override
    public List<VPlace> getVPlaces() {
        List<Place> places = placeRepository.findAll(IntoeatUtils.SORT_BY_NAME_ASC);
        List<VPlace> vPlaces = new LinkedList<>();
        for (Place place : places) {
            vPlaces.add(getTransformed(place));
        }
        return vPlaces;
    }

    @Override
    public List<VPlace> getVPlaces(Pageable pageable) {
        List<Place> places = placeRepository.findAll(pageable).getContent();
        List<VPlace> vPlaces = new LinkedList<>();
        for (Place place : places) {
            vPlaces.add(getTransformedForAdmin(place));
        }
        return vPlaces;
    }

    @Override
    public List<VPlace> getVPlacesAdmin() {
        List<Place> places = placeRepository.findAll(IntoeatUtils.SORT_BY_NAME_ASC);
        List<VPlace> vPlaces = new LinkedList<>();
        for (Place place : places) {
            vPlaces.add(getTransformedForAdmin(place));
        }
        return vPlaces;
    }

    @Override
    public List<VPlace> getVPlacesAdmin(Pageable pageable) {
        List<Place> places = placeRepository.findAll(pageable).getContent();
        List<VPlace> vPlaces = new LinkedList<>();
        for (Place place : places) {
            vPlaces.add(getTransformedForAdmin(place));
        }
        return vPlaces;
    }

    @Override
    @Transactional(readOnly = true)
    public VPlace getVPlaceByIdAdmin(Integer id) {
        Place place = placeRepository.findOne(id);
        if (place != null) {
            return getTransformedForAdmin(place);
        } else {
            throw new EntityNotFoundException(RemovalValidator.ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, placeValidator.type(), id);
        }
    }

    @Override
    public VPlace getVPlaceByNameAdmin(String name) {
        Place place = placeRepository.findByName(name);
        if(place != null){
            return getTransformedForAdmin(place);
        } else {
            throw new EntityNotFoundException("Place with name %s not found", name);
        }
    }

    @Override
    public List<VPlace> getVPlaceByNameOrTagAdmin(String arg) {
        List<Place> places = placeRepository.findPlaceByNameOrTag("%" + arg + "%");
        List<VPlace> vPlaces = new LinkedList<>();
        for (Place place : places) {
            vPlaces.add(getTransformedForAdmin(place));
        }
        return vPlaces;
    }

    @Override
    @Transactional
    public Place savePlace(VPlace vplace) {
        Place place = VPlace.transformAdmin(vplace);
        placeValidator.validate(place);
        place.setTags(populateTags(vplace));
        place.setContacts(populateContacts(vplace));
        return placeRepository.save(place);
    }

    @Override
    public void deletePlace(Integer id) {
        ((RemovalValidator)placeValidator).validate(id);
        placeRepository.delete(id);
    }

    private VPlace getTransformed(Place place) {
        VPlace vPlace = VPlace.transform(place);
        vPlace.baseDir = getImagesLocation();
        return vPlace;
    }

    private VPlace getTransformedForAdmin(Place place) {
        VPlace vPlace = VPlace.transformAdmin(place);
        vPlace.baseDir = getImagesLocation();
        return vPlace;
    }

    private List<Contact> populateContacts(VPlace vPlace) {
        List<Contact> contacts = new ArrayList<>();
        if (vPlace.contacts != null) {
            for (VContact vContact : vPlace.contacts) {
                Contact contactToAdd = VContact.transformAdmin(vContact);
                contacts.add(contactToAdd);
            }
        }
        return contacts;
    }

    private List<Tag> populateTags(VPlace vPlace) {
        List<Tag> tags = new ArrayList<>();
        if (vPlace.tagsIds != null) {
            for (Tag tag : tagService.getTags()) {
                if (vPlace.tagsIds.contains(tag.getId())) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    private String getImagesLocation() {
        String activeProfile = "dev";
        if (environment.getActiveProfiles().length > 0) {
            activeProfile = environment.getActiveProfiles()[0];
        }
        String assestsDir = "assets/pictures";
        String location = environment.getProperty(activeProfile + ".images.location", assestsDir);
        return location;
    }

}
