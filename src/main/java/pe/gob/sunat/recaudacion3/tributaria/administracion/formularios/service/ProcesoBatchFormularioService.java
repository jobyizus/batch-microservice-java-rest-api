package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service;

import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.ResultadoProcesoBatchFormularioBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7777bandprespago;
/**
 * Interface que crea ProcesoBatchFormularioService.
 *
 * @author Sapia
 * @version 1.0
 * @since 20-12-2016
 */
public interface ProcesoBatchFormularioService {

	/**
	 *
	 *
	 * @param idBandeja
	 * @return ResultadoProcesoBatchFormularioBean
	 */
	ResultadoProcesoBatchFormularioBean registrarFormularioBatch(Long idBandeja);

	T7777bandprespago obtenerBandPresPago(Long idBandeja);

	ResultadoProcesoBatchFormularioBean reprocesarCasillasFormulario(Long numBandeja);


}
