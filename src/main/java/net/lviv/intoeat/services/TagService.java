package net.lviv.intoeat.services;


import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.vmodels.VTag;

import java.util.List;

public interface TagService {

    public VTag getVTagById(Integer id);
    public VTag getVTagByName(String name);
    public List<Tag> getTags();
    public List<VTag> getVTags();
    public List<VTag> getTagsByName(String arg);
    public Tag saveTag(VTag tag);
    public void deleteTag(Integer id);
}
