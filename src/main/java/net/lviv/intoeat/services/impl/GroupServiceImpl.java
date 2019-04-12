package net.lviv.intoeat.services.impl;

import net.lviv.intoeat.exceptions.EntityNotFoundException;
import net.lviv.intoeat.models.Group;
import net.lviv.intoeat.models.Tag;
import net.lviv.intoeat.repositories.GroupRepository;
import net.lviv.intoeat.services.GroupService;
import net.lviv.intoeat.services.TagService;
import net.lviv.intoeat.validation.BaseValidator;
import net.lviv.intoeat.validation.RemovalValidator;
import net.lviv.intoeat.vmodels.VGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
	
	@Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private BaseValidator<Group> groupValidator;

	@Override
	public VGroup getVGroupById(Integer id) {
		Group group = groupRepository.findOne(id);
		if(group != null){
			return VGroup.transform(group);
		} else {
            throw new EntityNotFoundException(RemovalValidator.ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, groupValidator.type(), id);
        }
	}

	@Override
	public VGroup getVGroupByName(String name) {
		Group group = groupRepository.findByName(name);
		if(group != null){
			return VGroup.transform(group);
		} else {
            throw new EntityNotFoundException("Group with name %s not found", name);
        }
	}
	
	@Override
	public List<VGroup> getVGroups() {
        List<Group> groups = groupRepository.findAll();
        List<VGroup> vGroups = new LinkedList<>();
        for (Group group : groups) {
            vGroups.add(VGroup.transform(group));
        }
		return vGroups;
	}

    @Override
    public VGroup getVGroupByIdAdmin(Integer id) {
        Group group = groupRepository.findOne(id);
        if(group != null){
            return VGroup.transformAdmin(group);
        } else {
            throw new EntityNotFoundException(RemovalValidator.ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG, groupValidator.type(), id);
        }
    }

    @Override
    public VGroup getVGroupByNameAdmin(String name) {
        Group group = groupRepository.findByName(name);
        if(group != null){
            return VGroup.transformAdmin(group);
        } else {
            throw new EntityNotFoundException("Group with name %s not found", name);
        }
    }

    @Override
    public List<VGroup> getVGroupsAdmin() {
        List<Group> groups = groupRepository.findAll();
        List<VGroup> vGroups = new LinkedList<>();
        for (Group group : groups) {
            vGroups.add(VGroup.transformAdmin(group));
        }
        return vGroups;
    }

    @Override
    public List<VGroup> getVGroupsByNameAdmin(String name) {
        List<Group> groups = groupRepository.findGroupsByName("%" + name + "%");
        List<VGroup> vGroups = new LinkedList<>();
        for (Group group : groups) {
            vGroups.add(VGroup.transformAdmin(group));
        }
        return vGroups;
    }

    @Override
    @Transactional
	public Group saveGroup(VGroup vGroup) {
        Group group = VGroup.transformAdmin(vGroup);
		groupValidator.validate(group);
        group.setTags(populateTags(vGroup));
        return groupRepository.save(group);
    }

	@Override
	public void deleteGroup(Integer id) {
		((RemovalValidator)groupValidator).validate(id);
		groupRepository.delete(id);
	}

    private List<Tag> populateTags(VGroup vGroup) {
        List<Tag> tags = new ArrayList<>();
        if (vGroup.tagsIds != null) {
            for (Tag tag : tagService.getTags()) {
                if (vGroup.tagsIds.contains(tag.getId())) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }
}