package com.ejemplo.model.dto;

import java.math.BigDecimal;

import com.ejemplo.bicis.model.Bicicleta;

/**
 * DTO que representa una bicicleta
 * 
 * @author jose
 *
 */
public class BicicletaDTO {
	private Long id;

	private String color;
	
	private BigDecimal importe;

	public BicicletaDTO() {

	}

	public BicicletaDTO(Bicicleta bici) {
		this.setColor(bici.getColor());
		this.setId(bici.getId());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}
}
