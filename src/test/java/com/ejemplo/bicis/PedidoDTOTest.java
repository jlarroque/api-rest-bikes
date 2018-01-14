/**
 * 
 */
package com.ejemplo.bicis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.ejemplo.bicis.model.TiposDeReserva;
import com.ejemplo.model.dto.PedidoDTO;

/**
 * @author jose
 *
 */
public class PedidoDTOTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		PedidoDTO pedidoDto = new PedidoDTO();

		Long id = 1L;
		Integer tipoDeReserva = TiposDeReserva.POR_DIA.getTipo();
		pedidoDto.setId(id);
		pedidoDto.setTipoDeReserva(tipoDeReserva);

		assertNotNull(pedidoDto);
		assertTrue(tipoDeReserva.equals(pedidoDto.getTipoDeReserva()));
		assertTrue(id.equals(pedidoDto.getId()));

	}
}
