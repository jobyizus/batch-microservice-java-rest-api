package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean;

import java.io.Serializable;
import java.util.List;


public class ArchivaPDTBean implements Serializable {

private String rutaFileZIP;
private String nombreFilePDT;
private String rutaFileEncriptado;
private String rutaFileDesencriptado;
private String rutaTemporal;
private String barritaTemporal;
private String codFor;
private String rutaFinalDescomprimido;
private String valorTramaExtension;
private String rutaFileComodin;
private String codExtension;





	/**
	* Default empty ArchivaPDTBean constructor
	*/
	public ArchivaPDTBean() {
		super();
	}

	/**
	* Default ArchivaPDTBean constructor
	*/
	public ArchivaPDTBean(String rutaFileZIP, String nombreFilePDT, String rutaFileEncriptado, String rutaFileDesencriptado, String rutaTemporal, String barritaTemporal, String codFor, String rutaFinalDescomprimido, String valorTramaExtension, String rutaFileComodin, String codExtension) {
		super();
		this.rutaFileZIP = rutaFileZIP;
		this.nombreFilePDT = nombreFilePDT;
		this.rutaFileEncriptado = rutaFileEncriptado;
		this.rutaFileDesencriptado = rutaFileDesencriptado;
		this.rutaTemporal = rutaTemporal;
		this.barritaTemporal = barritaTemporal;
		this.codFor = codFor;
		this.rutaFinalDescomprimido = rutaFinalDescomprimido;
		this.valorTramaExtension = valorTramaExtension;
		this.rutaFileComodin = rutaFileComodin;
		this.codExtension = codExtension;
	}






	/**
	* Returns value of rutaFileZIP
	* @return
	*/
	public String getRutaFileZIP() {
		return rutaFileZIP;
	}

	/**
	* Sets new value of rutaFileZIP
	* @param
	*/
	public void setRutaFileZIP(String rutaFileZIP) {
		this.rutaFileZIP = rutaFileZIP;
	}

	/**
	* Returns value of nombreFilePDT
	* @return
	*/
	public String getNombreFilePDT() {
		return nombreFilePDT;
	}

	/**
	* Sets new value of nombreFilePDT
	* @param
	*/
	public void setNombreFilePDT(String nombreFilePDT) {
		this.nombreFilePDT = nombreFilePDT;
	}

	/**
	* Returns value of rutaFileEncriptado
	* @return
	*/
	public String getRutaFileEncriptado() {
		return rutaFileEncriptado;
	}

	/**
	* Sets new value of rutaFileEncriptado
	* @param
	*/
	public void setRutaFileEncriptado(String rutaFileEncriptado) {
		this.rutaFileEncriptado = rutaFileEncriptado;
	}

	/**
	* Returns value of rutaFileDesencriptado
	* @return
	*/
	public String getRutaFileDesencriptado() {
		return rutaFileDesencriptado;
	}

	/**
	* Sets new value of rutaFileDesencriptado
	* @param
	*/
	public void setRutaFileDesencriptado(String rutaFileDesencriptado) {
		this.rutaFileDesencriptado = rutaFileDesencriptado;
	}

	/**
	* Returns value of rutaTemporal
	* @return
	*/
	public String getRutaTemporal() {
		return rutaTemporal;
	}

	/**
	* Sets new value of rutaTemporal
	* @param
	*/
	public void setRutaTemporal(String rutaTemporal) {
		this.rutaTemporal = rutaTemporal;
	}

	/**
	* Returns value of barritaTemporal
	* @return
	*/
	public String getBarritaTemporal() {
		return barritaTemporal;
	}

	/**
	* Sets new value of barritaTemporal
	* @param
	*/
	public void setBarritaTemporal(String barritaTemporal) {
		this.barritaTemporal = barritaTemporal;
	}

	/**
	* Returns value of codFor
	* @return
	*/
	public String getCodFor() {
		return codFor;
	}

	/**
	* Sets new value of codFor
	* @param
	*/
	public void setCodFor(String codFor) {
		this.codFor = codFor;
	}

	/**
	* Returns value of rutaFinalDescomprimido
	* @return
	*/
	public String getRutaFinalDescomprimido() {
		return rutaFinalDescomprimido;
	}

	/**
	* Sets new value of rutaFinalDescomprimido
	* @param
	*/
	public void setRutaFinalDescomprimido(String rutaFinalDescomprimido) {
		this.rutaFinalDescomprimido = rutaFinalDescomprimido;
	}

	/**
	* Returns value of valorTramaExtension
	* @return
	*/
	public String getValorTramaExtension() {
		return valorTramaExtension;
	}

	/**
	* Sets new value of valorTramaExtension
	* @param
	*/
	public void setValorTramaExtension(String valorTramaExtension) {
		this.valorTramaExtension = valorTramaExtension;
	}

	/**
	* Returns value of rutaFileComodin
	* @return
	*/
	public String getRutaFileComodin() {
		return rutaFileComodin;
	}

	/**
	* Sets new value of rutaFileComodin
	* @param
	*/
	public void setRutaFileComodin(String rutaFileComodin) {
		this.rutaFileComodin = rutaFileComodin;
	}

	/**
	* Returns value of codExtension
	* @return
	*/
	public String getCodExtension() {
		return codExtension;
	}

	/**
	* Sets new value of codExtension
	* @param
	*/
	public void setCodExtension(String codExtension) {
		this.codExtension = codExtension;
	}
}
