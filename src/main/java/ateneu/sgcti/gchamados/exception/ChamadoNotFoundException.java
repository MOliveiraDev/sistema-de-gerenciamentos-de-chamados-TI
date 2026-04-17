package ateneu.sgcti.gchamados.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChamadoNotFoundException extends RuntimeException {

    public ChamadoNotFoundException(Integer id) {
        super("Chamado não encontrado com id " + id);
    }
}

