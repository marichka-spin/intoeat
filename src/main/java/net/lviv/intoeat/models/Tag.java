package net.lviv.intoeat.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "Tags")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag  extends BaseModel {

    @Column(unique = true, nullable = false)
	private String name;
	private String description;

	@Enumerated(EnumType.ORDINAL)
	private Type type = Type.TYPE;

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

    @Override
    public String toString() {
        return String.format("Tag[Id = %d, Name = %s]", getId(), name);
    }

}

enum Type {
	TYPE, SERVICE
}
