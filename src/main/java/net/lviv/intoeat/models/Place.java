package net.lviv.intoeat.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Places")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Place extends BaseModel {

    @Column(unique = true, nullable = false) //TODO In future version create unique constraint by address
	private String name;
	private String image;
	private String description;

	@OneToOne(cascade = CascadeType.ALL)
	private Details details;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "place_id")
	private List<Contact> contacts;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name = "Place_Tag",
            joinColumns = @JoinColumn(name = "place_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
	private List<Tag> tags;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String logoPath) {
		this.image = logoPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
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
        return String.format("Place[Id = %d, Name = %s, Tags = ", getId(), name) + tags.toString();
    }
}
