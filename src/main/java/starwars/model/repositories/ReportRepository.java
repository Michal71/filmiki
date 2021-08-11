package starwars.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import starwars.model.classes.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
