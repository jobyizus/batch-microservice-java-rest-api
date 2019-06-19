  package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import pe.gob.sunat.recaudacion3.tributaria.administracion.bean.FormularioJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.BandejaJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.ResultadoProcesoBatchFormularioBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioUtil;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7777bandprespagoDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7778FormulariosDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7779casillaformDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7792paramdetaDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7794ProcesoBandDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7778formulario;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7788cargapdt;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7794procesoband;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7794procesobandPK;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7781rectitributoPK;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7792paramdeta;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7779casillaform;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7780tributodecpag;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7781rectitributo;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7782anexaform;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7786detapagoprev;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7777bandprespago;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7779casillaformPK;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.ConfigService;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.ObjectNotFoundException;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.kibana.WSLoggerBean;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.metrics.MetricUtil;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.ConstantesUtils;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.JsonUtilParser;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.UtilLog;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_FOR_BOLETA;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_MED_PAG_NPS;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_PRO_BAN_ANEXAS;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_TIP_FOR_PDT;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.FEC_POR_DEFAULT;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.FORM_FECHA_DIA_MES_ANIO_HORA;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VALIDA_LIBRO_ELECTRONICO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VAL_LIB_0;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VAL_LIB_1;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VAL_ORIGINAL;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VAL_PDT_CERO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VAL_SUSTITUTORIA;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_0;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_1;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_ACT_0;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_CERO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_DOS;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_NO_EXISTE;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_USUARIO_SYSTEM;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_ERRONEO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EN_PROCESO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_PRO_BAN_CASILLAS;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_RESP_CON_ERROR;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_RESP_EXITOSO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EXITOSO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_UNO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VAL_PARAM_TABLA_1115;
import static pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.EntityManagerInitializer.EMFORMULARIO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VAL_PROCESO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.bean.AnexaBean;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.Cadena;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.cache.FormularioCache;
                                                                                                                                                          /**
   * Clase que implementa la interface ProcesoBatchFormularioService.

   *

   * @author Sapia

   * @version 1.0

   * @since 20-12-2016
   */
  @Transactional
  public class ProcesoBatchFormularioServiceImpl implements ProcesoBatchFormularioService {

    @Inject
    private T7778FormulariosDAO t7778FormulariosDAO;
    @Inject
    private T7779casillaformDAO t7779casillaformDAO;
    @Inject
    private T7794ProcesoBandDAO t7794dao;
    @Inject
    private T7777bandprespagoDAO t7777dao;
    @Inject
    private JsonUtilParser jsonUtilParser;
    @Inject
    private FormularioCache formularioCache;
    @Inject
    private T7792paramdetaDAO t7792paramdetaDAO;
    @Inject
    private ProcesoBatchAnexasFormularioService procesoBatchAnexasFormularioService;
    @Inject
    private UtilLog utilLog;

    @Context
    private HttpServletRequest request;

    private ConfigService configuracion = new ConfigService();
    ProcesoBatchFormularioUtil procesoBatchFormularioUtil = new ProcesoBatchFormularioUtil();


    public ResultadoProcesoBatchFormularioBean registrarFormularioBatch(Long idBandeja) {
        WSLoggerBean logBean = (WSLoggerBean) request.getAttribute(ConstantesUtils.LOG_BEAN);
        logBean.setNumBandeja(String.valueOf(idBandeja));
        request.setAttribute(ConstantesUtils.LOG_INPUT, idBandeja.toString());
        utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,logBean,"Ingreso al metodo registrarFormularioBatch ", Thread.currentThread().getStackTrace());


        ResultadoProcesoBatchFormularioBean resultadoProcesoBatchFormularioBean = new ResultadoProcesoBatchFormularioBean();
        Character indicadorProceso = VALOR_0;
        String idProceso = ProcesoBatchFormularioConstantes.COD_PROCESO_FORMULARIO_BANDEJA;
        T7794procesoband procesoEnvio = null;
        T7777bandprespago t7777bandprespagoEntity = null;
        try {
  	        t7777bandprespagoEntity = this.obtenerBandPresPago(idBandeja);
  	  }
        catch (ObjectNotFoundException excepcion) {
  	        utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,logBean,"No se encontro informacion Bandeja de Presentación", Thread.currentThread().getStackTrace());
  	  }

        if (t7777bandprespagoEntity == null) {
          resultadoProcesoBatchFormularioBean.setCod(Integer.toString(Response.Status.NOT_FOUND.getStatusCode()));
          resultadoProcesoBatchFormularioBean.setMsg(ProcesoBatchFormularioConstantes.MENSAJE_FALLIDO);
        }
        else {
        	try {
	          int procesar = 0;
              procesoEnvio = this.buscarProcesoBandeja(idBandeja, idProceso);
              procesar = this.validarProceso(procesar, procesoEnvio);
              if (procesar == 1) {
            	  if(procesoEnvio == null){
            		  procesoEnvio = this.registraInicioProceso(idBandeja);
					}else{
						procesoEnvio.getNumIntentos();
						indicadorProceso = procesoEnvio.getIndProBan();
						if(!indicadorProceso.equals(ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EN_PROCESO)){
							this.actualizaInicioProceso(procesoEnvio);
						}
					}
            	  if (this.registrarProcesoBatchFormulario(t7777bandprespagoEntity,procesoEnvio)) {
                      this.registroProcesoBandejaBatchFormulario(t7777bandprespagoEntity.getNumBandeja(), ProcesoBatchFormularioConstantes.COD_PROCESO_FORMULARIO_BANDEJA,
                                            ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EXITOSO,
                                            ProcesoBatchFormularioConstantes.COD_USUARIO_SYSTEM, procesoEnvio);
                      resultadoProcesoBatchFormularioBean.setCod(ProcesoBatchFormularioConstantes.COD_RESP_EXITOSO);
                      resultadoProcesoBatchFormularioBean.setMsg(ProcesoBatchFormularioConstantes.MENSAJE_EXITOSO);
                  }
              }else{
            	  resultadoProcesoBatchFormularioBean.setCod(ProcesoBatchFormularioConstantes.COD_RESP_EXITOSO);
                  resultadoProcesoBatchFormularioBean.setMsg(ProcesoBatchFormularioConstantes.MENSAJE_EXITOSO);
              }
        	} catch(Exception error) {
        		utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,logBean,"Problemas con Proceso Bandeja", Thread.currentThread().getStackTrace());
        		this.registroProcesoBandejaBatchFormulario(t7777bandprespagoEntity.getNumBandeja(), ProcesoBatchFormularioConstantes.COD_PROCESO_FORMULARIO_BANDEJA,
                        ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_ERRONEO,
                        ProcesoBatchFormularioConstantes.COD_USUARIO_SYSTEM, procesoEnvio);
        		resultadoProcesoBatchFormularioBean.setCod(ProcesoBatchFormularioConstantes.COD_RESP_CON_ERROR);
        		resultadoProcesoBatchFormularioBean.setMsg(ProcesoBatchFormularioConstantes.MENSAJE_FALLIDO);
        	}
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("Operacion", ProcesoBatchFormularioConstantes.REGISTRO_PROCESO_OK);
        resultadoProcesoBatchFormularioBean.setResultado(resultado);
        return resultadoProcesoBatchFormularioBean;
    }

    public T7777bandprespago obtenerBandPresPago(Long idBandeja) {
        return t7777dao.findById(idBandeja);
    }

    public boolean registrarProcesoBatchFormulario(T7777bandprespago t7777bandprespagoEntity, T7794procesoband procesoEnvio) {
        utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Ingreso al metodo registrarProcesoBatchFormulario", Thread.currentThread().getStackTrace());
        boolean resultadoTransaccion = false;

        String jsonFromBandeja = ProcesoBatchFormularioUtil.reemplazarCadena((String) t7777bandprespagoEntity.getArcData(), "IfxBSONObject", "");
        BandejaJsonBean bandejaBean = this.formatearDatos(jsonFromBandeja);

        List<FormularioJsonBean> lstT7778formulario = bandejaBean.getListaFormulario();
        if (lstT7778formulario != null && !lstT7778formulario.isEmpty()) {

          List<FormularioJsonBean> lstT7778FormulariosBoletas = this.formatearDatosBoletas(jsonFromBandeja, lstT7778formulario);
          if (!lstT7778FormulariosBoletas.isEmpty()) {
            lstT7778FormulariosBoletas.stream().forEachOrdered(formulario -> {

              if (formulario != null) {
                formulario.setNumBandeja(t7777bandprespagoEntity.getNumBandeja());
                this.agregarLogBean(request,formulario);

                Long indExiste = t7778FormulariosDAO.buscarPorNumOrd(formulario.getNumOrd());
                if (indExiste == 0) {
                  formulario.setCodUsuregis(t7777bandprespagoEntity.getCodUsuregis());
                  this.seteoInicialFormulario(t7777bandprespagoEntity, bandejaBean, formulario);
                  this.setearCasillasFormulario(formulario);
                  this.setearTributosDeclaradosPagados(formulario);

                  if (!formulario.getCodFor().equals(COD_FOR_BOLETA)) {
                    this.setearCasillasSugeridas(formulario);
                    this.setearDetallesCoeficienteIGV(formulario);
                    this.setearCoeficienteIGVPeriodo(formulario);
                    this.setearRectificatoriasTributo(t7777bandprespagoEntity, formulario);
                    this.setearDetallePagosPrevios(formulario);
                    this.setearRetePercIGV(formulario);
                    procesoBatchAnexasFormularioService.registrarAnexasFormularioBatch(formulario);

                  }

                  if (formulario.getCargaPdt() != null) {
                    T7788cargapdt t7788cargapdt = formulario.getCargaPdt();
                    t7788cargapdt.setNumOrdFor(BigInteger.valueOf(formulario.getNumOrd()));
                    t7788cargapdt.setCodUsumodif(formulario.getCodUsuregis());
                    t7788cargapdt.setFecModif(new Date());
                    t7788cargapdt.setCodDep(t7777bandprespagoEntity.getCodDep());
                    t7788cargapdt.setIndProc(IND_VAL_PROCESO);
                    t7788cargapdt.setNumCargaPdt(formulario.getNumCargaPdt());

                    if(formulario.getNumCargaPdt()==IND_VAL_PDT_CERO){
                        t7788cargapdt.setNumCargaPdt(null);
                    }else{
                        t7788cargapdt.setNumCargaPdt(formulario.getNumCargaPdt());
                    }
                  }
                } else {
                  utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Numero de Orden existente: ".concat(String.valueOf(formulario.getNumOrd())), Thread.currentThread().getStackTrace());
                }
              }
            });
            List<T7778formulario> lstT7778FormulariosSaved = this.setearListaFormularioEntity(lstT7778FormulariosBoletas);
            resultadoTransaccion=this.registrarT7778formulario(lstT7778FormulariosSaved, t7777bandprespagoEntity);

          }
        }
        return resultadoTransaccion;
    }

  private void setearRetePercIGV(FormularioJsonBean formulario) {

  	if (formulario.getRetePercIGVList() != null && !formulario.getRetePercIGVList().isEmpty()) {
  	    formulario.getRetePercIGVList().stream().forEach(retepercepcion -> {
  	      if ((retepercepcion != null) && (retepercepcion.getT7795retepercigvPK() != null)) {
  	        retepercepcion.getT7795retepercigvPK().setNumOrd(formulario.getNumOrd());
  	        retepercepcion.setCodUsuregis(formulario.getCodUsuregis());
  	      }
  	    });
  	  }
  }

  private void setearDetallePagosPrevios(FormularioJsonBean formulario) {
  	DecimalFormat formatoNumero = new DecimalFormat("0000");
  	formulario.getDetallePagosPreviosList().forEach((T7786detapagoprev detallePagoPrevio) -> {
  	    if (detallePagoPrevio != null) {
  	      detallePagoPrevio.getT7786detapagoprevPK().setNumOrd(formulario.getNumOrd());
  	      detallePagoPrevio.setCodUsuregis(formulario.getCodUsuregis());
  	      detallePagoPrevio.setCodDepPag(formatoNumero.format(Integer.parseInt(detallePagoPrevio.getCodDepPag())));
  	    }
  	  });
  }

  private void setearCoeficienteIGVPeriodo(FormularioJsonBean formulario) {
  	if (!formulario.getCoeficienteIGVPeriodoList().isEmpty()) {
  	    formulario.getCoeficienteIGVPeriodoList().stream().forEach(coeficienteigv -> {
  	      if (coeficienteigv != null) {
  	        coeficienteigv.setNumOrd(formulario.getNumOrd());
  	        coeficienteigv.setCodUsuregis(formulario.getCodUsuregis());
  	        if (IND_VAL_ORIGINAL.equals(formulario.getCodIndRec())) {
  	          coeficienteigv.setCodEstrec(VALOR_1);
  	          coeficienteigv.setIndReccoe(VALOR_0);
  	          coeficienteigv.setIndVig(VALOR_1);
  	        }
  	      }
  	    });
  	  }
  }

  private void setearDetallesCoeficienteIGV(FormularioJsonBean formulario) {
  	if (!formulario.getDetallesCoeficienteIGVList().isEmpty()) {
  	    formulario.getDetallesCoeficienteIGVList().stream().forEach(coeficiente -> {
  	      if ((coeficiente != null) && (coeficiente.getT7784coefigvdetPK() != null)) {
  	        coeficiente.getT7784coefigvdetPK().setNumOrd(formulario.getNumOrd());
  	        coeficiente.setCodUsuregis(formulario.getCodUsuregis());
  	      }
  	    });
  	}
  }

  private void setearCasillasSugeridas(FormularioJsonBean formulario) {
  	formulario.getCasillasSugeridasFinalesList().forEach(sugerida -> {
  	    if (sugerida != null) {
  	      sugerida.getT7783casillasugfinPK().setNumOrd(formulario.getNumOrd());
  	      sugerida.setCodUsuregis(formulario.getCodUsuregis());
  	      if (!sugerida.getDesCassug().equalsIgnoreCase(sugerida.getDesCasfin())) {
  	        sugerida.setIndValDif(ProcesoBatchFormularioConstantes.IND_VAL_DIF_1);
  	      }
  	      if (IND_VALIDA_LIBRO_ELECTRONICO == sugerida.getT7783casillasugfinPK().getNumCas()) {
  	        formulario.setIndValLib(IND_VAL_LIB_1);
  	      } else {
  	        formulario.setIndValLib(IND_VAL_LIB_0);
  	      }
  	    }
  	  });
  }

  private void setearTributosDeclaradosPagados(FormularioJsonBean formulario) {
  	formulario.getTributosDeclaradosPagadosList().stream().forEach(tributo -> {
  	  if (tributo != null) {
  	    tributo.getT7780tributodecpagPK().setNumOrd(formulario.getNumOrd());
  	    tributo.setCodUsuregis(formulario.getCodUsuregis());
  	    if (COD_FOR_BOLETA.equals(formulario.getCodFor())) {
  	      tributo.setMtoBasimp(new BigDecimal(0));
  	      tributo.setMtoImpres(new BigDecimal(0));
  	      tributo.setMtoTotDeu(new BigDecimal(0));
  	      tributo.setPorTasa(new BigDecimal(0));
  	    }
  	    if (tributo.getMtoBasimp() == null) tributo.setMtoBasimp(new BigDecimal(0));
  	    if (tributo.getMtoImpres() == null) tributo.setMtoImpres(new BigDecimal(0));
  	    if (tributo.getMtoTotDeu() == null) tributo.setMtoTotDeu(new BigDecimal(0));
  	    if (tributo.getMtoPagtot() == null || COD_MED_PAG_NPS.equals(formulario.getCodMedpag())){
  	    	tributo.setMtoPagtot(new BigDecimal(0));
  	    }
  	    tributo.setMtoBasimp(tributo.getMtoBasimp().setScale(2, RoundingMode.HALF_UP));
  	    tributo.setMtoImpres(tributo.getMtoImpres().setScale(2, RoundingMode.HALF_UP));
  	    tributo.setMtoTotDeu(tributo.getMtoTotDeu().setScale(2, RoundingMode.HALF_UP));
  	    tributo.setMtoPagtot(tributo.getMtoPagtot().setScale(2, RoundingMode.HALF_UP));
  	    if (tributo.getPorTasa() == null) {
  	      tributo.setPorTasa(new BigDecimal(0));
  	    }
  	    if (IND_VAL_ORIGINAL.equals(formulario.getCodIndRec())) {
  	      tributo.setIndVig(ProcesoBatchFormularioConstantes.IND_VIG_1);
  	    }
  	  }
  	});
  }

  private void setearCasillasFormulario(FormularioJsonBean formulario) {
   	SimpleDateFormat sdfhora = new SimpleDateFormat(FORM_FECHA_DIA_MES_ANIO_HORA);
   	Locale locale = new Locale("es", "PE");
       DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
       symbols.setDecimalSeparator('.');
       DecimalFormat df = new DecimalFormat("###0 .00", symbols);
   	formulario.getCasillasFormularioList().stream().forEachOrdered(casilla -> {
   	  if (casilla != null && casilla.getT7779casillaformPK() != null) {
   	    casilla.setCodUsuregis(formulario.getCodUsuregis());
   	    casilla.getT7779casillaformPK().setNumOrd(formulario.getNumOrd());
   	    if (casilla.getT7779casillaformPK().getNumCas() == 58) {
   	        casilla.setDesValcas(sdfhora.format(formulario.getHorPres()));
   	    }else{
             String casillaDetalle="";
           try{
             if(casilla.getDesValcas()!=null || !casilla.getDesValcas().equals("")){
               casillaDetalle=URLDecoder.decode(casilla.getDesValcas(), "UTF-8");
             }
            casilla.setDesValcas(casillaDetalle);
          } catch (UnsupportedEncodingException e) {
           utilLog.imprimirLog(ConstantesUtils.LEVEL_ERROR, "Error UnsupportedEncodingException "+e, Thread.currentThread().getStackTrace());
          }
   	    	if (((!COD_TIP_FOR_PDT.equals(formulario.getCodTipFor())) && Arrays.asList(ProcesoBatchFormularioConstantes.VALOR_06, ProcesoBatchFormularioConstantes.VALOR_07, ProcesoBatchFormularioConstantes.VALOR_08, ProcesoBatchFormularioConstantes.VALOR_09, ProcesoBatchFormularioConstantes.VALOR_10, ProcesoBatchFormularioConstantes.VALOR_11).contains(casilla.getCodTipCam())) && this.validaCasilla(casilla)) {
             if(!casilla.getDesValcas().equals("")){
               if (casilla.getDesValcas().matches("[0-9]*")){
                 casilla.setDesValcas(df.format(Double.parseDouble(casilla.getDesValcas())));
               }
             }
           }
   	    }
   	  }
   	});
   }

  private void seteoInicialFormulario(T7777bandprespago t7777bandprespagoEntity, BandejaJsonBean bandejaBean,
  		FormularioJsonBean formulario) {
        String bigDecimalString = String.valueOf(formulario.getMtoPag().doubleValue());

  	formulario.setNumRuc(t7777bandprespagoEntity.getNumRuc());
  	formulario.setMtoPag(formulario.getMtoPag().setScale(2, RoundingMode.HALF_UP));
  	formulario.setNumPedPro(obtenerNumPedPro(t7777bandprespagoEntity.getCodDep()));
  	formulario.setNumPedAnu(obtenerNumPedPro(t7777bandprespagoEntity.getCodDep()));
  	formulario.setCodUsuregis(t7777bandprespagoEntity.getCodUsuregis());

  	if (bandejaBean.getResultadoPagoBean() != null) {
  	  formulario.setCodMedpag(bandejaBean.getResultadoPagoBean().getCodMedPag());
  	  formulario.setCodEntFin(bandejaBean.getResultadoPagoBean().getCodEntFin());
  	  formulario.setNomEntFin(bandejaBean.getResultadoPagoBean().getNomEntFin());

      if(formulario.getNomEntFin()==null){
  	    //metrear
  	    String nombreEntFin = MetricUtil.timered(this, "t7792paramdetaDAO.getDescEntFinaciera", procesoBatchFormularioService1 -> t7792paramdetaDAO.getDescEntFinaciera(formulario.getCodEntFin()));
  	    formulario.setNomEntFin(nombreEntFin);

  	  }

  	}

  if (COD_MED_PAG_NPS.equals(formulario.getCodMedpag())) {
    formulario.setMtoPag(new BigDecimal(0));
    formulario.setCodEtapa(ProcesoBatchFormularioConstantes.V_COD_ETAPA_CERO_UNO);
  }

  if (formulario.getMtoPag().compareTo(BigDecimal.ZERO) == 0){
    	formulario.setCodEtapa(ProcesoBatchFormularioConstantes.V_COD_ETAPA_CERO_UNO);
        formulario.setCodEstado(ProcesoBatchFormularioConstantes.V_NUM_PAGO_MAYOR_CERO);//00
	        if(COD_TIP_FOR_PDT.equals(formulario.getCodTipFor())){ //es PDT
	          formulario.setCodEntFin(null);
	          formulario.setNomEntFin(null);
	          formulario.setFecPago(null);
	          formulario.setNumOpeBco(null);
            formulario.setCodMedpag(null);
	        }
   } else {
  	  // pago >0 || PDT
	  	  if (COD_FOR_BOLETA.equals(formulario.getCodFor()) || COD_TIP_FOR_PDT.equals(formulario.getCodTipFor())) {
	  	    formulario.setCodEtapa(ProcesoBatchFormularioConstantes.V_COD_ETAPA_CERO_DOS); // 02
	        formulario.setCodEstado(ProcesoBatchFormularioConstantes.V_NUM_PAGO_MAYOR_CERO);//00
	        /*if(COD_TIP_FOR_PDT.equals(formulario.getCodTipFor())){
	          formulario.setCodEstado(ProcesoBatchFormularioConstantes.V_COD_ESTADO_PDT);//90
	        }*/
	  	  } else {
	  	      // DDJJ
	  	      formulario.setMtoPag(new BigDecimal(0));
	  	      formulario.setCodEtapa(ProcesoBatchFormularioConstantes.V_COD_ETAPA_CERO_UNO); // 01
	  	      formulario.setCodEstado(ProcesoBatchFormularioConstantes.V_NUM_PAGO_MAYOR_CERO);
	  	  }
  	}

  	// Con IVAP solo se aplica para 0621
  	if (ProcesoBatchFormularioConstantes.COD_FORM_0621.equalsIgnoreCase(formulario.getCodFor())) {
  	  formulario.setIndValLib(IND_VAL_LIB_1);
  	} else {
  	  formulario.setIndValLib(IND_VAL_LIB_0);
  	}
  }


  private void setearRectificatoriasTributo(T7777bandprespago t7777bandprespagoEntity, FormularioJsonBean formulario) {
  	if (!COD_FOR_BOLETA.equals(formulario.getCodFor()) && (VALOR_UNO.equals(formulario.getCodIndRec()) || VALOR_DOS.equals(formulario.getCodIndRec()))) {
  	      List<T7781rectitributo> listaRecti = null;
  	        listaRecti = this.obtenerRectificatoriasFormulario(formulario, t7777bandprespagoEntity.getCodDep(), formulario.getTributosDeclaradosPagadosList());
  	        if (listaRecti != null && !listaRecti.isEmpty()) {
  	          formulario.setRectificatoriasTributoList(listaRecti);
  	        }
  	}
  }

    /**
     * Metodo obtener rectificatoia formulario.
     *
     * @param t7778formulario
     * @param codDep
     * @param listTributos    @return List<T7781rectitributo>
     * @throws JsonProcessingException
     */
    private List<T7781rectitributo> obtenerRectificatoriasFormulario(FormularioJsonBean t7778formulario, String codDep, List<T7780tributodecpag> listTributos) {
        List<T7781rectitributo> listaRecti = new ArrayList<>();
        Character valEstRectTributo = '0';
        if (null != listTributos && !listTributos.isEmpty()) {
          for (T7780tributodecpag tributo : listTributos) {
            if (tributo != null) {
              T7781rectitributoPK t7781rectitributoPK = new T7781rectitributoPK();
              T7781rectitributo rectificacion = new T7781rectitributo();
              t7781rectitributoPK.setNumOrd(tributo.getT7780tributodecpagPK().getNumOrd());
              t7781rectitributoPK.setCodTri(tributo.getT7780tributodecpagPK().getCodTri());
              rectificacion.setT7781rectitributoPK(t7781rectitributoPK);
              rectificacion.setMtoImpres(tributo.getMtoImpres());
              rectificacion.setCodOrirec(VALOR_0);
              rectificacion.setCodInduci(VALOR_0);
              rectificacion.setCodForant(t7778formulario.getCodFor());
              rectificacion.setNumOrdant(BigInteger.valueOf(this.obtenerUltimoNumOrdenFormulario(t7778formulario)));
              rectificacion.setMtoImpresAnt(t7778FormulariosDAO.obtenerUltimoMtoImpresAnt(rectificacion.getNumOrdant(), tributo));
              rectificacion.setPerTri(t7778formulario.getPerTri());
              rectificacion.setCodUsuregis(t7778formulario.getCodUsuregis());
              valEstRectTributo = this.seteaRectificacion(t7778formulario, codDep, rectificacion);

              if (VALOR_1.equals(valEstRectTributo)) {
                //actualizar anteriores Tributos y Coeficiente IGV
                this.verificarExisteActualizacion(rectificacion);
              }

              this.actualizarIndVig(t7778formulario, valEstRectTributo, rectificacion);
              this.actualizarIndCoeficienteIGV(t7778formulario, rectificacion, valEstRectTributo);
              listaRecti.add(rectificacion);
            }
          }
        }
        return listaRecti;
    }

  private Character seteaRectificacion(FormularioJsonBean t7778formulario, String codDep, T7781rectitributo rectificacion) {

  	boolean aceptado = false;
  	if (IND_VAL_SUSTITUTORIA.equals(t7778formulario.getCodIndRec())) {
  	  rectificacion.setCodTipver(VALOR_0);
  	  rectificacion.setCodEstrec(VALOR_1);
  	  rectificacion.setFecVer(new Date());
  	  rectificacion.setIndVig(VALOR_1);
  	} else {

      String valorParamMaximoMonto=t7792paramdetaDAO.obtenerValorParametro(VAL_PARAM_TABLA_1115,codDep);
      if(valorParamMaximoMonto!=null){ //monto permitido
        if(!valorParamMaximoMonto.equals(VALOR_NO_EXISTE)){
          //|10000|5.00|
          Double mtoImpreso=rectificacion.getMtoImpres().doubleValue();

          if(Double.parseDouble(valorParamMaximoMonto)<mtoImpreso){

              rectificacion.setCodEstrec(VALOR_0);
  	           rectificacion.setFecVer(new Date());
            }else{
              aceptado = this.evaluarRectificatoria(codDep, rectificacion.getMtoImpres().doubleValue(), rectificacion.getMtoImpresAnt().doubleValue());
          	  if (aceptado) {
          	    rectificacion.setCodTipver(VALOR_0);
          	    rectificacion.setCodEstrec(VALOR_1);
          	    rectificacion.setFecVer(new Date());
          	    rectificacion.setIndVig(VALOR_1);

          	  } else {
          	    try {
          	      rectificacion.setFecVer(new SimpleDateFormat("dd/mm/yyyy").parse(FEC_POR_DEFAULT));
          	    } catch (ParseException e) {
                  utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Problemas con la fecha de Veredicto", Thread.currentThread().getStackTrace());
          	    }
                rectificacion.setCodTipver(VALOR_0);
                rectificacion.setCodEstrec(VALOR_0);
          	    rectificacion.setIndVig(VALOR_0);
          	  }
            }
        }else{
          aceptado = this.evaluarRectificatoria(codDep, rectificacion.getMtoImpres().doubleValue(), rectificacion.getMtoImpresAnt().doubleValue());
          if (aceptado) {
            rectificacion.setCodTipver(VALOR_0);
            rectificacion.setCodEstrec(VALOR_1);
            rectificacion.setFecVer(new Date());
            rectificacion.setIndVig(VALOR_1);

          } else {

            try {
              rectificacion.setFecVer(new SimpleDateFormat("dd/mm/yyyy").parse(FEC_POR_DEFAULT));
            } catch (ParseException e) {
              utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Problemas con la fecha de Veredicto", Thread.currentThread().getStackTrace());
            }
            rectificacion.setCodTipver(VALOR_0);
            rectificacion.setCodEstrec(VALOR_0);
            rectificacion.setIndVig(VALOR_0);
          }
        }

      }
  	}

    Character valEstRectTributo = rectificacion.getCodEstrec();

    if (VALOR_UNO.equals(valEstRectTributo)) {
      rectificacion.setIndVig(VALOR_1);
    } else if (VALOR_CERO.equals(valEstRectTributo) || VALOR_DOS.equals(valEstRectTributo)) {
      rectificacion.setIndVig(VALOR_0);
    }

  	return valEstRectTributo;
  }

  private void actualizarIndVig(FormularioJsonBean t7778formulario, Character valEstRectTributo,
  		T7781rectitributo rectificacion) {
  	t7778formulario.getTributosDeclaradosPagadosList().stream().forEach(tributox -> {
  	    if (tributox != null && rectificacion.getT7781rectitributoPK().getCodTri().equals(tributox.getT7780tributodecpagPK().getCodTri())) {
  	      if (VALOR_0.equals(valEstRectTributo)) {
  	        tributox.setIndVig(VALOR_0);
  	      } else {
  	        tributox.setIndVig(VALOR_1);
  	      }
  	    }
  	});
  }

  private void actualizarIndCoeficienteIGV(FormularioJsonBean t7778formulario, T7781rectitributo rectificacion, Character valEstRectTributo) {
  	t7778formulario.getCoeficienteIGVPeriodoList().stream().forEach(coeficienteigv -> {
  	    if (coeficienteigv != null && rectificacion.getT7781rectitributoPK().getCodTri().equals(coeficienteigv.getCodTri())) {
  	      coeficienteigv.setIndReccoe(VALOR_1);
  	      if (ProcesoBatchFormularioConstantes.COD_TRIBUTO_010101.equals(coeficienteigv.getCodTri())) {
  	        coeficienteigv.setCodEstrec(valEstRectTributo);
  	      }
  	      if (VALOR_UNO.equals(valEstRectTributo)) {
  	        coeficienteigv.setIndVig(VALOR_1);
  	      } else {
  	        coeficienteigv.setIndVig(VALOR_0);
  	      }
  	    }
  	});
  }

    private boolean verificarExisteActualizacion(T7781rectitributo rectificacion) {
        t7778FormulariosDAO.verificarExisteActualizacion(rectificacion, VALOR_ACT_0);
        return true;
    }


    private T7794procesoband buscarProcesoBandeja(Long idBandeja, String codProceso) {


        utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Ingreso al metodo buscarProcesoBandeja ", Thread.currentThread().getStackTrace());

        T7794procesoband t7794procesoband = null;
        Integer cuenta = t7794dao.verificaProcesoBandeja(idBandeja, codProceso);

        if (cuenta > 0) {
      	  // Aquí se debe obtener formularios por nro de bandeja:

          T7794procesobandPK procesoBandConsulta = new T7794procesobandPK();
          procesoBandConsulta.setNumBandeja(idBandeja);
          procesoBandConsulta.setCodProBan(codProceso);
          t7794procesoband = t7794dao.findById(procesoBandConsulta);
        }
        return t7794procesoband;
      }

    private T7794procesoband registroProcesoBandejaBatchFormulario(Long idBandeja, String numProceso, Character indProcesoBandeja, String usuario, T7794procesoband t7794procesoband) {

        utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Ingreso al metodo registroProcesoBandejaBatchFormulario ", Thread.currentThread().getStackTrace());

        T7794procesobandPK procesoBandActualizar1 = new T7794procesobandPK();
        T7794procesoband procesoBandActualizarEntity1 = new T7794procesoband();
        Calendar fechaActual = null;
        Calendar fechaVacia = null;
        int numIntentos = 0;
        fechaVacia = Calendar.getInstance();
        fechaVacia.set(1, 0, 1, 0, 0, 0);
        fechaActual = Calendar.getInstance();
        //boolean retorno = false;

          if (t7794procesoband != null) { //ya existe
            utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Se actualiza ProcesoBandeja "+idBandeja, Thread.currentThread().getStackTrace());

	            numIntentos = IND_PROCESO_BANDEJA_EN_PROCESO.equals(t7794procesoband.getIndProBan())? numIntentos + ProcesoBatchFormularioConstantes.PRIMER_INTENTO : ProcesoBatchFormularioConstantes.PRIMER_INTENTO+t7794procesoband.getNumIntentos();
	            t7794procesoband.setNumIntentos(numIntentos);
	            t7794procesoband.setIndProBan(indProcesoBandeja);
	            t7794procesoband.setFecModif(fechaActual.getTime());
	            t7794procesoband.setCodUsumodif(usuario);
	            t7794procesoband.setIndDel(VALOR_CERO);
	            t7794dao.update(t7794procesoband);

          }else{ //no existe
            numIntentos = numIntentos + ProcesoBatchFormularioConstantes.PRIMER_INTENTO;
            procesoBandActualizar1.setNumBandeja(idBandeja);
            procesoBandActualizar1.setCodProBan(numProceso);
            procesoBandActualizarEntity1.setT7794procesobandPK(procesoBandActualizar1);
            procesoBandActualizarEntity1.setIndProBan(indProcesoBandeja);
            procesoBandActualizarEntity1.setNumIntentos(numIntentos);
            procesoBandActualizarEntity1.setIndDel(VALOR_CERO);
            procesoBandActualizarEntity1.setFecRegis(fechaActual.getTime());
            procesoBandActualizarEntity1.setCodUsumodif(usuario);
            procesoBandActualizarEntity1.setFecModif(fechaActual.getTime());
    //        if (indicadorProceso.equals(ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_ERRONEO)) {

              t7794dao.getEntityManager().persist(procesoBandActualizarEntity1);
              t7794dao.getEntityManager().flush();
              t7794procesoband=procesoBandActualizarEntity1;
          }
           // retorno = true;
  //        }
        return t7794procesoband;
    }


    private Long obtenerUltimoNumOrdenFormulario(FormularioJsonBean t7778formulario) {
  	T7778formulario t7778 = new T7778formulario();
      t7778.setNumRuc(t7778formulario.getNumRuc());
      t7778.setPerTri(t7778formulario.getPerTri());
      t7778.setCodFor(t7778formulario.getCodFor());
      return this.t7778FormulariosDAO.obtenerUltimoNumOrdenFormulario(t7778);
    }

    /**
     * Metodo que obtiene numero pedido producto.

     *

     * @param codDepen

     * @return String
     */
    private String obtenerNumPedPro(String codDepen) {
      return codDepen.concat(ProcesoBatchFormularioConstantes.SUFIJO_NUM_PED_PRO);
    }

    public boolean evaluarRectificatoria(String codDependencia, Double mtoImpResNue, Double mtoImpResAnt) {
        utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Ingreso al metodo EvaluarRectificatoria: ",Thread.currentThread().getStackTrace());
        ProcesoBatchFormularioUtil batchformularioUtil=new ProcesoBatchFormularioUtil();
        Double mtoDifImpRes = Double.valueOf(0);
        Double mtoDifPorc = Double.valueOf(0);
        Double mtoDifImpPerm = Double.valueOf(0);
        Double mtoDifPorcPerm = Double.valueOf(0);
        boolean aceptado = false;
        String[] importesBean = batchformularioUtil.calcularDiferenciaImportes(mtoImpResNue, mtoImpResAnt);
        T7792paramdeta parametroDetalle = t7792paramdetaDAO.getParametroDetallePlataforma(VAL_PARAM_TABLA_1115, codDependencia);
        if (parametroDetalle != null && parametroDetalle.getValParam() != null) {
          String valorParam = parametroDetalle.getValParam();
          String[] cadenas = valorParam.split("\\|");
          mtoDifImpPerm = new Double(cadenas[1]);//Importe de diferencia permitido
          mtoDifPorcPerm = new Double(cadenas[2]);//Porcentaje de diferencia permitido
        }
        if (importesBean != null) {
          mtoDifImpRes = new Double(importesBean[1]);
          mtoDifPorc = new Double(importesBean[3]);
        }
        if (mtoDifImpRes <= mtoDifImpPerm) {
          aceptado = true;
        } else {
          if (mtoDifPorc <= mtoDifPorcPerm) {
            aceptado = true;
          } else {
            aceptado = false;
          }
        }
        return aceptado;
    }

    @Transactional
    public boolean registrarT7778formulario(List<T7778formulario> lstT7778FormulariosBoleta, T7777bandprespago t7777bandprespagoEntity) {
      utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Ingreso al metodo registrarT7778formulario: ",Thread.currentThread().getStackTrace());
      List<AnexaBean> anexaBeanList= new ArrayList<>();

      EntityManagerFactory emf = Persistence.createEntityManagerFactory(EMFORMULARIO);
      EntityManager em = emf.createEntityManager();
      boolean resultadoTransaccion = false;
        List<T7782anexaform> listaAnexas =new ArrayList<T7782anexaform>();
      lstT7778FormulariosBoleta.stream().forEach(formulariox -> { //bucle principal
    	  Long indExiste = t7778FormulariosDAO.buscarPorNumOrd(formulariox.getNumOrd());
    	  if (formulariox != null && (indExiste == 0)) {

          //  em.persist(casillax);
          formulariox.getT7780tributodecpagList().stream().forEach(tributox -> {
            if (tributox != null) {
                em.persist(tributox);

            }
          });
          //  em.persist(tributox);
          if (!COD_FOR_BOLETA.equalsIgnoreCase(formulariox.getCodFor())) {
            formulariox.getT7783casillasugfinList().stream().forEach(casillasugeridax -> {
              if (casillasugeridax != null) {
                  em.persist(casillasugeridax);
              }
            });
            //   em.persist(casillasugeridax);
            formulariox.getT7781rectitributoList().stream().forEach(rectributox -> {
              if (rectributox != null) {
                  em.persist(rectributox);
              //    em.flush();
                  utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Guardo Rectificatorias Formulario", Thread.currentThread().getStackTrace());
              }
            });
  //                        em.persist(rectributox);
            formulariox.getT7784coefigvdetList().stream().forEach(coefigvdet -> {
              if (coefigvdet != null && coefigvdet.getT7784coefigvdetPK() != null) {
                  em.persist(coefigvdet);
                //  em.flush();
                  utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Guardo Detalle Coeficiente IGV", Thread.currentThread().getStackTrace());
              }
            });
            if (null != formulariox.getT7795retepercigvList() && !formulariox.getT7795retepercigvList().isEmpty()) {
              formulariox.getT7795retepercigvList().stream().forEach(retepercigv -> {
                if (retepercigv != null && retepercigv.getT7795retepercigvPK() != null) {
                    em.persist(retepercigv);
                //    em.flush();
                    utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Guardo Retencion IGV", Thread.currentThread().getStackTrace());
                }
              });
            }
            // em.persist(retepercigv);
            formulariox.getT7785coefigvperList().stream().forEach(coefigvper -> {
              if (coefigvper != null) {
                  em.persist(coefigvper);
                //  em.flush();
                  utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Guardo Percepcion IGV", Thread.currentThread().getStackTrace());
              }
            });
            //em.persist(coefigvper);
            formulariox.getT7786detapagoprevList().stream().forEach(detapagoprev -> {
              if (detapagoprev != null) {
                em.persist(detapagoprev);
              }
            });



          // em.persist(detapagoprev);
          if (formulariox.getT7788cargapdt() != null) {
            T7788cargapdt t7788cargapdt = formulariox.getT7788cargapdt();
              em.merge(t7788cargapdt);
            //  em.flush();
              utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Actualizo Carga PDT", Thread.currentThread().getStackTrace());
          }

          //anexas

          if(formulariox.getT7782anexaformList().size()>0){ //existe anexas
          	utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Total Anexas Final{}"+formulariox.getT7782anexaformList().size(), Thread.currentThread().getStackTrace());
          formulariox.getT7782anexaformList().stream().forEach(anexas -> {
            if (anexas != null) {
            	  AnexaBean anexaBean= new AnexaBean();
            	  anexaBean.setCodExtanx(anexas.getT7782anexaformPK().getCodExtanx());
            	  anexaBean.setCodNomanx(anexas.getT7782anexaformPK().getCodNomanx());
            	  anexaBean.setDesTabanx(anexas.getDesTabanx());
            	  anexaBean.setNumOrd(formulariox.getNumOrd());
            	  anexaBeanList.add(anexaBean);
            	  anexas.setCodUsuregis(t7777bandprespagoEntity.getCodUsuregis());
            	  em.persist(anexas);
            //  em.flush();
              utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Guardo Anexas Formulario", Thread.currentThread().getStackTrace());
            }
          });


        }
      }

          formulariox.setT7777bandprespago(t7777bandprespagoEntity);
          formulariox.setNumBandeja(t7777bandprespagoEntity.getNumBandeja());
          em.persist(formulariox);

        }
      }); //aqui se cierra

      utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Lista Anexas Tamanio "+listaAnexas.size(), Thread.currentThread().getStackTrace());
      utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"Ante de guardar anexas en redis", Thread.currentThread().getStackTrace());
      //validar para el redis
      //if(listaAnexas!=null && listaAnexas.size()>0){
      if(anexaBeanList!=null && anexaBeanList.size()>0){

      String jsonAnexas = jsonUtilParser.entityToJson(anexaBeanList);
     // utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, Cadena.obtenerMsgDeArrayJson("jsonAnexa", jsonAnexas), Thread.currentThread().getStackTrace());
      String numBandeja = t7777bandprespagoEntity.getNumBandeja().toString();

      formularioCache.saveJsonAnexa(numBandeja, jsonAnexas);
      utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, Cadena.obtenerMsgDeArray("Se guardo en redis jsonAnexa con bandeja:", numBandeja), Thread.currentThread().getStackTrace());
}
      em.flush();

      resultadoTransaccion = true;
      return resultadoTransaccion;
    }

    private boolean validaCasilla(T7779casillaform casilla) {
      Integer numCas=casilla.getT7779casillaformPK().getNumCas();
      String newNumCas=String.valueOf(numCas);
      return !"380".equals(newNumCas) && !"364".equals(newNumCas) && !"315".equals(newNumCas) && !"173".equals(newNumCas);
    }

    private List<T7778formulario> setearListaFormularioEntity(List<FormularioJsonBean> lstT7778FormulariosBoletas) {
      List<T7778formulario> listaFormularioSaved = new ArrayList<>();
      utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Ingreso al metodo setearListaFormularioEntity: ",Thread.currentThread().getStackTrace());

      DateFormat sdfhora = new SimpleDateFormat(FORM_FECHA_DIA_MES_ANIO_HORA);
      Calendar calendar = Calendar.getInstance();

      lstT7778FormulariosBoletas.stream().forEach(formularioBean -> {
        T7778formulario t7778formularioEntity = new T7778formulario();
        t7778formularioEntity.setNumBandeja(formularioBean.getNumBandeja());
        t7778formularioEntity.setNumOrd(formularioBean.getNumOrd());
        t7778formularioEntity.setNumRuc(formularioBean.getNumRuc());
        t7778formularioEntity.setNumRucSec(formularioBean.getNumRucSec());
        t7778formularioEntity.setFecPres(formularioBean.getFecPres());
        t7778formularioEntity.setHorPres(formularioBean.getHorPres());
        t7778formularioEntity.setPerTri(formularioBean.getPerTri());
        t7778formularioEntity.setSemFor(formularioBean.getSemFor());
        t7778formularioEntity.setCodIndrec(formularioBean.getCodIndRec());
        t7778formularioEntity.setCodOrifor(formularioBean.getCodOriFor());
        t7778formularioEntity.setCodOriPres(formularioBean.getCodOriPres());
        t7778formularioEntity.setCodTipfor(formularioBean.getCodTipFor());
        t7778formularioEntity.setNumValPag(formularioBean.getNumValPag());
        t7778formularioEntity.setMtoPag(formularioBean.getMtoPag());
        t7778formularioEntity.setCodTipdetrac(formularioBean.getCodTipdetrac());
        t7778formularioEntity.setCodEstado(formularioBean.getCodEstado());
        t7778formularioEntity.setCodEtapa(formularioBean.getCodEtapa());
        t7778formularioEntity.setNumIdConstancia(formularioBean.getNumIdConstancia());
        t7778formularioEntity.setNumIdResumen(formularioBean.getNumIdResumen());
        t7778formularioEntity.setNumIdReporte(formularioBean.getNumIdReporte());
        t7778formularioEntity.setNumNabono(formularioBean.getNumNabono());
        t7778formularioEntity.setNumResrec(formularioBean.getNumResrec());
        t7778formularioEntity.setIndValLib(formularioBean.getIndValLib());
        t7778formularioEntity.setNumPedPro(formularioBean.getNumPedPro());
        t7778formularioEntity.setNumPedAnu(formularioBean.getNumPedAnu());
        t7778formularioEntity.setIndForAut(formularioBean.getIndForAut());

        t7778formularioEntity.setNumOpeBco(formularioBean.getNumOpeBco());
        //     t7778formularioEntity.setIndDel(formularioBean.getIndDel());
        t7778formularioEntity.setCodUsuregis(formularioBean.getCodUsuregis());
        //    t7778formularioEntity.setFecRegis(formularioBean.getFecRegis());
        //   t7778formularioEntity.setCodUsumodif(formularioBean.getCodUsumodif());
        //    t7778formularioEntity.setFecModif(formularioBean.getFecModif());
        t7778formularioEntity.setNumOrdOri(formularioBean.getNumOrdOri());
        t7778formularioEntity.setCodFor(formularioBean.getCodFor());
        t7778formularioEntity.setNumVerFor(formularioBean.getNumVerFor());
        t7778formularioEntity.setT7788cargapdt(formularioBean.getCargaPdt());

        if(t7778formularioEntity.getNumCargaPdt()==IND_VAL_PDT_CERO){
          t7778formularioEntity.setNumCargaPdt(null);
        }else{
          t7778formularioEntity.setNumCargaPdt(formularioBean.getNumCargaPdt());
        }

        if(formularioBean.getCodTipFor()!=null){
          if((!formularioBean.getCodTipFor().equals(COD_TIP_FOR_PDT)) &&  !formularioBean.getCodFor().equals(COD_FOR_BOLETA)){
        	  t7778formularioEntity.setCodEntFin(null);
        	  t7778formularioEntity.setNomEntFin(null);
          }else{
        	  t7778formularioEntity.setCodEntFin(formularioBean.getCodEntFin());
        	  t7778formularioEntity.setNomEntFin(formularioBean.getNomEntFin());
          }
        }else{
          if(!formularioBean.getCodFor().equals(COD_FOR_BOLETA)){
            t7778formularioEntity.setCodEntFin(null);
        	  t7778formularioEntity.setNomEntFin(null);
          }else{
            t7778formularioEntity.setCodEntFin(formularioBean.getCodEntFin());
            t7778formularioEntity.setNomEntFin(formularioBean.getNomEntFin());
          }
        }

        t7778formularioEntity.setCodMedpag(formularioBean.getCodMedpag());
        t7778formularioEntity.setFecPago(formularioBean.getFecPago());
        t7778formularioEntity.setDesFor(formularioBean.getDesFor());
        t7778formularioEntity.setNumTrabajador(formularioBean.getNumTrabajador());
        t7778formularioEntity.setNumPensionista(formularioBean.getNumPensionista());
        t7778formularioEntity.setNumCuartaCategoria(formularioBean.getNumCuartaCategoria());
        t7778formularioEntity.setNumNorEspecial(formularioBean.getNumNorEspecial());
        t7778formularioEntity.setNumModFormativa(formularioBean.getNumModFormativa());
        t7778formularioEntity.setNumTercero(formularioBean.getNumTercero());
        t7778formularioEntity.setT7786detapagoprevList(formularioBean.getDetallePagosPreviosList());
        t7778formularioEntity.setT7779casillaformList(formularioBean.getCasillasFormularioList());
        t7778formularioEntity.setT7780tributodecpagList(formularioBean.getTributosDeclaradosPagadosList());
        t7778formularioEntity.setT7781rectitributoList(formularioBean.getRectificatoriasTributoList());
        t7778formularioEntity.setT7783casillasugfinList(formularioBean.getCasillasSugeridasFinalesList());
        t7778formularioEntity.setT7784coefigvdetList(formularioBean.getDetallesCoeficienteIGVList());
        t7778formularioEntity.setT7785coefigvperList(formularioBean.getCoeficienteIGVPeriodoList());
        t7778formularioEntity.setT7787autoguardformList(formularioBean.getAutoguardadoFormularioList());
        t7778formularioEntity.setT7782anexaformList(formularioBean.getAnexasFormularioList());
        t7778formularioEntity.setT7795retepercigvList(formularioBean.getRetePercIGVList());
        t7778formularioEntity.setIndBan(formularioBean.getIndBan());
        t7778formularioEntity.setCodTriAlt(formularioBean.getCodTriAlt());
        t7778formularioEntity.setCodTriBaj(formularioBean.getCodTriBaj());
        listaFormularioSaved.add(t7778formularioEntity);
      });
      return listaFormularioSaved;
    }

    /*private String obtenerValorParametro(String codTabla, String codParam){
      return ((List<String>) t7792paramdetaDAO.createNativeQuery(
          "SELECT SUBSTR(val_param,2,5) val_param FROM informix.t7792paramdeta WHERE cod_tabla = '"+codTabla+"' and cod_param='" + codParam
              + "'")
          .getResultList()).stream().findFirst().orElse("00000");
    }*/



    	public List<FormularioJsonBean> formatearDatosBoletas(String jsonFromBandeja, List<FormularioJsonBean> lstT7778formularioOrigen) {

    			utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,"Ingreso al metodo formatearDatosBoletas ", Thread.currentThread().getStackTrace());

    			List<FormularioJsonBean> lstBoletas = new ArrayList<>();
    			List<FormularioJsonBean> lstFormularioEntity = new ArrayList<>();
    			FormularioJsonBean formularioDj = null;
    			FormularioJsonBean formularioBoleta = null;
    			JsonNode jObject = jsonUtilParser.jsonToEntity(jsonFromBandeja, JsonNode.class);
    			if (jObject != null) {
    				ArrayNode formularioArray = (ArrayNode) jObject.get("formulario");
    				for (int i = 0; i < formularioArray.size(); i++) {
    					formularioDj = jsonUtilParser.jsonToEntity(String.valueOf(formularioArray.get(i)), FormularioJsonBean.class);
    					JsonNode boletasNode = formularioArray.get(i).get("boletas");
    					if (boletasNode.isArray()) {
    						ArrayNode boletasArray = (ArrayNode) boletasNode;
    						if (boletasArray != null) {
    							for (JsonNode objetoJson : boletasArray) {
    								JsonNode elem0 = objetoJson.get("numOrd");
    								if (!Long.valueOf(elem0.asLong()).equals(ProcesoBatchFormularioConstantes.BOLETA_NO_VALIDA)) {
    									formularioBoleta = jsonUtilParser.jsonToEntity(String.valueOf(objetoJson), FormularioJsonBean.class);
    									/*T7779casillaform casilla16 = new T7779casillaform();
    									T7779casillaformPK pkcasilla16 = new T7779casillaformPK();
    									pkcasilla16.setNumCas(16);
    									pkcasilla16.setNumOrd(elem0.asLong());
    									casilla16.setT7779casillaformPK(pkcasilla16);
    									casilla16.setCodTipcas(ProcesoBatchFormularioConstantes.VALOR_O1);
    									casilla16.setCodTipCam(ProcesoBatchFormularioConstantes.VALOR_O1);
    									casilla16.setPerTri(formularioDj.getPerTri());
    									casilla16.setDesValcas(formularioDj.getCodFor());
    									T7779casillaform casilla17 = new T7779casillaform();
    									T7779casillaformPK pkcasilla17 = new T7779casillaformPK();
    									pkcasilla17.setNumCas(17);
    									pkcasilla17.setNumOrd(elem0.asLong());
    									casilla17.setT7779casillaformPK(pkcasilla17);
    									casilla17.setCodTipcas(ProcesoBatchFormularioConstantes.VALOR_O1);
    									casilla17.setCodTipCam(ProcesoBatchFormularioConstantes.VALOR_O1);
    									casilla17.setPerTri(formularioDj.getPerTri());
    									casilla17.setDesValcas(formularioDj.getNumOrd().toString());
    									*/
    									formularioBoleta.getCasillasFormularioList().add(crearCasillaAuto(16,elem0.asLong(),formularioDj.getPerTri(),
    											formularioDj.getCodFor()));
    									formularioBoleta.getCasillasFormularioList().add(crearCasillaAuto(17,elem0.asLong(),formularioDj.getPerTri(),
    											formularioDj.getNumOrd().toString()));
    									lstBoletas.add(formularioBoleta);
    								}
    							}
    						}
    					}
    					if (lstBoletas != null && !lstBoletas.isEmpty()) {
    						lstFormularioEntity = Stream.concat(lstT7778formularioOrigen.stream(), lstBoletas.stream()).collect(Collectors.toList());
    					} else {
    						lstFormularioEntity = lstT7778formularioOrigen;
    					}
    				}
    			}
    			return lstFormularioEntity;
    	}


    private T7779casillaform crearCasillaAuto(int numCas, Long numOrd,Integer perTri, String desVal){
    	T7779casillaform casilla16 = new T7779casillaform();
		T7779casillaformPK pkcasilla16 = new T7779casillaformPK();
		pkcasilla16.setNumCas(numCas);
		pkcasilla16.setNumOrd(numOrd);
		casilla16.setT7779casillaformPK(pkcasilla16);
		casilla16.setCodTipcas(ProcesoBatchFormularioConstantes.VALOR_O1);
		casilla16.setCodTipCam(ProcesoBatchFormularioConstantes.VALOR_O1);
		casilla16.setPerTri(perTri);
		casilla16.setDesValcas(desVal);
		return casilla16;
    }

    public BandejaJsonBean formatearDatos(String jsonFromBandeja) {

      		BandejaJsonBean	bandejaJsonBean=new BandejaJsonBean();

      		try{
      			bandejaJsonBean=jsonUtilParser.jsonToEntity(jsonFromBandeja, BandejaJsonBean.class);
      		}catch(Exception jpe){
      			utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"  Exception {} "+jpe, Thread.currentThread().getStackTrace());
      		}
      		return bandejaJsonBean;
    }

    private void reprocesoCasillasTotal(FormularioJsonBean formularioDj,JsonNode formularioNode, FormularioJsonBean formPadre){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(EMFORMULARIO);
        EntityManager em = emf.createEntityManager();
    	//FormularioJsonBean formularioDj = null;
        List<String> listaData=null;
        Long numOrd=new Long(0);
        List<T7779casillaform> listaCasillas = new ArrayList<>();
        String codFor="";
        String numCas="";
        String codUsuario="";
        Integer perTri=0;
        String desValcas="";
        String codTipcas="";
        String codTipCam="";
    	  //validar si existe en Formulario
        if(formularioDj!=null){
        	numOrd = formularioNode.path("numOrd").asLong();
            codFor = formularioNode.path("codFor").asText();
            codUsuario = formularioNode.path("codUsuregis").asText();
            if(!ProcesoBatchFormularioConstantes.BOLETA_NO_VALIDA.equals(numOrd)){
            	Long indExiste = t7778FormulariosDAO.buscarPorNumOrd(numOrd);

          if (indExiste > 0) { //si existe formulario
          //validar si numOrdBoleta >0

        	  //validacion casilla 16-17 boletas
        	  if(formPadre!=null){

        		  T7779casillaformPK casillaPKAuto=null;
        		  try{
        			  casillaPKAuto = new T7779casillaformPK();
                      casillaPKAuto.setNumCas(ProcesoBatchFormularioConstantes.CASILLA_16);
                      casillaPKAuto.setNumOrd(numOrd);
                      if(t7779casillaformDAO.findById(casillaPKAuto)==null){
                    	  em.persist(crearCasillaAuto(ProcesoBatchFormularioConstantes.CASILLA_16, numOrd, formPadre.getPerTri(), formPadre.getCodFor()));
                      }
        		  }catch(ObjectNotFoundException ex){
        			  em.persist(crearCasillaAuto(ProcesoBatchFormularioConstantes.CASILLA_16, numOrd, formPadre.getPerTri(), formPadre.getCodFor()));
        		  }

        		  try{
        			  casillaPKAuto = new T7779casillaformPK();
                      casillaPKAuto.setNumCas(ProcesoBatchFormularioConstantes.CASILLA_17);
                      casillaPKAuto.setNumOrd(numOrd);
                      if(t7779casillaformDAO.findById(casillaPKAuto)==null){
                    	  em.persist(crearCasillaAuto(ProcesoBatchFormularioConstantes.CASILLA_17, numOrd, formPadre.getPerTri(), formPadre.getNumOrd().toString()));
                      }
        		  }catch(ObjectNotFoundException ex){
        			  em.persist(crearCasillaAuto(ProcesoBatchFormularioConstantes.CASILLA_17, numOrd, formPadre.getPerTri(), formPadre.getNumOrd().toString()));
        		  }
        	  }

          JsonNode casillaListNode = formularioNode.path("casillasFormularioList");
          ArrayNode casillaListArray = (ArrayNode) casillaListNode;
              if(casillaListArray.isArray()){
                if(numOrd>0){
                listaCasillas=t7778FormulariosDAO.detalleFormulario(numOrd);
                if(listaCasillas.size()>0){
                  if(casillaListNode.size()>listaCasillas.size()){ //si listajson es mayor lista Casilla
                    listaData=new ArrayList<String>();

                    for(T7779casillaform casillaForm:listaCasillas){
                        listaData.add(String.valueOf(casillaForm.getT7779casillaformPK().getNumCas()));
                    }

                    for (JsonNode casillaNode : casillaListArray) {
                      desValcas = casillaNode.path("desValcas").asText();
                      codTipCam=casillaNode.path("codTipCam").asText();
                      codTipcas=casillaNode.path("codTipcas").asText();
                      perTri = casillaNode.path("perTri").asInt();
                      numCas = casillaNode.path("casillasFormularioPK").path("numCas").asText();
                    //  listaJson.add(numCas);
                    if(Integer.parseInt(numCas)>0){

                      boolean match = listaData.stream().anyMatch(numCas::contains);
                      if(!match){ //no existe numCas
                        utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"  crear casilla "+numOrd+" - "+numCas, Thread.currentThread().getStackTrace());
                        T7779casillaform casilla = new T7779casillaform();
                        T7779casillaformPK casillaPK = new T7779casillaformPK();
                        casillaPK.setNumOrd(numOrd);
                        casillaPK.setNumCas(Integer.parseInt(numCas));
                        casilla.setT7779casillaformPK(casillaPK);
                        casilla.setCodTipcas(codTipcas);
                        casilla.setCodTipCam(codTipCam);
                        casilla.setPerTri(perTri);
                        casilla.setCodUsuregis(codUsuario);
                        casilla.setDesValcas(desValcas);
                        em.persist(casilla);
                      //  formularioDj.getCasillasFormularioList().add(casilla);
                      //  this.setearCasillasFormulario(formularioDj);
                    }else{
                      utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"  actualizar casilla "+numOrd+" - "+numCas, Thread.currentThread().getStackTrace());
                      T7779casillaform casilla = new T7779casillaform();
                      T7779casillaformPK casillaPK = new T7779casillaformPK();
                      casillaPK.setNumOrd(numOrd);
                      casillaPK.setNumCas(Integer.parseInt(numCas));
                      casilla.setT7779casillaformPK(casillaPK);
                      casilla.setCodTipcas(codTipcas);
                      casilla.setCodTipCam(codTipCam);
                      casilla.setCodUsuregis(codUsuario);
                      casilla.setPerTri(perTri);
                      casilla.setDesValcas(desValcas);
                      em.merge(casilla);
                    }
                    }
                    }
                    em.flush();
                  }
                  }else{//x<=0
                    for (JsonNode casillaNode : casillaListArray) {
                      desValcas = casillaNode.path("desValcas").asText();
                      codTipCam=casillaNode.path("codTipCam").asText();
                      codTipcas=casillaNode.path("codTipcas").asText();
                      perTri = casillaNode.path("perTri").asInt();
                      numCas = casillaNode.path("casillasFormularioPK").path("numCas").asText();

                    if(Integer.parseInt(numCas)>0){
                      utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"  crear casilla Nuevo "+numOrd+" - "+numCas, Thread.currentThread().getStackTrace());
                      T7779casillaform casilla = new T7779casillaform();
                      T7779casillaformPK casillaPK = new T7779casillaformPK();
                      casillaPK.setNumOrd(numOrd);
                      casillaPK.setNumCas(Integer.parseInt(numCas));
                      casilla.setT7779casillaformPK(casillaPK);
                      casilla.setCodTipcas(codTipcas);
                      casilla.setCodTipCam(codTipCam);
                      casilla.setCodUsuregis(codUsuario);
                      casilla.setPerTri(perTri);
                      casilla.setDesValcas(desValcas);
                      em.persist(casilla);
                    }
                    }
                    em.flush();
                  }
                }
                }
              }else{
                utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,"  Nro Orden No Existe: "+numOrd, Thread.currentThread().getStackTrace());
              }
            }
    	}
    }

    /*****Escanear Casillas Formulario*/
    private ResultadoProcesoBatchFormularioBean validarCasillasFormulario(Long numBandeja){
    	utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Iniciando Validar Casillas", Thread.currentThread().getStackTrace());
    	ResultadoProcesoBatchFormularioBean resultadoProcesoBatchFormularioBean = new ResultadoProcesoBatchFormularioBean();
        T7777bandprespago bandejaPresPago=this.obtenerBandPresPago(numBandeja);
        String jsonFromBandeja = ProcesoBatchFormularioUtil.reemplazarCadena((String) bandejaPresPago.getArcData(), "IfxBSONObject", "");
        JsonNode jObject = jsonUtilParser.jsonToEntity(jsonFromBandeja, JsonNode.class);
        FormularioJsonBean formularioDj = null;
        FormularioJsonBean boletaForm = null;

        //resultadoProcesoBatchFormularioBean.setCod(COD_RESP_CON_ERROR);
		//resultadoProcesoBatchFormularioBean.setMsg("Inicio de reproceso de casillas");
        if (jObject != null) {

          ArrayNode formularioArray = (ArrayNode) jObject.get("formulario");
          if(formularioArray.isArray()){
            for (JsonNode formularioNode : formularioArray) {

            	formularioDj = jsonUtilParser.jsonToEntity(String.valueOf(formularioNode), FormularioJsonBean.class);
            	reprocesoCasillasTotal(formularioDj,formularioNode,null);
            	if(!formularioNode.get("boletas").isNull()){
            		ArrayNode boletaArray = (ArrayNode)formularioNode.get("boletas");
	            	if(boletaArray.isArray()){
	            		for (JsonNode boletaNode : boletaArray) {
	            			boletaForm = jsonUtilParser.jsonToEntity(String.valueOf(formularioNode), FormularioJsonBean.class);
	            			reprocesoCasillasTotal(boletaForm,boletaNode,formularioDj);
	            		}
	            	}

            	}

              }
            }

          }
            resultadoProcesoBatchFormularioBean.setCod(ProcesoBatchFormularioConstantes.COD_RESP_EXITOSO);
            resultadoProcesoBatchFormularioBean.setMsg(ProcesoBatchFormularioConstantes.MENSAJE_CASILLAS_EXITOSO);

          return resultadoProcesoBatchFormularioBean;
    }
    public ResultadoProcesoBatchFormularioBean reprocesarCasillasFormulario(Long numBandeja){
    		ResultadoProcesoBatchFormularioBean respuesta = new ResultadoProcesoBatchFormularioBean();
    		respuesta.setCod(COD_RESP_CON_ERROR);
    		respuesta.setMsg("Inicio de reproceso de casillas");
    		T7794procesoband t7794procesoband=buscarProcesoBandeja(numBandeja,COD_PRO_BAN_CASILLAS);
    		try{
    			t7794procesoband=registroProcesoBandejaBatchFormulario(numBandeja,COD_PRO_BAN_CASILLAS,IND_PROCESO_BANDEJA_EN_PROCESO,COD_USUARIO_SYSTEM,t7794procesoband);
    			respuesta=validarCasillasFormulario(numBandeja);
    			if(COD_RESP_EXITOSO.equals(respuesta.getCod()))
  	    		  registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_CASILLAS, IND_PROCESO_BANDEJA_EXITOSO, COD_USUARIO_SYSTEM,t7794procesoband);
  	    	    else
  	    		  registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_CASILLAS, IND_PROCESO_BANDEJA_ERRONEO, COD_USUARIO_SYSTEM,t7794procesoband);
    		}catch(Exception e){
    			utilLog.imprimirLog(ConstantesUtils.LEVEL_ERROR,e.getMessage(),Thread.currentThread().getStackTrace());
    	    	  if(COD_RESP_EXITOSO.equals(respuesta.getCod()))
    	    		  registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_CASILLAS, IND_PROCESO_BANDEJA_EXITOSO, COD_USUARIO_SYSTEM,t7794procesoband);
    	    	  else
    	    		  registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_CASILLAS, IND_PROCESO_BANDEJA_ERRONEO, COD_USUARIO_SYSTEM,t7794procesoband);
    		}
    		return respuesta;
        }


    private void agregarLogBean(HttpServletRequest request, FormularioJsonBean formularioBean) {
      WSLoggerBean logBean = new WSLoggerBean();
      logBean.setLabel(ConstantesUtils.TRAZA);
      logBean.setUsuario(formularioBean.getCodUsuregis());
      logBean.setRuc(formularioBean.getNumRuc());
      logBean.setNumBandeja(String.valueOf(formularioBean.getNumBandeja()));
      logBean.setHost(request.getHeader("Host"));
      logBean.setFormulario(formularioBean.getCodFor());
      logBean.setPeriodo(String.valueOf(formularioBean.getPerTri()));
      logBean.setServicio("");
      logBean.setUri(request.getRequestURI());
      request.setAttribute(ConstantesUtils.LOG_BEAN, logBean);
    }

    private int validarProceso(int procesar, T7794procesoband proceso) {
		int procesarLocal = 0;
		if (proceso != null) {
			utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "indicador de proceso "+proceso.getIndProBan(), Thread.currentThread().getStackTrace());
			//Character indicadorProceso = proceso.getIndProBan();
			Calendar fechaActual = Calendar.getInstance();
			Long ini = proceso.getFecRegis().getTime();
			Long fin = fechaActual.getTime().getTime();
			Long restante = (fin - ini)/1000;
			utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "tiempo restante "+restante, Thread.currentThread().getStackTrace());
			/*if (indicadorProceso.equals(ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EXITOSO)) { //1
				respuestaBean.setCodRespuesta(COD_RESP_EXITOSO);
				respuestaBean.setDesRespuesta(MENSAJE_EXITOSO);
			} else if (indicadorProceso.equals(ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EN_PROCESO) && restante.intValue() < 300) {//2
				respuestaBean.setCodRespuesta(COD_RESP_EXITOSO);
				respuestaBean.setDesRespuesta("EL PROCESO DE REPLICA SE ENCUENTRA EN PROCESO.");
			} else
			{*/
				procesarLocal = 1;
			//}
		} else { //es nuevo
			procesarLocal = 1;
		}
		procesarLocal = procesarLocal != 0 ? procesarLocal : procesar;
		return procesarLocal;
	}

    private T7794procesoband registraInicioProceso(Long numBandeja) {
		T7794procesobandPK procesoBandInsert = new T7794procesobandPK();
		T7794procesoband procesoBandInsertEntity = new T7794procesoband();
		Calendar fechaActual = null;
		Calendar fechaVacia = null;
		fechaVacia = Calendar.getInstance();
		fechaVacia.set(1, 0, 1, 0, 0, 0);
		fechaActual = Calendar.getInstance();
		procesoBandInsert.setNumBandeja(numBandeja);
		procesoBandInsert.setCodProBan(ProcesoBatchFormularioConstantes.COD_PROCESO_FORMULARIO_BANDEJA);
		procesoBandInsertEntity.setT7794procesobandPK(procesoBandInsert);
		procesoBandInsertEntity.setIndProBan(ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EN_PROCESO);
		procesoBandInsertEntity.setNumIntentos(ProcesoBatchFormularioConstantes.PRIMER_INTENTO);
		procesoBandInsertEntity.setIndDel(ProcesoBatchFormularioConstantes.FLAG_ELIMINADO_ACTIVO);
		procesoBandInsertEntity.setCodUsuregis(ProcesoBatchFormularioConstantes.COD_USUARIO_SYSTEM);
		procesoBandInsertEntity.setFecRegis(fechaActual.getTime());
		procesoBandInsertEntity.setCodUsumodif(ProcesoBatchFormularioConstantes.VARIABLE_STRING_VACIA);
		procesoBandInsertEntity.setFecModif(fechaVacia.getTime());
		t7794dao.getEntityManager().persist(procesoBandInsertEntity);
		t7794dao.getEntityManager().flush();
		return procesoBandInsertEntity;
	}
    private void actualizaInicioProceso(T7794procesoband proceso) {
		proceso.setIndProBan(ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EN_PROCESO);
		t7794dao.getEntityManager().persist(proceso);
		t7794dao.getEntityManager().flush();
	}

  }
