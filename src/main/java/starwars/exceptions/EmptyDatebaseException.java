package starwars.exceptions;

@SuppressWarnings("serial")
public class EmptyDatebaseException extends RuntimeException {

	public EmptyDatebaseException() {
		super("There are no reports to display");
	}
}
