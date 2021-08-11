package starwars;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import starwars.exceptions.MoreThanOneMatchException;
import starwars.exceptions.ReportNotFoundException;
import starwars.model.classes.Character;
import starwars.model.classes.CharacterQuery;
import starwars.model.classes.Film;
import starwars.model.classes.Planet;
import starwars.model.classes.PlanetQuery;
import starwars.model.classes.Report;
import starwars.model.classes.Result;
import starwars.model.repositories.ReportRepository;

@RestController
public class ReportController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private final ReportRepository repository;

	ReportController(ReportRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/reports")
	List<Report> all() {
		List<Report> Reports = repository.findAll().stream()
				.collect(Collectors.toList());
		return Reports;
	}

	@GetMapping("/reports/{id}")
	Report one(@PathVariable Long id) {
		Report Report = repository.findById(id) //
				.orElseThrow(() -> new ReportNotFoundException(id));
		return Report;
	}

	@PutMapping("/reports/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	void replaceReport(@RequestBody Report reportQuery, @PathVariable Long id) {		
			String characterPhrase =  reportQuery.getQueryCharacter();
			String planetName = reportQuery.getQueryPlanet();
			
			List <Character> charQueryResults = new ArrayList<Character>();
			CharacterQuery characterQuery = restTemplate.getForObject("http://localhost:8080/api/people/?search="+ characterPhrase, CharacterQuery.class);
			for (int i = 0; i < characterQuery.getResults().length; i++) {
				String pName = characterQuery.getResults()[i].get("name").asText();
				String pHomeworld = characterQuery.getResults()[i].get("homeworld").asText();
				//Getting character id from Url
				String pUrl = characterQuery.getResults()[i].get("url").asText();
				ArrayList<String> pFilms = new ArrayList<String>();
				JsonNode arrNode = characterQuery.getResults()[i].get("films");			
				if (arrNode.isArray()) {
				    for (final JsonNode objNode : arrNode) {
				       pFilms.add(objNode.textValue());
				    }
				}
				String urlSplit[] = pUrl.split("/");
				String pId = urlSplit[urlSplit.length - 1];
				charQueryResults.add(new Character(pId, pName, pHomeworld, pFilms));
			}
			
			List <Planet> planQueryResults = new ArrayList<Planet>();
			PlanetQuery planetQuery = restTemplate.getForObject("http://localhost:8080/api/planets/?search="+ planetName, PlanetQuery.class);
			for (int i = 0; i < planetQuery.getResults().length; i++) {
				String pName = planetQuery.getResults()[i].get("name").asText();
				String pUrl = planetQuery.getResults()[i].get("url").asText();
				//Getting planet id from Url
				String urlSplit[] = pUrl.split("/");
				String pId = urlSplit[urlSplit.length - 1];
				planQueryResults.add(new Planet(pId, pName, pUrl));
			}
			
			List <String> listOfFilms = new ArrayList<String>();
			//flag that makes sure only one pair character - planet is matched
			boolean alreadyMatched = false;
			Character matchedChr = new Character();
			Planet matchedPlt = new Planet();
			for (Character chr : charQueryResults) {
				for (Planet plt : planQueryResults) {
					if (chr.getHomeworld().equals(plt.getUrl())) {
						if (!alreadyMatched) {
							matchedChr = chr;
							matchedPlt = plt;
							listOfFilms.addAll(chr.getFilms());
						}
						else {
							throw new MoreThanOneMatchException();
						}
						alreadyMatched = true;
					}
				}
			}
			
			List <Result> results = new ArrayList<Result>();
			for (String film : listOfFilms) {
				Film filmQuery = restTemplate.getForObject(film, Film.class);
				String urlSplit[] = filmQuery.getUrl().split("/");
				String filmId = urlSplit[urlSplit.length - 1];
				filmQuery.setFilmId(filmId);
				Result result = new Result(filmQuery.getFilmId(), filmQuery.getTitle(), matchedChr.getCharacterId(), matchedChr.getName(), matchedPlt.getPlanetId(), matchedPlt.getName());
				results.add(result);
			}
			
			Report newReport = new Report(id, characterPhrase, planetName, results);

			repository.findById(id) //
				.map(Report -> {
					Report.setQueryCharacter(newReport.getQueryCharacter());
					Report.setQueryPlanet(newReport.getQueryPlanet());
					Report.setResults(newReport.getResults());
					return repository.save(Report);
				}) //
				.orElseGet(() -> {
					newReport.setReportId(id);
					return repository.save(newReport);
				});
			repository.findById(id);
	}

	@DeleteMapping("/reports/{id}")
	void deleteReport(@PathVariable Long id) {	
		repository.deleteById(id);
	}
	
	@DeleteMapping("/reports")
	void deleteAllReports() {	
		repository.deleteAll();
	}
	
}
