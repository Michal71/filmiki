package starwars.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import starwars.exceptions.EmptyDatebaseException;
import starwars.exceptions.ReportNotFoundException;
import starwars.exceptions.QueryReturnedNoDataException;
import starwars.model.classes.Character;
import starwars.model.classes.CharacterQuery;
import starwars.model.classes.Planet;
import starwars.model.classes.PlanetQuery;
import starwars.model.classes.Report;
import starwars.model.classes.ReportQuery;
import starwars.model.repositories.ReportRepository;

@Service
public class ReportService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private final ReportRepository repository;

	ReportService(ReportRepository repository) {
		this.repository = repository;
	}
	
	public List<Report> getAll() {
		List<Report> reports = repository.findAll().stream()
				.collect(Collectors.toList());
		if (reports.size() == 0) {
			throw new EmptyDatebaseException();
		}
		return reports;
	}
	
	public Report getOne(@PathVariable Long id) {
		Report report = repository.findById(id) //
				.orElseThrow(() -> new ReportNotFoundException(id));
		return report;
	}
	
	public void putOrReplace(@RequestBody ReportQuery reportQuery, @PathVariable Long id) {
		String characterPhrase =  reportQuery.getQueryCharacter();
		String planetName = reportQuery.getQueryPlanet();
		if (characterPhrase.equals("") || planetName.equals("") ||
				characterPhrase == null || planetName == null ) {
			throw new QueryReturnedNoDataException();
		}
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
	
	public void deleteOne(@PathVariable Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
		}
		else {
			throw new ReportNotFoundException(id);
		}
	}
	
	public void deleteAll() {	
		repository.deleteAll();
	}
	
}
