package net.lviv.intoeat.controllers;

import java.util.List;

import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.services.GroupService;
import net.lviv.intoeat.utils.ResponseInfo;

import net.lviv.intoeat.vmodels.VGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GroupController {
	
	@Autowired
    private GroupService groupService;

	@RequestMapping("/group")
    @ResponseBody
    public VGroup getGroup(@RequestParam(value = "id", required = false) Integer id,
                         @RequestParam(value = "name", required = false) String name) {
        if (StringUtils.isEmpty(name)) {
            return groupService.getVGroupById(id);
        }
        return groupService.getVGroupByName(name);
    }
	
    @RequestMapping("/group/all")
    @ResponseBody
    public List<VGroup> getGroups(){
        return groupService.getVGroups();
   }

    @RequestMapping("/admin/group")
    @ResponseBody
    public VGroup getGroupAdmin(@RequestParam(value = "id", required = false) Integer id,
                           @RequestParam(value = "name", required = false) String name) {
        if (StringUtils.isEmpty(name)) {
            return groupService.getVGroupByIdAdmin(id);
        }
        return groupService.getVGroupByNameAdmin(name);
    }

    @RequestMapping("/admin/group/all")
    @ResponseBody
    public List<VGroup> getGroupsAdmin(){
        return groupService.getVGroupsAdmin();
    }

    @RequestMapping("/admin/group/search")
    @ResponseBody
    public List<VGroup> getGroupsByNameAdmin(@RequestParam(value = "arg") String arg){
        return groupService.getVGroupsByNameAdmin(arg);
    }
    
    @RequestMapping(value = "/admin/group/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseInfo addGroup(@RequestBody VGroup group) {
        Group savedGroup = groupService.saveGroup(group);
        return new ResponseInfo(savedGroup.getId());
    }
    
    @RequestMapping(value = "/admin/group/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseInfo removeGroup(@RequestParam(value = "id", required = true) Integer id) {
        groupService.deleteGroup(id);
        return new ResponseInfo(id);
    }
}

