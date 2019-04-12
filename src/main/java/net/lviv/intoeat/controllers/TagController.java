package net.lviv.intoeat.controllers;

import net.lviv.intoeat.exceptions.InvalidInputParametersException;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.utils.ResponseInfo;
import net.lviv.intoeat.vmodels.VTag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping(value = {"/tag", "/admin/tag"})
    @ResponseBody
    public VTag getTag(@RequestParam(value = "id", required = false) Integer id,
                        @RequestParam(value = "name", required = false) String name) {
        if (id != null) {
            return tagService.getVTagById(id);
        } else if (!StringUtils.isEmpty(name)) {
            return tagService.getVTagByName(name);
        } else {
            throw new InvalidInputParametersException("The given id must not be null!");
        }

    }

    @RequestMapping(value = {"/tag/all", "/admin/tag/all"})
    @ResponseBody
    public List<VTag> getVTags(){
        return tagService.getVTags();
    }

    @RequestMapping("/admin/tag/search")
    @ResponseBody
    public List<VTag> getTagsByName(@RequestParam(value = "arg") String arg) {
        return tagService.getTagsByName(arg);
    }

    @RequestMapping(value = "/admin/tag/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo saveTag(@RequestBody VTag tag) {
        Tag savedTag = tagService.saveTag(tag);
        return new ResponseInfo(savedTag.getId());
    }

    @RequestMapping(value = "/admin/tag/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseInfo removeTag(@RequestParam(value = "id", required = true) Integer id) {
        tagService.deleteTag(id);
        return new ResponseInfo(id);
    }

}
