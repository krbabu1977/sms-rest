package payroll;

import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalRestExceptionHandler {
	@ExceptionHandler(value = {ResourceNotFoundException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorModel handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
       
		return new ErrorModel(new Date(), HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());

    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorModel handleOtherExceptions(Exception e, HttpServletRequest request) {
    	
    	return new ErrorModel(new Date(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI());
	
    }
}
