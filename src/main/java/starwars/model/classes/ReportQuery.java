package starwars.model.classes;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class ReportQuery {
	
	@JsonProperty("query_criteria_character_phrase")
	private String queryCharacter;
	@JsonProperty("query_criteria_planet_name")
	private String queryPlanet;
	
}