package ateneu.sgcti.shared.exceptions;

import ateneu.sgcti.auth.exception.AuthUnauthorizedException;
import ateneu.sgcti.gchamados.exception.ChamadoBusinessException;
import ateneu.sgcti.gchamados.exception.ChamadoNotFoundException;
import ateneu.sgcti.gsolicitantes.exception.SolicitanteBusinessException;
import ateneu.sgcti.gsolicitantes.exception.SolicitanteNotFoundException;
import ateneu.sgcti.gtecnicos.exception.TecnicoBusinessException;
import ateneu.sgcti.gtecnicos.exception.TecnicoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SolicitanteNotFoundException.class)
	public ResponseEntity<CustomHandler> handleSolicitanteNotFoundException(SolicitanteNotFoundException ex) {
		return buildResponse(ex.getMessage(), "Solicitante não encontrado", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(TecnicoNotFoundException.class)
	public ResponseEntity<CustomHandler> handleTecnicoNotFoundException(TecnicoNotFoundException ex) {
		return buildResponse(ex.getMessage(), "Técnico não encontrado", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ChamadoNotFoundException.class)
	public ResponseEntity<CustomHandler> handleChamadoNotFoundException(ChamadoNotFoundException ex) {
		return buildResponse(ex.getMessage(), "Chamado não encontrado", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({
			SolicitanteBusinessException.class,
			TecnicoBusinessException.class,
			ChamadoBusinessException.class
	})
	public ResponseEntity<CustomHandler> handleBusinessException(RuntimeException ex) {
		return buildResponse(ex.getMessage(), "Violação de regra de negócio", HttpStatus.CONFLICT);
	}

	@ExceptionHandler(AuthUnauthorizedException.class)
	public ResponseEntity<CustomHandler> handleAuthUnauthorizedException(AuthUnauthorizedException ex) {
		return buildResponse(ex.getMessage(), "Não autenticado", HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<CustomHandler> handleAccessDeniedException(AccessDeniedException ex) {
		return buildResponse(ex.getMessage(), "Acesso negado", HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomHandler> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		String detalhes = ex.getBindingResult().getFieldErrors().stream()
				.map(this::formatFieldError)
				.collect(Collectors.joining("; "));
		return buildResponse("Dados inválidos", detalhes, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<CustomHandler> handleException(Exception ex) {
		return buildResponse(ex.getMessage(), "Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private String formatFieldError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}

	private ResponseEntity<CustomHandler> buildResponse(String message, String details, HttpStatus status) {
		CustomHandler customHandler = new CustomHandler(
				message,
				details,
				status.value(),
				LocalDateTime.now()
		);
		return ResponseEntity.status(status).body(customHandler);
	}
}
