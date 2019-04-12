package net.lviv.intoeat.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group extends BaseModel {

	@Column(unique = true, nullable = false)
	private String name;
	private String description;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "Group_Tag", 
			   joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), 
			   inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
	private List<Tag> tags;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

    public boolean removeTag(Integer tagId) {
        if (tags != null) {
           for (Tag tag : tags) {
               if (tag.getId().equals(tagId)) {
                   return tags.remove(tag);
               }
           }
        }
        return false;
    }
	
	@Override
    public String toString() {
        return String.format("Group[Id = %d, Name = %s]", getId(), name);
    }
}
