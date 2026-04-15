package ateneu.sgcti.gsolicitantes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SolicitanteNotFoundException extends RuntimeException {

    public SolicitanteNotFoundException(Integer id) {
        super("Solicitante não encontrado com id " + id);
    }
}

