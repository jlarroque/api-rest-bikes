package com.ejemplo.bicis.excepciones;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ejemplo.bicis.services.PagosService;

/**
 * Manejador de Excepciones global
 * 
 * @author jose
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(PagosService.class);

	@ExceptionHandler({ TipoDeReservaInvalidaException.class })
	protected ResponseEntity<Object> handleBicicletaYaHaSidoReservadaException(TipoDeReservaInvalidaException ex,
			WebRequest request) {
		String bodyOfResponse = "Tipo de reserva invalida!";
		log.info(ex.toString());
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}

	@ExceptionHandler({ BicicletaYaHaSidoReservadaException.class })
	protected ResponseEntity<Object> handleBicicletaYaHaSidoReservadaException(BicicletaYaHaSidoReservadaException ex,
			WebRequest request) {
		String bodyOfResponse = "Bicicleta reservada previamente!";
		log.info(ex.toString());
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}

	/**
	 * Catch all for any other exceptions...
	 */
	@ExceptionHandler({ Exception.class })
	protected ResponseEntity<Object> handleAnyException(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "Excepcion genérica";
		log.info(ex.toString());
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
}
