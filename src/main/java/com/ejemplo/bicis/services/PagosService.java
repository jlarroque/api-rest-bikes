package com.ejemplo.bicis.services;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.model.dto.PedidoDTO;

/**
 * Maneja como se realizan los pagos ante cada reserva
 * 
 * @author jose
 *
 */
@Service
@Transactional
public class PagosService {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(PagosService.class);

	public BigDecimal pagar(List<PedidoDTO> listaDePedidosDTO) {

		BigDecimal montoAPagar = BigDecimal.ZERO;
		BigDecimal resultado = BigDecimal.ZERO;
		// Si es una reserva individual
		if (listaDePedidosDTO.size() == 1) {
			PedidoDTO pedidoDTO = listaDePedidosDTO.get(0);
			resultado = montoAPagar
					.add(calcularCostoDeReservaDeBici(pedidoDTO.getTipoDeReserva(), pedidoDTO.getVeces()));
		}
		// En cambio, si es una reserva de entre 3 y 5 bicis
		else {
			for (PedidoDTO pedido : listaDePedidosDTO) {
				montoAPagar = calcularCostoDeReservaDeBici(pedido.getTipoDeReserva(), pedido.getVeces());
				resultado = resultado.add(montoAPagar);
			}
			resultado = percentage(resultado, new BigDecimal(70));
		}

		// Se logea el monto que se le cobrara al usuario, dado que segun el
		// enunciado no puedo determinar que se debería hacer con este monto
		log.info("Se recaudo el monto de: ", resultado.toPlainString(), " por una nueva reserva de bicicletas");
		return resultado;
	}

	private BigDecimal calcularCostoDeReservaDeBici(Integer tipoDeReserva, Integer veces) {

		BigDecimal resultado = BigDecimal.ZERO;
		switch (tipoDeReserva) {
		case 1: {
			resultado = resultado.add(new BigDecimal(veces * 5));
			break;
		}
		case 2: {
			resultado = resultado.add(new BigDecimal(veces * 20));
			break;
		}

		case 3: {
			resultado = resultado.add(new BigDecimal(veces * 60));
			break;
		}
		default: {
			resultado = BigDecimal.ZERO;
		}

		}
		return resultado;
	}

	public static BigDecimal percentage(BigDecimal base, BigDecimal pct) {
		return base.multiply(pct).divide(new BigDecimal(100));
	}
}
