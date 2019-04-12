package net.lviv.intoeat.services;

import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.vmodels.VPlace;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceService {

    public VPlace getVPlaceById(Integer id);
    public VPlace getVPlaceByName(String name);
    public List<VPlace> getVPlaceByNameOrTag(String arg);
    public List<VPlace> getVPlaces();
    public List<VPlace> getVPlaces(Pageable pageable);

    public VPlace getVPlaceByIdAdmin(Integer id);
    public VPlace getVPlaceByNameAdmin(String name);
    public List<VPlace> getVPlaceByNameOrTagAdmin(String arg);
    public List<VPlace> getVPlacesAdmin();
    public List<VPlace> getVPlacesAdmin(Pageable pageable);
    public Place savePlace(VPlace place);
    public void deletePlace(Integer id);
}
