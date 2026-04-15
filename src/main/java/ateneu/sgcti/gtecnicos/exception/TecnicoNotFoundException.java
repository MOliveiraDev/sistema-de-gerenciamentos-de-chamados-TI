package ateneu.sgcti.gtecnicos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TecnicoNotFoundException extends RuntimeException {

    public TecnicoNotFoundException(Integer id) {
        super("Técnico não encontrado com id " + id);
    }
}

