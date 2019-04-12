package net.lviv.intoeat.controllers;

import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.services.PlaceService;
import net.lviv.intoeat.utils.ResponseInfo;
import net.lviv.intoeat.vmodels.VPlace;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlaceController {

    private static final int PAGE_LIMIT = 10;

    @Autowired
    private PlaceService placeService;

    @RequestMapping("/place")
    @ResponseBody
    public VPlace getPlace(@RequestParam(value = "id", required = false) Integer id,
                          @RequestParam(value = "name", required = false) String name) {
        if (id != null) {
            return placeService.getVPlaceById(id);
        } else if (!StringUtils.isEmpty(name)) {
            return placeService.getVPlaceByName(name);
        } else {
            throw new InvalidInputParametersException("The given id must not be null!");
        }

    }

    @RequestMapping("/place/all")
    @ResponseBody
    public List<VPlace> getPlaces(){
        return placeService.getVPlaces();
    }

    @RequestMapping("/place/pages")
    @ResponseBody
    public List<VPlace> getPlaces(@PageableDefault(size = PAGE_LIMIT) Pageable page){
        return placeService.getVPlaces(page);
    }

    @RequestMapping("/place/search")
    @ResponseBody
    public List<VPlace> getPlacesByNameOrTag(@RequestParam(value = "arg") String arg){

        return placeService.getVPlaceByNameOrTag(arg);
    }

    @RequestMapping("/admin/place")
    @ResponseBody
    public VPlace getPlaceAdmin(@RequestParam(value = "id", required = false) Integer id,
                           @RequestParam(value = "name", required = false) String name) {
        if (id != null) {
            return placeService.getVPlaceByIdAdmin(id);
        } else if (!StringUtils.isEmpty(name)) {
            return placeService.getVPlaceByNameAdmin(name);
        } else {
            throw new InvalidInputParametersException("The given id must not be null!");
        }

    }

    @RequestMapping("/admin/place/all")
    @ResponseBody
    public List<VPlace> getPlacesAdmin(){
        return placeService.getVPlacesAdmin();
    }

    @RequestMapping("/admin/place/pages")
    @ResponseBody
    public List<VPlace> getPlacesAdmin(@PageableDefault(size = PAGE_LIMIT) Pageable page){
        return placeService.getVPlacesAdmin(page);
    }

    @RequestMapping("/admin/place/search")
    @ResponseBody
    public List<VPlace> getPlacesByNameOrTagAdmin(@RequestParam(value = "arg") String arg){

        return placeService.getVPlaceByNameOrTagAdmin(arg);
    }

    @RequestMapping(value = "/admin/place/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo savePlace(@RequestBody VPlace place) {
        Place savedPlace = placeService.savePlace(place);
        return new ResponseInfo(savedPlace.getId());
    }

    @RequestMapping(value = "/admin/place/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseInfo removePlace(@RequestParam(value = "id", required = true) Integer id) {
        placeService.deletePlace(id);
        return new ResponseInfo(id);
    }
}
