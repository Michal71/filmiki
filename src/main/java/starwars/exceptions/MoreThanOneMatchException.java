package starwars.exceptions;

@SuppressWarnings("serial")
public class MoreThanOneMatchException extends RuntimeException {

	public MoreThanOneMatchException() {
		super("There is more then one result matching this character phrase and this planet name. Try to be more specific");
	}
	
}