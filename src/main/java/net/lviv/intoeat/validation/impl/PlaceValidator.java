package net.lviv.intoeat.validation.impl;

import net.lviv.intoeat.exceptions.DuplicateEntityException;
import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.repositories.PlaceRepository;
import net.lviv.intoeat.utils.IntoeatUtils;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import org.apache.commons.lang3.StringUtils;

public class PlaceValidator implements BaseValidator<Place>, RemovalValidator {

    private static final String TYPE_CLASS = IntoeatUtils.getGenericType(PlaceValidator.class).getSimpleName();

    private PlaceRepository placeRepository;

    public PlaceValidator(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public void validate(Place place) {
        if (StringUtils.isEmpty(place.getName())) {
            throw new InvalidInputParametersException(MANDATORY_PARAMETER_NAME_MISSING_ERROR_MSG);
        }

        Place existingPlace = placeRepository.findByName(place.getName());
        if (existingPlace != null && (place.getId() == null || !existingPlace.getId().equals(place.getId()))) {
            throw new DuplicateEntityException(ENTITY_WITH_NAME_EXISTS_ERROR_MSG, TYPE_CLASS, place.getName());
        }
    }

    @Override
    public void validate(Integer id) {
        if (id == null) {
            throw new InvalidInputParametersException(NOTHING_TO_DELETE_ERROR_MSG);
        }

        if (!placeRepository.exists(id)) {
            throw new EntityNotFoundException(ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, TYPE_CLASS, id);
        }
    }

    @Override
    public String type() {
        return TYPE_CLASS;
    }
}
