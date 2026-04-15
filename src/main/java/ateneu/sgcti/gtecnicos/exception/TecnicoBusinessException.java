package ateneu.sgcti.gtecnicos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TecnicoBusinessException extends RuntimeException {

    public TecnicoBusinessException(String message) {
        super(message);
    }
}

