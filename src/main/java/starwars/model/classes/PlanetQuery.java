package starwars.model.classes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

public @Data class PlanetQuery {
    @JsonProperty("results")
    private JsonNode[] results; 
}
