package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean;


import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.config.bean.MensajeBean;

import java.util.Map;

/**
 * Clase de transferencia que representa la información de la ResultadoProcesoBatchFormularioBean.
 *
 * @author Sapia
 * @version 1.0
 * @since 19-11-2016
 */
public class ResultadoProcesoBatchFormularioBean<T> extends MensajeBean {

	private Map<String, Object> resultado;
	private String codRespuesta;
	private T desRespuesta;
	private int CodGenerado;

	public ResultadoProcesoBatchFormularioBean(String cod, String msg) {

		super(cod, msg);

	}

	public ResultadoProcesoBatchFormularioBean() {

		super();

	}

	public ResultadoProcesoBatchFormularioBean(String cod, String msg, Map<String, Object> resultado) {

		super(cod, msg);
		this.resultado = resultado;

	}

	public ResultadoProcesoBatchFormularioBean(String cod,String msg, T resultado) {
		super(cod,msg);
	   this.desRespuesta = resultado;
	}

	/**
	 * @return resultado
	 */
	public Map<String, Object> getResultado() {
		return resultado;
	}

	/**
	 * @param resultado: resultado a set
	 */
	public void setResultado(Map<String, Object> resultado) {
		this.resultado = resultado;
	}

	/**
	 * @return codRespuesta
	 */
	public String getCodRespuesta() {
		return codRespuesta;
	}

	/**
	 * @param codRespuesta: codRespuesta a set
	 */
	public void setCodRespuesta(String codRespuesta) {
		this.codRespuesta = codRespuesta;
	}

	/**
	 * @return desRespuesta
	 */
	public T getDesRespuesta() {
		return desRespuesta;
	}

	/**
	 * @param desRespuesta: desRespuesta a set
	 */
	public void setDesRespuesta(T desRespuesta) {
		this.desRespuesta = desRespuesta;
	}

	/**
	 * @return codGenerado
	 */
	public int getCodGenerado() {
		return CodGenerado;
	}

	/**
	 * @param codGenerado: codGenerado a set
	 */
	public void setCodGenerado(int codGenerado) {
		CodGenerado = codGenerado;
	}


}
