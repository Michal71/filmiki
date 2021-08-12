package starwars.model.classes;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable  
public @Data class Result {
	
	@JsonProperty("film_id")
	private String filmId;
	@JsonProperty("film_name")
	private String filmName;
	@JsonProperty("character_id")
	private String characterId;
	@JsonProperty("character_name")
	private String characterName;
	@JsonProperty("planet_id")
	private String planetId;
	@JsonProperty("planet_name")
	private String planetName; 
	
}
