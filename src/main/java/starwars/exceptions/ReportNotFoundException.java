package starwars.exceptions;

@SuppressWarnings("serial")
public class ReportNotFoundException extends RuntimeException {

	public ReportNotFoundException(Long id) {
		super("Could not find report " + id);
	}
	
}
