/**
 * 
 */
package com.ejemplo.bicis;

/**
 * @author jose
 *
 */
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ejemplo.bicis.model.Bicicleta;
import com.ejemplo.model.dto.BicicletaDTO;

public class BicicletaDTOTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Bicicleta bici = new Bicicleta();
		bici.setId(77L);
		bici.setReservada(Boolean.FALSE);
		bici.setColor("Rojo");
		BicicletaDTO biciDto = new BicicletaDTO(bici);
		assertTrue(biciDto.getId().equals(bici.getId()));
		assertTrue(biciDto.getColor().equals(bici.getColor()));

	}
}
