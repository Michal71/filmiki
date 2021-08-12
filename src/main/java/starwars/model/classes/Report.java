package starwars.model.classes;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public @Data class Report {
	@JsonProperty("report_id")
	private @Id Long reportId;
	@JsonProperty("query_criteria_character_phrase")
	private String queryCharacter;
	@JsonProperty("query_criteria_planet_name")
	private String queryPlanet;
	@JsonProperty("results")
	@ElementCollection
	private List <Result> results;
}
