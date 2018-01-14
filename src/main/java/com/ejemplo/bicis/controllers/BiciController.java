package com.ejemplo.bicis.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ejemplo.bicis.excepciones.BicicletaYaHaSidoReservadaException;
import com.ejemplo.bicis.excepciones.TipoDeReservaInvalidaException;
import com.ejemplo.bicis.model.Bicicleta;
import com.ejemplo.bicis.services.BiciService;
import com.ejemplo.bicis.services.PagosService;
import com.ejemplo.model.dto.BicicletaDTO;
import com.ejemplo.model.dto.PedidoDTO;

/**
 * Este controlador maneja todas las peticiones REST de la API. Dispone de los
 * siguientes metodos:
 * 
 * <ul>
 * <li>bicis -> GET, Devuelve todas las bicicletas, sin importar si fueron o no
 * reservadas</li>
 * <li>bicisLibres -> GET, Devuelve las bicicletas que no han sido reservadas.
 * </li>
 * <li>reservarUnaBici -> PUT, nos permite reservar una sola bici, por
 * cualquiera de los tipos de reserva posibles. Espera un JSON como el
 * siguiente: { "id": 1, "tipoDeReserva": 2 , veces : 2}</li>
 * <li>reservarVariasBicis -> PUT, nos permite reservar entre 3 y 5 bicis.
 * Espera un JSON como el siguiente: [{ "id": 1, "tipoDeReserva": 2,"veces":3 }, { "id":
 * 2, "tipoDeReserva": 3,"veces":1 }, { "id": 3, "tipoDeReserva": 1,"veces":3 }]</li>
 * </ul>
 * 
 * 
 * @author jose
 *
 */
@RestController
@RequestMapping("")
@Transactional
public class BiciController {

	@Autowired
	private BiciService servicioBicis;

	@Autowired
	private PagosService servicioPagos;

	/**
	 * Este metodo devuelve todas las bicicletas que existen en la BD. Nos
	 * permite tener un control del stock total de bicicletas.
	 * 
	 * @return List - Lista de todas las bicicletas
	 */
	@RequestMapping(method = RequestMethod.GET, value = { "/bicis" })
	public List<Bicicleta> consultarBicis() {
		return servicioBicis.obtenerTodasLasBicicletas();
	}

	/**
	 * Este metodo devuelve todas las bicicletas que existen en la BD. Nos
	 * permite tener un control del stock total de bicicletas.
	 * 
	 * @return List - Lista de bicicletas disponibles para ser reservadas
	 */
	@RequestMapping(method = RequestMethod.GET, value = { "/bicisLibres" })
	public List<Bicicleta> consultarBicisDisponibles() {
		return servicioBicis.obtenerTodasLasDisponibles();
	}

	/**
	 * Permite reservar una sola bici. Esto se logra a través de
	 * {@link PedidoDTO pedidoDTO}, el cual contiene el ID de la bicicleta a
	 * reservar, y el tipo de reserva a realizar.
	 * <ul>
	 * <li>Si el tipo de la reserva es invalida, devuelve
	 * {@link TipoDeReservaInvalidaException}.</li>
	 * <li>Si la bicicleta ya fue reservada, devuelve
	 * {@link BicicletaYaHaSidoReservadaException}.</li>
	 * </ul>
	 * 
	 * @param pedidoDto
	 * @return
	 * @throws TipoDeReservaInvalidaException
	 * @throws BicicletaYaHaSidoReservadaException
	 */
	@RequestMapping(method = RequestMethod.PUT, value = { "/reservarUnaBici" })
	public BicicletaDTO reservarUnaBici(PedidoDTO pedidoDto)
			throws TipoDeReservaInvalidaException, BicicletaYaHaSidoReservadaException {
		BicicletaDTO biciDto = servicioBicis.reservarBicicleta(pedidoDto);
		List<PedidoDTO> listaPedidosDto = new ArrayList<PedidoDTO>();
		listaPedidosDto.add(pedidoDto);
		biciDto.setImporte(servicioPagos.pagar(listaPedidosDto));
		return biciDto;
	}

	/**
	 * Permite reservar de 3 a 5 bicis a la vez. Esto se logra a través de una
	 * <b>lista</b> de {@link PedidoDTO pedidoDTO}, el cual contiene el ID de la
	 * bicicleta a reservar, y el tipo de reserva a realizar.
	 * <ul>
	 * <li>Si el tipo de la reserva es invalida, devuelve
	 * {@link TipoDeReservaInvalidaException}.</li>
	 * <li>Si la bicicleta ya fue reservada, devuelve
	 * {@link BicicletaYaHaSidoReservadaException}.</li>
	 * </ul>
	 * 
	 * @param listaDePedidosDtoDeBicis
	 *            - Listado de {@link PedidoDTO}
	 * @return
	 * @throws TipoDeReservaInvalidaException
	 * @throws BicicletaYaHaSidoReservadaException
	 */
	@RequestMapping(method = RequestMethod.PUT, value = { "/reservarVariasBicis" })
	public List<BicicletaDTO> reservarVariasBicis(@RequestBody List<PedidoDTO> listaDePedidosDtoDeBicis)
			throws TipoDeReservaInvalidaException, BicicletaYaHaSidoReservadaException {
		List<BicicletaDTO> bicisDto = servicioBicis.reservarBicicletas(listaDePedidosDtoDeBicis);
		bicisDto.get(0).setImporte(servicioPagos.pagar(listaDePedidosDtoDeBicis));
		return bicisDto;
	}

}
