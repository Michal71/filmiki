package starwars.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class EmployeeNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(ReportNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String reportNotFoundHandler(ReportNotFoundException ex) {
		return ex.getMessage();
	}
}