package com.ejemplo.bicis.excepciones;

/**
 * Ocurre cuando el tipo de reserva que se quiere realizar sobre una bicicleta,
 * no es un numero valido.
 * 
 * @author jose
 *
 */
public class TipoDeReservaInvalidaException extends Exception {

	private static final long serialVersionUID = -7839037885558576185L;

	public TipoDeReservaInvalidaException(String string) {
		super(string);
	}

}
