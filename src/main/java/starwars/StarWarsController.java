package starwars;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import starwars.model.classes.Report;
import starwars.model.classes.ReportQuery;
import starwars.services.ReportService;

@RestController
public class StarWarsController {
	
    private final ReportService reportService;

    @Autowired
    public StarWarsController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    @GetMapping("/reports/{id}")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getOne(id));
    }

    @GetMapping(("/reports"))
    public ResponseEntity<List<Report>> getReports() {
        return ResponseEntity.ok(reportService.getAll());
    }
	
    @PutMapping("/reports/{id}")
    public ResponseEntity<Void> response( @RequestBody ReportQuery reportQuery,
    		@PathVariable Long id) {
    	reportService.putOrReplace(reportQuery, id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteOne(id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/reports")
    public ResponseEntity<Void> deleteAllReports() {
        reportService.deleteAll();
        return ResponseEntity.status(204).build();
    }

}
