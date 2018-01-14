package com.ejemplo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa los datos que un usuario del sistema envia con el fin de
 * hacer una reserva de bicicletas
 * 
 * @author jose
 *
 */
public class PedidoDTO {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("tipoDeReserva")
	private Integer tipoDeReserva;
	
	@JsonProperty("veces")
	private Integer veces; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTipoDeReserva() {
		return tipoDeReserva;
	}

	public void setTipoDeReserva(Integer tipoDeReserva) {
		this.tipoDeReserva = tipoDeReserva;
	}

	public Integer getVeces() {
		return veces;
	}

	public void setVeces(Integer veces) {
		this.veces = veces;
	}
}
