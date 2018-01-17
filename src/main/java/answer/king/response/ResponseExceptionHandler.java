package answer.king.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import answer.king.exception.InvalidItemException;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ InvalidItemException.class })
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		InvalidItemException invalidItemException = (InvalidItemException)e;
		Error error = new Error(101, invalidItemException.getMessage());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request);
	}
	
}
