package com.ejemplo.bicis.model;

/**
 * 
 * Enumerativo que contiene los distintos tipos de reserva que hay disponibles,
 * con el fin de reservar una bicicleta.
 * 
 * @author jose
 *
 */
public enum TiposDeReserva {
	POR_HORA(1), POR_DIA(2), POR_SEMANA(3);

	private Integer tipo;	


	public Integer getTipo() {
		return tipo;
	}

	TiposDeReserva(Integer tipo) {
		this.tipo = tipo;
	}
}
