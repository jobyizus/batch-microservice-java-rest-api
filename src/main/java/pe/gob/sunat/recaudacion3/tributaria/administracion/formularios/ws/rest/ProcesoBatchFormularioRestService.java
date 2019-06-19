package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.ws.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;

import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.AnexaJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service.ProcesoBatchAnexasFormularioService;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service.ProcesoBatchFormularioService;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service.ProcesoBatchReprocesosService;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.ConstantesUtils;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.UtilLog;

/**
 * Componente rest principal que representa el Proceso Batch Formulario.
 *
 * 
 * 
 * @author Sapia
 * 
 * @version 1.0
 * 
 * @since 22-01-2016
 */
@Transactional
@Path(ProcesoBatchFormularioConstantes.DEFAULT_URI_SERVICIO)
@Produces(APPLICATION_JSON + ";charset=utf-8")
public class ProcesoBatchFormularioRestService {

	@Inject
	ProcesoBatchFormularioService procesoBatchFormularioService;
	@Inject
	ProcesoBatchAnexasFormularioService anexasFormularioService;
	@Inject
	ProcesoBatchReprocesosService procesoBatchReprocesosService;
	@Inject
	private UtilLog utilLog;

	@Context
	private HttpServletRequest request;

	@GET
	@Path("/registrarformulario/{idbandeja}")
	public Response registrarFormularioBatch(@PathParam("idbandeja") Long idBandeja) {

		utilLog.agregarLogBean(request, "", "");
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Entro al metodo que registra Formularios y Boletas",
				Thread.currentThread().getStackTrace());

		return ok(procesoBatchFormularioService.registrarFormularioBatch(idBandeja)).build();

	}

	@GET
	@Path("/obtenerbandejanoprocesada/")
	public Response obtenerbandejanoprocesada() {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Entro al metodo que obtiene bandejas No Procesadas",
				Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "", "");

		List<Long> listaBandejas = procesoBatchReprocesosService.obtenerBandejaNoprocesada();

		return ok(listaBandejas).build();

	}

	@GET
	@Path("/obtenerpagosbandejanoprocesada/")
	public Response obtenerPagosBandejaNoProcesada() {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,
				"Entro al metodo que obtiene Pagos de Bandejas no procesadas en FORMULARIO",
				Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "", "");

		List<Long> listaBandejas = procesoBatchReprocesosService.obtenerPagosBandejaNoProcesada();

		return ok(listaBandejas).build();

	}

	// Anexas FORMULARIO
	@GET
	@Path("/reprocesaranexasformulario/{idbandeja}")
	public Response reprocesarAnexasFormulario(@PathParam("idbandeja") Long idBandeja) {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Entro al metodo que guarda Anexas no procesadas en FORMULARIO",
				Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "", "");

		Response respuesta = ok(anexasFormularioService.validarReprocesoAnexas(idBandeja)).build();
		return respuesta;

	}
	
	@GET
	@Path("/obtenercantidadanexa/{mes}/{dia}")
	@Timed
	public Response obtenercantidadanexa(@PathParam("mes") String mes, @PathParam("dia") String dia) {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Entro al metodo que obtiene cantidad anexa", Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "","");

		List<AnexaJsonBean>  listaBandejas = anexasFormularioService.obtenerCantidadAnexa(mes,dia);

		return ok(listaBandejas).build();

	}

	// Reprocesar Casillas FORMULARIO
	@GET
	@Path("/reprocesarcasillasformulario/{idbandeja}")
	public Response reprocesarCasillasFormulario(@PathParam("idbandeja") Long idBandeja) {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,
				"Entro al metodo que guarda Casilla no procesadas en FORMULARIO",
				Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "", "");

		Response respuesta = ok(procesoBatchFormularioService.reprocesarCasillasFormulario(idBandeja)).build();
		return respuesta;

	}

	@GET
	@Path("/obteneranexasformulario/")
	public Response obtenerAnexasFormulario() {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,
				"Entro al metodo que obtiene Anexas no procesadas en Formularios",
				Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "", "");

		// List<Long> listaBandejas =
		// anexasFormularioService.obtenerAnexasFormulario();
		List<Long> listaBandejas = anexasFormularioService
				.obtenerAnexaCasillaFormulario(ProcesoBatchFormularioConstantes.COD_PRO_BAN_ANEXAS);
		return ok(listaBandejas).build();

	}

	@GET
	@Path("/obtenercasillasformulario/")
	public Response obtenerCasillasFormulario() {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,
				"Entro al metodo que obtiene Casillas no procesadas en Formularios",
				Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "", "");

		List<Long> listaBandejas = anexasFormularioService
				.obtenerAnexaCasillaFormulario(ProcesoBatchFormularioConstantes.COD_PRO_BAN_CASILLAS);
		return ok(listaBandejas).build();

	}

	@GET
	@Path("/obtenerbandejanoprocesadapdtinternet/")
	public Response obtenerBandejaNoProcesadaPdtInternet() {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,
				"Entro al metodo que obtiene Bandejas No Procesadas en PDTINTERNET",
				Thread.currentThread().getStackTrace());
		utilLog.agregarLogBean(request, "", "");

		List<Long> listaBandejas = procesoBatchReprocesosService.obtenerBandejaNoProcesadaPdtInternet();

		return ok(listaBandejas).build();

	}

}
