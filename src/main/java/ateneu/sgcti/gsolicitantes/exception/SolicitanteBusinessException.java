package ateneu.sgcti.gsolicitantes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SolicitanteBusinessException extends RuntimeException {

    public SolicitanteBusinessException(String message) {
        super(message);
    }
}

