package net.lviv.intoeat.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Details")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Details extends BaseModel {

	private String about;
	private String menu;

	@ElementCollection
	@CollectionTable(name = "Details_Halls")
	@Column(name = "hall")
	@MapKeyColumn(name = "hall_key")
	private Map<String, String> halls;

	@ElementCollection
	@CollectionTable(name = "Details_Entertainments")
	@Column(name = "entertainment")
	@MapKeyColumn(name = "entertainment_key")
	private Map<String, String> entertainments;

	@ElementCollection
	@CollectionTable(name = "Details_FoodType")
    @Column(name = "food")
	@MapKeyColumn(name = "food_type_key")
	private Map<String, String> foodType;

	@ElementCollection
	@CollectionTable(name = "Details_Parking")
	@MapKeyColumn(name = "parking_key")
	private Map<String, String> parking;

	@ElementCollection
	@CollectionTable(name = "Details_ForChildren")
    @Column(name = "for_children")
	private List<String> forChildren;

	@ElementCollection
	@CollectionTable(name = "Details_Kitchens")
    @Column(name = "kitchen")
	private List<String> kitchens;

	@ElementCollection
	@CollectionTable(name = "Details_Languages")
    @Column(name = "language")
	private List<String> languages;

	@ElementCollection
	@CollectionTable(name = "Details_PhotoPaths")
    @Column(name = "photo_path")
	private List<String> photoPaths;

	@OneToMany
	private List<Event> events;

	private Location map;

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public Location getMap() {
		return map;
	}

	public void setMap(Location map) {
		this.map = map;
	}

	public Map<String, String> getEntertainments() {
		return entertainments;
	}

	public void setEntertainments(Map<String, String> entertainments) {
		this.entertainments = entertainments;
	}

	public Map<String, String> getFoodType() {
		return foodType;
	}

	public void setFoodType(Map<String, String> foodType) {
		this.foodType = foodType;
	}

	public Map<String, String> getParking() {
		return parking;
	}

	public void setParking(Map<String, String> parking) {
		this.parking = parking;
	}

	public Map<String, String> getHalls() {
		return halls;
	}

	public void setHalls(Map<String, String> halls) {
		this.halls = halls;
	}

	public List<String> getForChildren() {
		return forChildren;
	}

	public void setForChildren(List<String> forChildren) {
		this.forChildren = forChildren;
	}

	public List<String> getKitchens() {
		return kitchens;
	}

	public void setKitchens(List<String> kitchens) {
		this.kitchens = kitchens;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public List<String> getPhotoPaths() {
		return photoPaths;
	}

	public void setPhotoPaths(List<String> photoPaths) {
		this.photoPaths = photoPaths;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
