package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service;

import java.util.List;

public interface ProcesoBatchReprocesosService {

  List<Long> obtenerBandejaNoprocesada();

	List<Long> obtenerPagosBandejaNoProcesada();

  	List<Long> obtenerBandejaNoProcesadaPdtInternet();
    
}
