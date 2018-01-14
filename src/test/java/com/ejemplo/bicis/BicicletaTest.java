package com.ejemplo.bicis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ejemplo.bicis.model.Bicicleta;
import com.ejemplo.model.dto.BicicletaDTO;

public class BicicletaTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Bicicleta bici = new Bicicleta();
		Long id = 77L;
		Boolean reserva = Boolean.FALSE;
		String color = "Rojo";

		bici.setId(id);
		bici.setReservada(reserva);
		bici.setColor(color);

		assertNotNull(bici);
		assertTrue(id.equals(bici.getId()));
		assertTrue(reserva.equals(bici.getReservada()));
		assertTrue(color.equals(bici.getColor()));

	}
}
