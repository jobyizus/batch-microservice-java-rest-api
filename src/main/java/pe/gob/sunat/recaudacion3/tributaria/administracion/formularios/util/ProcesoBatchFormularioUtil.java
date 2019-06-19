package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util;

import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.ConfigService;
import java.util.Calendar;
import java.util.Date;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.main.config.ConfiguracionBatch;

/**
 * Clase ProcesoBatchFormularioUtil de ProcesoBatchFormulario.
 *
 * @author Sapia
 * @version 1.0
 * @since 12-10-2016.
 */
public class ProcesoBatchFormularioUtil {

	private ConfigService configuracion = new ConfigService();


	public static String reemplazarCadena(final String cadena, final String busqueda, final String reemplazo) {
		return cadena.replaceAll(busqueda, reemplazo);
	}

	/**
	 *
	 * Metodo que transforma plano lista.
	 *
	 * @param mapaAnexasIn
	 * @param tramaAnexasForm
	 * @param formulario
	 * @return List<T7782anexaform>
	 */



	public String[] calcularDiferenciaImportes(Double mtoImpResNue, Double mtoImpResAnt){
			Double mtoDifImpRes = mtoImpResAnt - mtoImpResNue; //Monto diferencia impuesto resultante
			Double mtoDifPorc = (mtoDifImpRes * 100)/mtoImpResAnt;//Monto diferencia porcentual
			return new String[]{"mto_dif_imp_res:", String.valueOf(mtoDifImpRes), "mto_dif_porc:", String.valueOf(mtoDifPorc) };
	}


	public Date sumarRestarDiasFecha(Date hoy) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hoy);
		calendar.add(Calendar.DAY_OF_YEAR, configuracion.getConfig(ConfiguracionBatch.class).getNumDias());
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	public Date sumarRestarHorasFecha(Date fecha){
		      Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fecha);
		      calendar.add(Calendar.HOUR, configuracion.getConfig(ConfiguracionBatch.class).getNumHoras());
		      return calendar.getTime();
	}



}
