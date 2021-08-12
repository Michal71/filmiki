package starwars.model.classes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public @Data class CharacterQuery {
	
    @JsonProperty("next")
    private String next; 
    @JsonProperty("results")
    private List<Character> results;   
    
}