package net.lviv.intoeat.vmodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.lviv.intoeat.models.Contact;
import net.lviv.intoeat.models.Place;
import net.lviv.intoeat.models.Tag;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VPlace extends VBaseModel {

    public String name;
    public String image;
    public String baseDir;
    public String description;

    public List<VContact> contacts;
    public List<VTag> tags;

    public Set<Integer> tagsIds;

    public static VPlace transform(Place place) {
        VPlace vPlace = new VPlace();
        vPlace.id = place.getId();
        vPlace.name = place.getName();
        vPlace.image = getImageLink(place, vPlace);
        vPlace.description = place.getDescription();
        vPlace.contacts = new LinkedList<>();
        vPlace.tags = new LinkedList<>();
        for (Contact contact : place.getContacts()) {
            vPlace.contacts.add(VContact.transform(contact));
        }
        for (Tag tag : place.getTags()) {
            vPlace.tags.add(VTag.transform(tag));
        }
        return vPlace;
    }

    public static VPlace transformAdmin(Place place) {
        VPlace vPlace = new VPlace();
        vPlace.id = place.getId();
        vPlace.name = place.getName();
        vPlace.image = getImageLink(place, vPlace);
        vPlace.description = place.getDescription();
        vPlace.contacts = new LinkedList<>();
        vPlace.tagsIds = new HashSet<>();
        for (Contact contact : place.getContacts()) {
            vPlace.contacts.add(VContact.transform(contact));
        }
        for (Tag tag : place.getTags()) {
            vPlace.tagsIds.add(tag.getId());
        }
        return vPlace;
    }

    public static Place transformAdmin(VPlace vPlace) {
        Place place = new Place();
        place.setId(vPlace.id);
        place.setName(vPlace.name);
        place.setDescription(vPlace.description);
        place.setImage(vPlace.image);
        return place;
    }

    private static String getImageLink(Place place, VPlace vPlace) {
        String image = place.getImage();
        if (image != null) {
            String result = image.replace("\\", "/");
            return result.startsWith("/") ? result : "/" + result;
        }
        return null;
    }
    
}
