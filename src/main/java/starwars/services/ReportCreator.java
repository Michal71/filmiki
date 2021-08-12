package starwars.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import starwars.exceptions.QueryReturnedNoDataException;

import starwars.model.classes.Character;
import starwars.model.classes.Planet;
import starwars.model.classes.Film;
import starwars.model.classes.Report;
import starwars.model.classes.Result;

@Service
public class ReportCreator {
	
	@Autowired
	private RestTemplate restTemplate = new RestTemplate();
	
	public ReportCreator() {
		
	}
	
	public ReportCreator(List<Character> listOfCharacters, List<Planet> listOfPlanets) {
		
	}
	
	public Report getReport (Long id, String characterPhrase, String planetName, List<Character> listOfCharacters, List<Planet> listOfPlanets){
		List<String> listOfFilmUrls = new ArrayList<String>();
		List<Result> results = new ArrayList<Result>();
		for (Character chr: listOfCharacters) {
			for (Planet plan: listOfPlanets) {
				if (chr.getHomeworld().equals(plan.getUrl())){
					listOfFilmUrls = chr.getFilmUrls();
					for (String filmUrl: listOfFilmUrls) {
						Film film = restTemplate.getForObject(filmUrl, Film.class);
						film.setFilmIdFromUrl(film.getUrl());
						results.add(new Result(film.getFilmId(), film.getTitle(), chr.getCharacterId(),
								chr.getName(), plan.getPlanetId(), plan.getName()));
					}
				}
			}
		}
		 if (results.size() == 0) throw new QueryReturnedNoDataException();
		Report report = new Report(id, characterPhrase, planetName, results);
		return report;
	}
	
}
