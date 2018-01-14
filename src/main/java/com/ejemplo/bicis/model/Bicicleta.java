package com.ejemplo.bicis.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * 
 * Esta clase representa una bicicleta. Tiene un id, un color, y un booleano
 * para indicar si fue reservada o no.
 * 
 * @author jose
 *
 */

@Entity
@Table(name = "BICICLETA")
public class Bicicleta {

	@Id
	@GeneratedValue
	private Long id;

	private String color;

	private Boolean reservada;

	@Version
	private int version;

	/**
	 * Constructor por defecto para que JPA no tire error
	 */
	public Bicicleta() {

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

	public Boolean getReservada() {
		return reservada;
	}

	public void setReservada(Boolean reservada) {
		this.reservada = reservada;
	}

	public int getVersion() {
		return version;
	}

}
