package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean;

/**
 * Created by Admin on 07/09/2017.
 */
public class RespuestaBean<T> {

    private String codRespuesta;
    private T desRespuesta;
    private int CodGenerado;

    public RespuestaBean(){

    }
    public RespuestaBean(String codigo, T resultado) {
        this.codRespuesta = codigo;
        this.desRespuesta = resultado;
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
