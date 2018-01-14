package com.ejemplo.bicis.excepciones;

/**
 * Esta excepcion ocurre cuando se intenta reservar una bicicleta que ya fue
 * reservada. Puede darse por un error en los datos que llegan al backend, o por
 * situaciones de concurrencia, en donde dos usuarios intentaron reservar la
 * misma bici con muy poco tiempo de diferencia
 * 
 * @author jose
 *
 */
public class BicicletaYaHaSidoReservadaException extends Exception {

	private static final long serialVersionUID = 9174959251434564398L;

	public BicicletaYaHaSidoReservadaException(Long id) {
		super("La bicicleta: " + id + "fue recientemente reservada");
	}

	public BicicletaYaHaSidoReservadaException(String string) {
		super(string);
	}

}
