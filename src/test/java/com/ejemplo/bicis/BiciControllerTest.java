package com.ejemplo.bicis;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ejemplo.bicis.model.Bicicleta;
import com.ejemplo.bicis.repository.BiciRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BiciControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testBicis() throws Exception {
		/**
		 * GET
		 */

		// bicis
		mockMvc.perform(get("/bicis")).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// bicisLibres
		mockMvc.perform(get("/bicisLibres")).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		/**
		 * PUT
		 */

		// reservarUnaBici

		// Sin parametros da error
		mockMvc.perform(put("/reservarUnaBici")).andExpect(status().is4xxClientError());
		// Sin el tipo de reserva da error
		mockMvc.perform(put("/reservarUnaBici").param("id", "1")).andExpect(status().is4xxClientError());
		// Sin el id da error
		mockMvc.perform(put("/reservarUnaBici").param("tipoDeReserva", "1")).andExpect(status().is4xxClientError());
		// Con id y tipo de reserva anda bien
		mockMvc.perform(put("/reservarUnaBici").param("id", "1").param("tipoDeReserva", "1").param("veces", "2"))
				.andExpect(status().isOk());
		// Si reservo dos veces lo mismo da error
		mockMvc.perform(put("/reservarUnaBici").param("id", "1").param("tipoDeReserva", "1").param("veces", "2"))
				.andExpect(status().isInternalServerError());

		// reservarVariasBicis

		// Sin parametros da error
		mockMvc.perform(put("/reservarVariasBicis")).andExpect(status().is4xxClientError());
		// Sin el tipo de reserva da error
		mockMvc.perform(put("/reservarVariasBicis").param("id", "5")).andExpect(status().is4xxClientError());
		// Sin el id da error
		mockMvc.perform(put("/reservarVariasBicis").param("tipoDeReserva", "1")).andExpect(status().is4xxClientError());

		ArrayList<JSONObject> array = new ArrayList<JSONObject>(1);
		JSONObject pedido = new JSONObject();
		pedido.put("id", "2");
		pedido.put("tipoDeReserva", "3");
		pedido.put("veces", "4");
		array.add(pedido);

		// Al menos deben ser 3 reservas, pidiendo solo 1
		mockMvc.perform(put("/reservarVariasBicis").contentType(MediaType.APPLICATION_JSON)
				.content(array.toString().getBytes("utf-8"))).andExpect(status().isInternalServerError());

		pedido = new JSONObject();
		pedido.put("id", "3");
		pedido.put("tipoDeReserva", "2");
		pedido.put("veces", "3");
		array.add(pedido);

		// Al menos deben ser 3 reservas, pidiendo solo 2
		mockMvc.perform(put("/reservarVariasBicis").contentType(MediaType.APPLICATION_JSON)
				.content(array.toString().getBytes("utf-8"))).andExpect(status().isInternalServerError());

		pedido = new JSONObject();
		pedido.put("id", "4");
		pedido.put("tipoDeReserva", "1");
		pedido.put("veces", "2");
		array.add(pedido);

		// Pedido de tres reservas
		mockMvc.perform(put("/reservarVariasBicis").contentType(MediaType.APPLICATION_JSON)
				.content(array.toString().getBytes("utf-8"))).andExpect(status().isOk());
	}
}
