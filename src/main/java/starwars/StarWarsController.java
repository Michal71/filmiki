package starwars;

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

import starwars.exceptions.EmptyDatebaseException;
import starwars.exceptions.ReportNotFoundException;
import starwars.model.classes.Character;
import starwars.model.classes.CharacterQuery;
import starwars.model.classes.Planet;
import starwars.model.classes.PlanetQuery;
import starwars.model.classes.Report;
import starwars.model.repositories.ReportRepository;
import starwars.services.CharacterGetter;
import starwars.services.PlanetGetter;
import starwars.services.ReportCreator;

@RestController
public class StarWarsController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private final ReportRepository repository;

	StarWarsController(ReportRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/reports")
	List<Report> all() {
		List<Report> reports = repository.findAll().stream()
				.collect(Collectors.toList());
		if (reports.size() == 0) {
			throw new EmptyDatebaseException();
		}
		return reports;
	}

	@GetMapping("/reports/{id}")
	Report one(@PathVariable Long id) {
		Report report = repository.findById(id) //
				.orElseThrow(() -> new ReportNotFoundException(id));
		return report;
	}

	@PutMapping("/reports/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	void replaceReport(@RequestBody Report reportQuery, @PathVariable Long id) {
		String characterPhrase =  reportQuery.getQueryCharacter();
		String planetName = reportQuery.getQueryPlanet();
		
		CharacterQuery characterQuery = 
				restTemplate.getForObject("http://localhost:8080/api/people/?search="+ characterPhrase, CharacterQuery.class);
		CharacterGetter charGetter = new CharacterGetter();
		List<Character> charQueryResults = charGetter.getListOfCharacters(characterQuery);	
		
		PlanetQuery planetQuery = restTemplate.getForObject("http://localhost:8080/api/planets/?search="+ planetName, PlanetQuery.class);
		PlanetGetter planGetter = new PlanetGetter();
		List <Planet> planQueryResults = planGetter.getListOfPlanets(planetQuery);
		ReportCreator creator = new ReportCreator();
		Report newReport = creator.getReport(id, characterPhrase, planetName, charQueryResults, planQueryResults);

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
	}

	@DeleteMapping("/reports/{id}")
	void deleteReport(@PathVariable Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
		}
		else {
			throw new ReportNotFoundException(id);
		}
	}
	
	@DeleteMapping("/reports")
	void deleteAllReports() {	
		repository.deleteAll();
	}
	
}
