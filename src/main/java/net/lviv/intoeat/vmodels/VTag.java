package net.lviv.intoeat.vmodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.lviv.intoeat.models.Tag;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VTag extends VBaseModel {

    public String name;
    public String description;

    public static VTag transform(Tag tag) {
        VTag vTag = new VTag();
        vTag.id = tag.getId();
        vTag.name = tag.getName();
        vTag.description = tag.getDescription();
        return vTag;
    }

    public static Tag transformAdmin(VTag vTag) {
        Tag tag = new Tag();
        tag.setId(vTag.id);
        tag.setName(vTag.name);
        tag.setDescription(vTag.description);
        return tag;
    }

}
