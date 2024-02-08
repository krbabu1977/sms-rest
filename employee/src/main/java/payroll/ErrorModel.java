package payroll;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorModel {
	private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    
    public ErrorModel(Date timestamp, HttpStatus httpStatus, String message, String path) {
		this.timestamp = timestamp;
		this.status = httpStatus.value();
		this.error = httpStatus.getReasonPhrase();		
		this.message = message;
		this.path = path;
	}
}
