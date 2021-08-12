package starwars.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import starwars.exceptions.ReportNotFoundException;
import starwars.exceptions.EmptyDatebaseException;
import starwars.exceptions.QueryReturnedNoDataException;
import starwars.model.classes.Report;
import starwars.model.classes.ReportQuery;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceTest {
	
    @Autowired
    private ReportService reportService;

    private String characterPhrase;
    private String planetName;
    private Long reportId = 1L;
    private ReportQuery reportQuery;
    
    @Test
    @Transactional
    public void testIfReportIsCorrectlyPersisted() {
    	characterPhrase = "luke";
    	planetName = "tat";
    	reportQuery = reportQuery.builder().queryCharacter(characterPhrase).queryPlanet(planetName).build();
        reportService.putOrReplace(reportQuery, reportId);
        Report newReport = reportService.getOne(1L);
        assertThat(newReport.getReportId()).isEqualTo(1L);
        assertThat(newReport.getResults()).isNotEmpty();
        assertThat(newReport.getQueryCharacter()).isEqualTo(characterPhrase);
        assertThat(newReport.getQueryPlanet()).isEqualTo(planetName);
    }

    @Test(expected = ReportNotFoundException.class)
    public void reportNotFoundExceptionTest() {
        reportService.deleteOne(2L);
    }

    @Test(expected = EmptyDatebaseException.class)
    public void emptyDatebaseExceptionTest() {
        reportService.getAll();
    }
    
    @Test(expected = QueryReturnedNoDataException.class)
    public void queryReturnedNoDataExceptionTest1() {
    	characterPhrase = "lukeeee";
    	planetName = "tat";
    	reportQuery = reportQuery.builder().queryCharacter(characterPhrase).queryPlanet(planetName).build();
        reportService.putOrReplace(reportQuery, reportId);
    }
    
    @Test(expected = QueryReturnedNoDataException.class)
    public void queryReturnedNoDataExceptionTest2() {
    	characterPhrase = "";
    	planetName = "";
    	reportQuery = reportQuery.builder().queryCharacter(characterPhrase).queryPlanet(planetName).build();
        reportService.putOrReplace(reportQuery, reportId);
    }

}
