package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean;

public class AnexaJsonBean {
	private String codFor;
	private Long numOrd;
	private String NomAnx;
	private Integer cont;
	
	public String getCodFor() {
		return codFor;
	}
	public void setCodFor(String codFor) {
		this.codFor = codFor;
	}
	public Long getNumOrd() {
		return numOrd;
	}
	public void setNumOrd(Long numOrd) {
		this.numOrd = numOrd;
	}
	public String getNomAnx() {
		return NomAnx;
	}
	public void setNomAnx(String nomAnx) {
		NomAnx = nomAnx;
	}
	public Integer getCont() {
		return cont;
	}
	public void setCont(Integer cont) {
		this.cont = cont;
	}
}
