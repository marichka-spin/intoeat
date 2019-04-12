package net.lviv.intoeat.services;

import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.vmodels.VGroup;

import java.util.List;


public interface GroupService {

    VGroup getVGroupById(Integer id);
    VGroup getVGroupByName(String name);
    List<VGroup> getVGroups();
    
	VGroup getVGroupByIdAdmin(Integer id);
	VGroup getVGroupByNameAdmin(String name);
	List<VGroup> getVGroupsAdmin();
    List<VGroup> getVGroupsByNameAdmin(String name);

	Group saveGroup(VGroup group);
	void deleteGroup(Integer id);
}
