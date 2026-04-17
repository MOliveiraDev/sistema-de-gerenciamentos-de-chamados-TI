package ateneu.sgcti.gchamados.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ChamadoBusinessException extends RuntimeException {

    public ChamadoBusinessException(String message) {
        super(message);
    }
}

