package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import pe.gob.sunat.recaudacion3.tributaria.administracion.bean.FormularioJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.bean.ResultadoPagoBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Victor Ramos on 03/10/2017.
 */
public class BandejaJsonBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("formulario")
    private List<FormularioJsonBean> listaFormulario;
    @JsonProperty("resultadoPago")
    private ResultadoPagoBean resultadoPagoBean;


    public List<FormularioJsonBean> getListaFormulario() {
        return listaFormulario;
    }

    public void setListaFormulario(List<FormularioJsonBean> listaFormulario) {
        this.listaFormulario = listaFormulario;
    }

    public ResultadoPagoBean getResultadoPagoBean() {
        return resultadoPagoBean;
    }

    public void setResultadoPagoBean(ResultadoPagoBean resultadoPagoBean) {
        this.resultadoPagoBean = resultadoPagoBean;
    }

}
