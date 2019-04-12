package net.lviv.intoeat.vmodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.models.Tag;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VGroup extends VBaseModel {

    public String name;
    public String description;

    public List<VTag> tags;

    public Set<Integer> tagsIds;

    public static VGroup transform(Group group) {
        VGroup vGroup = new VGroup();
        vGroup.id = group.getId();
        vGroup.name = group.getName();
        vGroup.description = group.getDescription();
        vGroup.tags = new LinkedList<>();
        for (Tag tag : group.getTags()) {
            vGroup.tags.add(VTag.transform(tag));
        }
        return vGroup;
    }

    public static VGroup transformAdmin(Group group) {
        VGroup vGroup = new VGroup();
        vGroup.id = group.getId();
        vGroup.name = group.getName();
        vGroup.description = group.getDescription();
        vGroup.tagsIds = new HashSet<>();
        for (Tag tag : group.getTags()) {
            vGroup.tagsIds.add(tag.getId());
        }
        return vGroup;
    }

    public static Group transformAdmin(VGroup vGroup) {
        Group group = new Group();
        group.setId(vGroup.id);
        group.setName(vGroup.name);
        group.setDescription(vGroup.description);
        return group;
    }

}
