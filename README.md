Este proyecto es una API rest, el cual tiene implementadas distintas funcionalidades con el fin de cumplir el enunciado pedido. La misma está basada en Spring boot.

Los metodos que dispone esta API son los siguientes:
- bicis -> GET, Devuelve todas las bicicletas, sin importar si fueron o no reservadas
- bicisLibres -> GET, Devuelve las bicicletas que no han sido reservadas.
- reservarUnaBici -> PUT, nos permite reservar una sola bici, por cualquiera de los tipos de reserva posibles. Espera un JSON como el siguiente:
	{
	"id": 1,
	"tipoDeReserva": 2,
	"veces":4
	}
- reservarVariasBicis -> PUT, nos permite reservar entre 3 y 5 bicis. Espera un JSON como el siguiente:
	[{
		"id": 1,
		"tipoDeReserva": 2,
		"veces": 2
	}, {
		"id": 2,
		"tipoDeReserva": 3,
		"veces":3
	},
	{
		"id": 3,
		"tipoDeReserva": 1
	}]

Los metodos rest que nos permiten reservar una bici devuelven, en caso que la reserva sea exitosa, la/s bici/s reservada/s, junto con el importe que costó (en el caso de la reserva múltiple, el importe viene en el primer resultado)

Se modeló las bicicletas a través de una clase Bicicleta, la cual tiene 3 caracteristicas, un ID, un color, y un booleano que permite determinar si está reservada o no.

Se dispone de un controlador, el BiciController, el cual maneja todas las peticiones de la API. Este controlador delega la logica propia de la reserva de la bici en un servicio, el cual se llama BiciService, y delega lo que tiene que ver con el pago en PagosService. El controlador recibe DTOs y devuelve DTOs, intentando separar la comunicacion de frontend-backend con el modelo de la aplicación.

Esta API no esta segurizada, es decir, podría ser accedida por cualquier usuario que conozca la URL. Lo idea seria utilizar algun mecanismo de autenticación como JWT pero esto entiendo que no era necesario para este ejemplo.

La API se puede probar con una BD que se carga en memoria. Se cargaron 5 bicicletas a traves de un archivo .SQL con el fin de poder utilizar la API con datos de prueba.

Se utilizo JPA para manejar la persistencia, con Hibernate como ORM. Se hizo un BiciRepository, el cual permite centralizar en un solo lugar todo lo que tenga que ver con accesos a la BD.

En todo momento se inyectó dependencias a través de Spring, con el fin de manejar correctamente la inicializacion y el acceso a cualquier recurso del backend.

La bicicleta tiene un atributo "version" con el fin de poder tener forma de controlar si fue modificado concurrentemente por otra transaccion, y en ese caso, se cancelará cualquier nuevo cambio. 

Se manejaron los importes como BigDecimal, dado que es el tipo de datos mas indicado para esto en Java. No se decidió una politica de redondeo (lo cual es importante cuando se manejan importes) dado que el enunciado no brinda suficiente informacion al respecto. 

Faltaria para completar la API una forma de devolver las bicis una vez finalizada la reserva. Al no tener informacion sobre como sería esa devolución, se decidió no modelar esta funcionalidad.


Para poder correr los test y al mismo tiempo medir el coverage, se pueden descargar el plugin EclEmma Java Code Coverage. Una vez instalado este plugin, se hace lo siguiente:
	Click derecho en el proyecto -> Coverage As -> JUnit Test
Esto nos permitirá controlar en la solapa de Junit el resultado de los tests, y nos dira en la solapa Coverage el porcentaje de Coverage (valga la redundancia).  




