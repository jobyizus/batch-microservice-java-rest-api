package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.main.config;


import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.MicroserviceConfig;

import javax.enterprise.inject.Vetoed;
import java.io.Serializable;

/**
 * Clase modelo que representa la configuraci?n del Proceso Batch Formulario.
 *
 * @author Sapia
 * @version 1.0
 * @since 20-12-2016
 */

@Vetoed
public class ConfiguracionBatch extends MicroserviceConfig implements Serializable{

	private Integer tamanioFileSize;
	private String fileserver1;
	private String fileserver2;
	private String fileserverbck;
	private String rutaFileCargado;
	private String rutaFileDecryp;
	private String rutaFileEncryp;
	private Integer reintentosInvocacionGestorSesiones;
	private Integer indBloqueReproceso;
	private String indInstancia;
	private Integer numDias;
	private Integer numHoras;



	/**
	* Default empty ConfiguracionBatch constructor
	*/
	public ConfiguracionBatch() {
		super();
	}

	/**
	* Default ConfiguracionBatch constructor
	*/
	public ConfiguracionBatch(Integer tamanioFileSize, String fileserver1, String fileserver2, String fileserverbck, String rutaFileCargado, String rutaFileDecryp, String rutaFileEncryp, Integer reintentosInvocacionGestorSesiones, Integer indBloqueReproceso, String indInstancia, Integer numDias, Integer numHoras) {
		super();
		this.tamanioFileSize = tamanioFileSize;
		this.fileserver1 = fileserver1;
		this.fileserver2 = fileserver2;
		this.fileserverbck = fileserverbck;
		this.rutaFileCargado = rutaFileCargado;
		this.rutaFileDecryp = rutaFileDecryp;
		this.rutaFileEncryp = rutaFileEncryp;
		this.reintentosInvocacionGestorSesiones = reintentosInvocacionGestorSesiones;
		this.indBloqueReproceso = indBloqueReproceso;
		this.indInstancia = indInstancia;
		this.numDias = numDias;
		this.numHoras = numHoras;
	}





	/**
	* Returns value of tamanioFileSize
	* @return
	*/
	public Integer getTamanioFileSize() {
		return tamanioFileSize;
	}

	/**
	* Sets new value of tamanioFileSize
	* @param
	*/
	public void setTamanioFileSize(Integer tamanioFileSize) {
		this.tamanioFileSize = tamanioFileSize;
	}

	/**
	* Returns value of fileserver1
	* @return
	*/
	public String getFileserver1() {
		return fileserver1;
	}

	/**
	* Sets new value of fileserver1
	* @param
	*/
	public void setFileserver1(String fileserver1) {
		this.fileserver1 = fileserver1;
	}

	/**
	* Returns value of fileserver2
	* @return
	*/
	public String getFileserver2() {
		return fileserver2;
	}

	/**
	* Sets new value of fileserver2
	* @param
	*/
	public void setFileserver2(String fileserver2) {
		this.fileserver2 = fileserver2;
	}

	/**
	* Returns value of fileserverbck
	* @return
	*/
	public String getFileserverbck() {
		return fileserverbck;
	}

	/**
	* Sets new value of fileserverbck
	* @param
	*/
	public void setFileserverbck(String fileserverbck) {
		this.fileserverbck = fileserverbck;
	}

	/**
	* Returns value of rutaFileCargado
	* @return
	*/
	public String getRutaFileCargado() {
		return rutaFileCargado;
	}

	/**
	* Sets new value of rutaFileCargado
	* @param
	*/
	public void setRutaFileCargado(String rutaFileCargado) {
		this.rutaFileCargado = rutaFileCargado;
	}

	/**
	* Returns value of rutaFileDecryp
	* @return
	*/
	public String getRutaFileDecryp() {
		return rutaFileDecryp;
	}

	/**
	* Sets new value of rutaFileDecryp
	* @param
	*/
	public void setRutaFileDecryp(String rutaFileDecryp) {
		this.rutaFileDecryp = rutaFileDecryp;
	}

	/**
	* Returns value of rutaFileEncryp
	* @return
	*/
	public String getRutaFileEncryp() {
		return rutaFileEncryp;
	}

	/**
	* Sets new value of rutaFileEncryp
	* @param
	*/
	public void setRutaFileEncryp(String rutaFileEncryp) {
		this.rutaFileEncryp = rutaFileEncryp;
	}

	/**
	* Returns value of reintentosInvocacionGestorSesiones
	* @return
	*/
	public Integer getReintentosInvocacionGestorSesiones() {
		return reintentosInvocacionGestorSesiones;
	}

	/**
	* Sets new value of reintentosInvocacionGestorSesiones
	* @param
	*/
	public void setReintentosInvocacionGestorSesiones(Integer reintentosInvocacionGestorSesiones) {
		this.reintentosInvocacionGestorSesiones = reintentosInvocacionGestorSesiones;
	}

	/**
	* Returns value of indBloqueReproceso
	* @return
	*/
	public Integer getIndBloqueReproceso() {
		return indBloqueReproceso;
	}

	/**
	* Sets new value of indBloqueReproceso
	* @param
	*/
	public void setIndBloqueReproceso(Integer indBloqueReproceso) {
		this.indBloqueReproceso = indBloqueReproceso;
	}

	/**
	* Returns value of indInstancia
	* @return
	*/
	public String getIndInstancia() {
		return indInstancia;
	}

	/**
	* Sets new value of indInstancia
	* @param
	*/
	public void setIndInstancia(String indInstancia) {
		this.indInstancia = indInstancia;
	}

	/**
	* Returns value of numDias
	* @return
	*/
	public Integer getNumDias() {
		return numDias;
	}

	/**
	* Sets new value of numDias
	* @param
	*/
	public void setNumDias(Integer numDias) {
		this.numDias = numDias;
	}

	/**
	* Returns value of numHoras
	* @return
	*/
	public Integer getNumHoras() {
		return numHoras;
	}

	/**
	* Sets new value of numHoras
	* @param
	*/
	public void setNumHoras(Integer numHoras) {
		this.numHoras = numHoras;
	}
}
