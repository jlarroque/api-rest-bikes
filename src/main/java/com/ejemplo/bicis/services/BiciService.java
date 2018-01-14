package com.ejemplo.bicis.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

import com.ejemplo.bicis.excepciones.BicicletaYaHaSidoReservadaException;
import com.ejemplo.bicis.excepciones.TipoDeReservaInvalidaException;
import com.ejemplo.bicis.model.Bicicleta;
import com.ejemplo.bicis.model.TiposDeReserva;
import com.ejemplo.bicis.repository.BiciRepository;
import com.ejemplo.model.dto.BicicletaDTO;
import com.ejemplo.model.dto.PedidoDTO;

/**
 * Se encarga de manejar la logica de negocio. Tiene acceso al repositorio JPA
 * {@link BiciRepository} en orden de acceder a la BD de esta API.
 * 
 * @author jose
 *
 */
@Service
public class BiciService {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Repositorio JPA
	 */
	@Autowired
	private BiciRepository repositorio;

	/**
	 * 
	 * Obtener todas las bicis que existan, sin importar si fueron reservadas
	 * previamente o no. {@link BiciRepository repositorio}
	 * 
	 * @return
	 */
	public List<Bicicleta> obtenerTodasLasBicicletas() {
		return repositorio.findAll();
	}

	/**
	 * Obtener todas las bicis que no han sido reservadas desde el
	 * {@link BiciRepository repositorio}
	 * 
	 * @return
	 */
	public List<Bicicleta> obtenerTodasLasDisponibles() {
		return repositorio.findByReservada(Boolean.FALSE);
	}

	/**
	 * 
	 * Permite reservar una sola bici. Esto se logra a través de
	 * {@link PedidoDTO pedidoDTO}, el cual contiene el ID de la bicicleta a
	 * reservar, y el tipo de reserva a realizar.
	 * 
	 * @param pedido
	 * @return
	 * @throws TipoDeReservaInvalidaException
	 * @throws BicicletaYaHaSidoReservadaException
	 */
	public BicicletaDTO reservarBicicleta(PedidoDTO pedido)
			throws TipoDeReservaInvalidaException, BicicletaYaHaSidoReservadaException {

		Bicicleta bici = repositorio.getOne(pedido.getId());
		if (validarEstadoBici(bici)) {					
			return reservarYGuardarBicicleta(pedido, bici);
			
		} else {
			throw new BicicletaYaHaSidoReservadaException(pedido.getId());
		}

	}

	/**
	 * 
	 * Permite reservar de 3 a 5 bicis a la vez. Esto se logra a través de una
	 * <b>lista</b> de {@link PedidoDTO pedidoDTO}, el cual contiene el ID de la
	 * bicicleta a reservar, y el tipo de reserva a realizar.
	 * 
	 * <ul>
	 * <li>Si el tipo de la reserva es invalida, devuelve
	 * {@link TipoDeReservaInvalidaException}.</li>
	 * <li>Si la bicicleta ya fue reservada, devuelve
	 * {@link BicicletaYaHaSidoReservadaException}.</li>
	 * </ul>
	 * 
	 * @param listaDeDtoDeBicis
	 * @return
	 * @throws TipoDeReservaInvalidaException
	 * @throws BicicletaYaHaSidoReservadaException
	 */
	public List<BicicletaDTO> reservarBicicletas(List<PedidoDTO> listaDeDtoDeBicis)
			throws TipoDeReservaInvalidaException, BicicletaYaHaSidoReservadaException {
		List<BicicletaDTO> bicisDto = new ArrayList<BicicletaDTO>();
		if (validarTipoDeReserva(listaDeDtoDeBicis)) {

			List<Long> listaDeIds = getBicisIdsDePedidosDTO(listaDeDtoDeBicis);

			List<Bicicleta> bicis = repositorio.findAll(listaDeIds);
			if (validarEstadoBici(bicis)) {
				for (PedidoDTO pedido : listaDeDtoDeBicis) {
					bicisDto.add(reservarYGuardarBicicleta(pedido, buscarYDevolverBici(pedido.getId(), bicis)));
				}
			} else {
				throw new BicicletaYaHaSidoReservadaException(
						"Alguna de las bicicletas que intenta reservar fue recientemente reservada");
			}

			return bicisDto;
		} else {
			throw new TipoDeReservaInvalidaException("La cantidad de reservas debe estar entre 3 y 5");
		}
	}

	/**
	 * Permite reservar una bicicleta. Antes se valida si el
	 * {@link TipoDeReserva tipo de reserva} es valido.
	 * 
	 * @param pedido
	 *            - PedidoDTO que llego con el fin de hacer una reserva
	 * @param bici
	 *            - Bicicleta que se quiere reservar
	 * @return
	 * @throws TipoDeReservaInvalidaException
	 *             - Si es un tipo de reserva inválido
	 */
	private BicicletaDTO reservarYGuardarBicicleta(PedidoDTO pedido, Bicicleta bici)
			throws TipoDeReservaInvalidaException {
		if (validarTipoDeReserva(pedido)) {
			bici.setReservada(Boolean.TRUE);
			entityManager.merge(bici);
			return new BicicletaDTO(bici);
		} else {
			throw new TipoDeReservaInvalidaException("El tipo de reserva del pedido no es valido: "
					+ pedido.getTipoDeReserva() + ". Los tipos validos son: " + TiposDeReserva.POR_HORA.getTipo() + ","
					+ TiposDeReserva.POR_DIA.getTipo() + "," + TiposDeReserva.POR_SEMANA.getTipo());
		}

	}

	/**
	 * Busca una bici en una coleccion de bicis, a través de su ID. Al buscar
	 * por ID, nos aseguramos que haya solo un posible resultado
	 * 
	 * @param id
	 *            - Id de la bici que se está buscando
	 * @param bicis
	 *            - Lista de {@link Bicicleta bicis} en donde hay que buscar
	 * @return Bicicleta La bicicleta que se estaba buscando.
	 */
	@SuppressWarnings("unchecked")
	private Bicicleta buscarYDevolverBici(Long id, List<Bicicleta> bicis) {
		return ((List<Bicicleta>) CollectionUtils.select(bicis, new Predicate() {

			@Override
			public boolean evaluate(Object bici) {
				Bicicleta biciAux = (Bicicleta) bici;
				return biciAux.getId().equals(id);
			}
		})).get(0);
	}

	/**
	 * Devuelve todos los Ids de bicicletas, extrayendo los mismos de un listado
	 * de {@link PedidoDTO pedidosDto}
	 * 
	 * @param listaDeDtoDeBicis
	 *            - Lista de {@link PedidoDTO pedidosDto} con los datos de las
	 *            bicis que se quiere reservar
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Long> getBicisIdsDePedidosDTO(List<PedidoDTO> listaDeDtoDeBicis) {

		return (List<Long>) CollectionUtils.collect(listaDeDtoDeBicis, new Transformer() {

			@Override
			public Object transform(Object pedidoDto) {
				PedidoDTO dto = (PedidoDTO) pedidoDto;
				return dto.getId();
			}

		});
	}

	/**
	 * Determina si el tipo de reserva del {@link PedidoDTO pedidoDto} es
	 * valido. Ver {@link TiposDeReserva tipos de reserva} para mas informacion.
	 * 
	 * @param pedido
	 *            - PedidoDTO en el que se debe validar el tipo de reserva
	 * @return
	 */
	private Boolean validarTipoDeReserva(PedidoDTO pedido) {
		for (TiposDeReserva tipoDeReserva : TiposDeReserva.values()) {
			if (pedido.getTipoDeReserva().equals(tipoDeReserva.getTipo())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Determina si la cantidad de bicis a reservar está entre 3 y 5
	 * 
	 * @param listaDeDtoDeBicis
	 *            - Lista de {@link PedidoDTO pedidosDto} con los datos de las
	 *            bicicletas que se quieren reservar
	 * @return
	 */
	private Boolean validarTipoDeReserva(List<PedidoDTO> listaDeDtoDeBicis) {
		return (listaDeDtoDeBicis.size() >= 3 && listaDeDtoDeBicis.size() <= 5);
	}

	/**
	 * Valida si una bici NO está reservada, es decir, está disponible para su
	 * inmediata reserva.
	 * 
	 * @param bici
	 *            - Bicicleta que se quiere validar
	 * @return
	 */
	private Boolean validarEstadoBici(Bicicleta bici) {
		return !bici.getReservada();
	}

	/**
	 * Valida si una <b>lista</b> de bicis NO está reservada, es decir, está
	 * disponible para su inmediata reserva.
	 * 
	 * @param bicis
	 *            - Listado de bicicleta que se quiere validar
	 * @return
	 */
	private boolean validarEstadoBici(List<Bicicleta> bicis) {
		for (Bicicleta bici : bicis) {
			if (bici.getReservada()) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

}
