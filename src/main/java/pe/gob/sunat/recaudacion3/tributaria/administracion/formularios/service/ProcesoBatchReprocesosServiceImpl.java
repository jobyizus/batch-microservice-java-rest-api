	package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Date;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.main.config.ConfiguracionBatch;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7778FormulariosDAO;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.ConfigService;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.kibana.WSLoggerBean;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.ConstantesUtils;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioUtil;


public class ProcesoBatchReprocesosServiceImpl implements ProcesoBatchReprocesosService {

	@Context
	private HttpServletRequest request;

	@Inject
	private T7778FormulariosDAO t7778FormulariosDAO;

	private ConfigService configuracion = new ConfigService();
	ProcesoBatchFormularioUtil procesoBatchFormularioUtil = new ProcesoBatchFormularioUtil();

	public List<Long> obtenerBandejaNoprocesada() {

		WSLoggerBean logBean = (WSLoggerBean) request.getAttribute(ConstantesUtils.LOG_BEAN);
		logBean.setNumBandeja(String.valueOf(0));
		request.setAttribute(ConstantesUtils.LOG_INPUT, logBean.getNumBandeja());
		Date hoy=new Date();
		Integer primerosReproceso = configuracion.getConfig(ConfiguracionBatch.class).getIndBloqueReproceso(); // 2017-10-12
		Date fechaDesde = procesoBatchFormularioUtil.sumarRestarDiasFecha(hoy);
		Date fechaHasta = procesoBatchFormularioUtil.sumarRestarHorasFecha(hoy);
		List<Long> resultados = t7778FormulariosDAO.obtenerBandejaNoprocesada(primerosReproceso, fechaDesde,fechaHasta);

		return resultados;
	}

	public List<Long> obtenerPagosBandejaNoProcesada() {

		WSLoggerBean logBean = (WSLoggerBean) request.getAttribute(ConstantesUtils.LOG_BEAN);
		logBean.setNumBandeja(String.valueOf(0));
		request.setAttribute(ConstantesUtils.LOG_INPUT, logBean.getNumBandeja());
		Date hoy=new Date();
		Integer primerosReproceso = configuracion.getConfig(ConfiguracionBatch.class).getIndBloqueReproceso(); // 2017-10-12
		Date fechaDesde = procesoBatchFormularioUtil.sumarRestarDiasFecha(hoy);
		Date fechaHasta = procesoBatchFormularioUtil.sumarRestarHorasFecha(hoy);
		List<Long> resultados = t7778FormulariosDAO.obtenerPagosBandejaNoProcesada(primerosReproceso, fechaDesde,fechaHasta);


		return resultados;
	}

	public List<Long> obtenerBandejaNoProcesadaPdtInternet() {
		WSLoggerBean logBean = (WSLoggerBean) request.getAttribute(ConstantesUtils.LOG_BEAN);
		logBean.setNumBandeja(String.valueOf(0));
		request.setAttribute(ConstantesUtils.LOG_INPUT, logBean.getNumBandeja());
		String instancia = configuracion.getConfig(ConfiguracionBatch.class).getIndInstancia();
		Date hoy = new Date();
		Date fechaDesde = procesoBatchFormularioUtil.sumarRestarDiasFecha(hoy);
		Date fechaHasta = procesoBatchFormularioUtil.sumarRestarHorasFecha(hoy);
		List<Long> resultados = t7778FormulariosDAO.obtenerBandejaNoProcesadaPdtInternet(instancia, fechaDesde, fechaHasta);
		if (resultados == null || resultados.isEmpty()) {
			resultados = new ArrayList<>();
		}
		return resultados;
	}
}
