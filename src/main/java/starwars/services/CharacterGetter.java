package starwars.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import starwars.model.classes.Character;
import starwars.model.classes.CharacterQuery;

@Service
public class CharacterGetter {
	
	@Autowired
	private RestTemplate restTemplate = new RestTemplate();
	
	public CharacterGetter() {
		
	}
	
	public CharacterGetter(CharacterQuery query) {
		
	}
	
	public List<Character> getListOfCharacters (CharacterQuery query){
        String next = query.getNext();
        List<Character> list = query.getResults();
        while (next != null){
            CharacterQuery results = restTemplate.getForObject(next, CharacterQuery.class);
            next = results.getNext();
            list.addAll(results.getResults());
        }
        for (Character chr: list) {
        	chr.setCharacterIdFromUrl(chr.getUrl());
        }
        return list;
	}
}

