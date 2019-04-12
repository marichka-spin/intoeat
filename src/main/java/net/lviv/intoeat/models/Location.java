package net.lviv.intoeat.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Embeddable;

@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
	private Double latitude;
	private Double longitude;
	private Integer zoom;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}
}
