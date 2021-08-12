package starwars.model.classes;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class Film {
	private String filmId;
	@JsonProperty("title")
	private String title;
	@JsonProperty("url")
	private String url;	
	
	public void setFilmIdFromUrl(String url) {
    	String[] urlSplit = url.split("/");
    	String id = urlSplit[urlSplit.length - 1];
    	this.setFilmId(id);
	}
}

