package starwars.model.classes;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class Character {
	
    private String characterId;
    private String name;
    private String homeworld;
    private String url;
    @JsonProperty("films")
    private ArrayList<String> filmUrls;
    
	public void setCharacterIdFromUrl(String url) {
    	String[] urlSplit = url.split("/");
    	String id = urlSplit[urlSplit.length - 1];
    	this.setCharacterId(id);
	}
	
}