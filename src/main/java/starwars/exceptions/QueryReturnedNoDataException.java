package starwars.exceptions;

@SuppressWarnings("serial")
public class QueryReturnedNoDataException extends RuntimeException {

	public QueryReturnedNoDataException() {
		super("This query returned no data");
	}
	
}
