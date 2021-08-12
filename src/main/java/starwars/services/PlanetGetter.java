package starwars.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import starwars.model.classes.Planet;
import starwars.model.classes.PlanetQuery;

@Service
public class PlanetGetter {

	@Autowired
	private RestTemplate restTemplate = new RestTemplate();
	
	public PlanetGetter() {
		
	}
	
	public PlanetGetter(PlanetQuery query) {
		
	}
	
	public List<Planet> getListOfPlanets (PlanetQuery query){
        String next = query.getNext();
        List<Planet> list = query.getResults();
        while (next != null){
            PlanetQuery results = restTemplate.getForObject(next, PlanetQuery.class);
            next = results.getNext();
            list.addAll(results.getResults());
        }
        for (Planet plan: list) {
        	plan.setPlanetIdFromUrl(plan.getUrl());
        }
        return list;
	}
}
