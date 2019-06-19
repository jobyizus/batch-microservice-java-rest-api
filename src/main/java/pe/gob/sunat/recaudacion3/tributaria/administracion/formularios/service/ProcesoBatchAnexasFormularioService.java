package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service;

import pe.gob.sunat.recaudacion3.tributaria.administracion.bean.FormularioJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.AnexaJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.ResultadoProcesoBatchFormularioBean;
import java.util.List;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7777bandprespago;

public interface ProcesoBatchAnexasFormularioService {

  public  void registrarAnexasFormularioBatch(FormularioJsonBean t7778formulario);

  public  ResultadoProcesoBatchFormularioBean validarReprocesoAnexas(Long numBandeja);

	public List<Long> obtenerAnexasFormulario();

  public  T7777bandprespago obtenerBandPresPago(Long idBandeja);

	public List<Long> obtenerAnexaCasillaFormulario(String codProBand);
	
	public List<AnexaJsonBean> obtenerCantidadAnexa(String mes, String dia);
}
