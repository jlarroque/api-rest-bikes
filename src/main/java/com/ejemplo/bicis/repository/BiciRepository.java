package com.ejemplo.bicis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ejemplo.bicis.model.Bicicleta;

/**
 * Repositorio JPA para interactuar con la BD.
 * 
 * @author jose
 *
 */
@Repository
public interface BiciRepository extends JpaRepository<Bicicleta, Long> {

	/**
	 * Devuelve todas las bicicletas que esten disponible para reserva.
	 * Aprovechando JPA, no hace falta implementar este metodo, JPA se encarga
	 * de eso
	 * 
	 * @param booleano
	 * @return
	 */
	List<Bicicleta> findByReservada(Boolean booleano);

}
