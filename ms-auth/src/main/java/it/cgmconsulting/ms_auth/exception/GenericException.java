package it.cgmconsulting.ms_auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class GenericException extends RuntimeException{

    private final String msg;
    private final HttpStatus status;
    public GenericException(String msg, HttpStatus status) {
        super(msg);
        this.msg = msg;
        this.status = status;
    }
}
