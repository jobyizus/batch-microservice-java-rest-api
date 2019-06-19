package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.service;

import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_FORM_0621;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_FOR_BOLETA;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_PRO_BAN_ANEXAS;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_RESP_CON_ERROR;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_RESP_EXITOSO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_TIP_FOR_PDT;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_USUARIO_SYSTEM;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.DESENCRIPTADOS;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.ENCRIPTADOS;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EN_PROCESO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_ERRONEO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_EXITOSO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.MENSAJE_ANEXAS_EXISTE;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.MENSAJE_ANEXAS_EXITOSO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.MENSAJE_ANEXAS_NO_EXISTE;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.PRIMER_INTENTO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.SUNATPDT_TMP;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.SUNATPDT_TMP_BARRA;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_CERO;
import static pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.EntityManagerInitializer.EMFORMULARIO;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import pe.gob.sunat.recaudacion3.tributaria.administracion.bean.FormularioJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.AnexaJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.ArchivaPDTBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.BandejaJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.ResultadoProcesoBatchFormularioBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.main.config.ConfiguracionBatch;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ArchivoZIPBatch;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioUtil;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7777bandprespago;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7778formulario;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7782anexaform;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7782anexaformPK;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7794procesoband;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7794procesobandPK;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T6630cretperDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7777bandprespagoDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7778FormulariosDAO;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.dao.T7794ProcesoBandDAO;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.ConfigService;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.RecursoNoDisponibleException;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.kibana.WSLoggerBean;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.ConstantesUtils;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.JsonUtilParser;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.UtilLog;

/**
 * Created by Admin on 07/10/2017.
 */
@Transactional
public class ProcesoBatchAnexasFormularioServiceImpl implements ProcesoBatchAnexasFormularioService {

	@Inject
	private T6630cretperDAO t6630cretperDAO;
	@Inject
	private ConfigService configuracion;
	@Inject
	private UtilLog utilLog;
	@Inject
	private T7778FormulariosDAO t7778FormulariosDAO;
	@Inject
	private T7777bandprespagoDAO t7777dao;
	@Context
	private HttpServletRequest request;
	@Inject
	private JsonUtilParser jsonUtilParser;
	ProcesoBatchFormularioUtil procesoBatchFormularioUtil = new ProcesoBatchFormularioUtil();

	@Inject
	private T7794ProcesoBandDAO t7794dao;

	public ResultadoProcesoBatchFormularioBean validarReprocesoAnexas(Long numBandeja) {

		ResultadoProcesoBatchFormularioBean respuesta = new ResultadoProcesoBatchFormularioBean();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(EMFORMULARIO);
		EntityManager em = emf.createEntityManager();
		T7777bandprespago t7777bandprespago = t7777dao.findById(numBandeja);
		T7794procesoband t7794procesoband = buscarProcesoBandeja(numBandeja, COD_PRO_BAN_ANEXAS);
		try {
			t7794procesoband=registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_ANEXAS, IND_PROCESO_BANDEJA_EN_PROCESO,
					COD_USUARIO_SYSTEM,t7794procesoband);
			respuesta = this.reprocesarAnexasFormulario(t7777bandprespago, em);
			if (COD_RESP_EXITOSO.equals(respuesta.getCod())){
				utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Anexa Exitosa",Thread.currentThread().getStackTrace());
						
				registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_ANEXAS, IND_PROCESO_BANDEJA_EXITOSO,
						COD_USUARIO_SYSTEM,t7794procesoband);
			}else{
				if(COD_RESP_CON_ERROR.equals(respuesta.getCod())){
					utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Anexa Fallida",Thread.currentThread().getStackTrace());
					registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_ANEXAS, IND_PROCESO_BANDEJA_ERRONEO,
							COD_USUARIO_SYSTEM,t7794procesoband);
				}
			}	
		} catch (Exception ex) {
			utilLog.imprimirLog(ConstantesUtils.LEVEL_ERROR, ex.getMessage(), Thread.currentThread().getStackTrace());
			if (COD_RESP_EXITOSO.equals(respuesta.getCod()))
				registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_ANEXAS, IND_PROCESO_BANDEJA_EXITOSO,
						COD_USUARIO_SYSTEM,t7794procesoband);
			else
				registroProcesoBandejaBatchFormulario(numBandeja, COD_PRO_BAN_ANEXAS, IND_PROCESO_BANDEJA_ERRONEO,
						COD_USUARIO_SYSTEM,t7794procesoband);
		}
		return respuesta;
	}

	public ResultadoProcesoBatchFormularioBean reprocesarAnexasFormulario(T7777bandprespago t7777bandprespago,
			EntityManager em) {
		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Ingreso al metodo reprocesarAnexasFormulario: ",
				Thread.currentThread().getStackTrace());

		ResultadoProcesoBatchFormularioBean resultadoProcesoBatchFormularioBean = new ResultadoProcesoBatchFormularioBean();
		resultadoProcesoBatchFormularioBean.setCod(COD_RESP_CON_ERROR);
		resultadoProcesoBatchFormularioBean.setMsg("Inicio de reproceso de Anexas");

		if (t7777bandprespago != null) {
			String jsonFromBandeja = ProcesoBatchFormularioUtil
					.reemplazarCadena((String) t7777bandprespago.getArcData(), "IfxBSONObject", "");
			BandejaJsonBean bandejaBean = this.formatearDatos(jsonFromBandeja);

			List<FormularioJsonBean> lstT7778formulario = bandejaBean.getListaFormulario();
			if (lstT7778formulario.size() > 0) {

				lstT7778formulario.stream().forEachOrdered(formularioAnexas -> {
					if (formularioAnexas != null) {
						Long indExiste = t7778FormulariosDAO.buscarPorNumOrd(formularioAnexas.getNumOrd());

						if (indExiste > 0) {
							utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
									"Numero de Orden Anexas: " + formularioAnexas.getNumOrd(),
									Thread.currentThread().getStackTrace());
							Integer cuentaAnexas = t7778FormulariosDAO
									.verificaAnexaFormulario(formularioAnexas.getNumOrd());
							if (cuentaAnexas <= 0) { // no existe anexas
								// verificar si es pdt
								if (formularioAnexas.getCargaPdt() != null
										&& COD_TIP_FOR_PDT.equals(formularioAnexas.getCodTipFor())) {
									Integer tamanio = configuracion.getConfig(ConfiguracionBatch.class)
											.getTamanioFileSize();

									if (formularioAnexas.getCargaPdt().getCntTamArch() <= tamanio) { // menor
																										// a
																										// 500kb
										utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Ingreso a Menor a 500kb",
												Thread.currentThread().getStackTrace());

										List<T7782anexaform> lstanexasForm = formularioAnexas.getAnexasFormularioList();

										if (lstanexasForm.size() > 0) {
											for (T7782anexaform anexasForm : lstanexasForm) {
												T7782anexaform anexas = new T7782anexaform();
												T7782anexaformPK anexasPk = new T7782anexaformPK();
												anexasPk.setNumOrd(formularioAnexas.getNumOrd());
												anexasPk.setCodExtanx(anexasForm.getT7782anexaformPK().getCodExtanx());
												anexasPk.setCodNomanx(anexasForm.getT7782anexaformPK().getCodNomanx());
												anexas.setT7782anexaformPK(anexasPk);
												anexas.setPerTri(anexasForm.getPerTri());
												anexas.setCodUsuregis(formularioAnexas.getCodUsuregis());
												String cadenaCodificada = String.valueOf(anexasForm.getDesTabanx());
												try {
													anexas.setDesTabanx(URLDecoder.decode(cadenaCodificada, "UTF-8"));
												} catch (UnsupportedEncodingException ex) {
													utilLog.imprimirLog(ConstantesUtils.LEVEL_ERROR, ex.getMessage(),
															Thread.currentThread().getStackTrace());
												}
												em.persist(anexas);
											}
											em.flush();
											utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
													"Guardo Anexas Formulario - CargaPDT y menor 500kb ",
													Thread.currentThread().getStackTrace());
											resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
											resultadoProcesoBatchFormularioBean.setMsg(
													MENSAJE_ANEXAS_EXITOSO + " : " + formularioAnexas.getCodFor());
										}
									} else { // mayor a 500kb
										utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
												"ingreso anexas formulario PDTmayor500",
												Thread.currentThread().getStackTrace());

										List<T7782anexaform> lstanexasForm = this
												.procesarArchivosAnexas(formularioAnexas);
										if (!lstanexasForm.isEmpty()) {
											for (T7782anexaform anexasForm : lstanexasForm) {
												em.persist(anexasForm);
											}
											em.flush();
											utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
													"Guardo Anexas Formulario PDT>500",
													Thread.currentThread().getStackTrace());
											resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
											resultadoProcesoBatchFormularioBean.setMsg(
													MENSAJE_ANEXAS_EXITOSO + " : " + formularioAnexas.getCodFor());
										} else {
											utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
													"No existe Anexas PDT>500 para el Formulario "
															+ formularioAnexas.getCodFor(),
													Thread.currentThread().getStackTrace());
											resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
											resultadoProcesoBatchFormularioBean
													.setMsg(MENSAJE_ANEXAS_NO_EXISTE + formularioAnexas.getCodFor());
										}
									}
								} else {
									// NO ES pdt pero es Formulario Simplificado
									if (!COD_FOR_BOLETA.equals(formularioAnexas.getCodFor())
											&& !COD_FORM_0621.equals(formularioAnexas.getCodFor())
											&& !COD_TIP_FOR_PDT.equals(formularioAnexas.getCodTipFor())) {

										List<T7782anexaform> lstanexasForm = this
												.obtenerAnexasLegacyFormulario(formularioAnexas);
										if (lstanexasForm.size() > 0) {
											for (T7782anexaform anexasForm : lstanexasForm) {
												em.persist(anexasForm);
											}
											em.flush();
											utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
													"Guardo Anexas Legacy Formulario " + formularioAnexas.getCodFor(),
													Thread.currentThread().getStackTrace());
											//this.registrarProcesoBandeja(t7777bandprespago.getNumBandeja(),
											//		COD_PRO_BAN_ANEXAS, IND_PROCESO_BANDEJA_EXITOSO,
											//		formularioAnexas.getCodUsuregis(), em);
											resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
											resultadoProcesoBatchFormularioBean.setMsg(
													MENSAJE_ANEXAS_EXITOSO + " : " + formularioAnexas.getCodFor());
										} else {
											utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
													"No existe Anexas para el CodFor " + formularioAnexas.getCodFor(),
													Thread.currentThread().getStackTrace());
											//this.registrarProcesoBandeja(t7777bandprespago.getNumBandeja(),
											//		COD_PRO_BAN_ANEXAS, IND_PROCESO_BANDEJA_EXITOSO,
											//		formularioAnexas.getCodUsuregis(), em);
											resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
											resultadoProcesoBatchFormularioBean
													.setMsg(MENSAJE_ANEXAS_NO_EXISTE + formularioAnexas.getCodFor());
										}

									} else {
										utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
												" No existe Anexas para el CodFor  " + formularioAnexas.getCodFor(),
												Thread.currentThread().getStackTrace());
										resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
										resultadoProcesoBatchFormularioBean
												.setMsg(MENSAJE_ANEXAS_NO_EXISTE + formularioAnexas.getCodFor());
									}
								}
							} else {
								verificaCantAnexas(em, resultadoProcesoBatchFormularioBean, formularioAnexas,
										cuentaAnexas);
							}
						}
					}
				});
			}
		}
		return resultadoProcesoBatchFormularioBean;
	}

	private void verificaCantAnexas(EntityManager em,
			ResultadoProcesoBatchFormularioBean resultadoProcesoBatchFormularioBean,
			FormularioJsonBean formularioAnexas, Integer cuentaAnexas) {
		if (formularioAnexas.getCargaPdt() != null && COD_TIP_FOR_PDT.equals(formularioAnexas.getCodTipFor())) {
			Integer tamanio = configuracion.getConfig(ConfiguracionBatch.class).getTamanioFileSize();
			if (formularioAnexas.getCargaPdt().getCntTamArch() <= tamanio
					&& cuentaAnexas != formularioAnexas.getAnexasFormularioList().size()) { // menor
																							// a
																							// 500kb
				utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Ingreso a Menor a 500kb",
						Thread.currentThread().getStackTrace());
				em.createNativeQuery("delete t7782anexaform where num_ord= ?1 ")
						.setParameter(1, formularioAnexas.getNumOrd()).executeUpdate();
				resultadoProcesoBatchFormularioBean.setCod(COD_RESP_CON_ERROR);
				resultadoProcesoBatchFormularioBean.setMsg("borra anexas para reprocesar nuevamente");
			}else{
				resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
				resultadoProcesoBatchFormularioBean.setMsg(MENSAJE_ANEXAS_EXISTE);
			}
		} else {
			resultadoProcesoBatchFormularioBean.setCod(COD_RESP_EXITOSO);
			resultadoProcesoBatchFormularioBean.setMsg(MENSAJE_ANEXAS_EXISTE);
		}
	}

	public void registrarAnexasFormularioBatch(FormularioJsonBean t7778formulario) {

		utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Ingreso al metodo registrarAnexasFormularioBatch: ",
				Thread.currentThread().getStackTrace());

		if (t7778formulario != null) {
			this.agregarLogBean(request, t7778formulario);
			if (!ProcesoBatchFormularioConstantes.COD_FOR_BOLETA.equals(t7778formulario.getCodFor())
					&& !ProcesoBatchFormularioConstantes.COD_FORM_0621.equals(t7778formulario.getCodFor())
					&& !COD_TIP_FOR_PDT.equals(t7778formulario.getCodTipFor())) {

				List<T7782anexaform> lstanexasForm = this.obtenerAnexasLegacyFormulario(t7778formulario);
				if (lstanexasForm.size() > 0) {
					t7778formulario.setAnexasFormularioList(lstanexasForm);
				}
			} else {
				// 621
				if (t7778formulario.getAnexasFormularioList() != null
						&& !t7778formulario.getAnexasFormularioList().isEmpty()) {
					this.seteaAnexasFormularioVirtual(t7778formulario);
				} else {
					if (t7778formulario.getCargaPdt() != null) {
						Integer tamanio = configuracion.getConfig(ConfiguracionBatch.class).getTamanioFileSize();
						utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "tamanio " + String.valueOf(tamanio),
								Thread.currentThread().getStackTrace());

						if (t7778formulario.getCargaPdt().getCntTamArch() <= tamanio) {
							utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Existe en cargaPDT y es menor 500kb ",
									Thread.currentThread().getStackTrace());

						} else {
							utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Flujo Normal PDTMayor500",
									Thread.currentThread().getStackTrace());

							List<T7782anexaform> lstanexasForm = this.procesarArchivosAnexas(t7778formulario);
							t7778formulario.setAnexasFormularioList(lstanexasForm);
							utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
									"Tamanio ListaT7778 " + t7778formulario.getAnexasFormularioList().size(),
									Thread.currentThread().getStackTrace());
						}
						t7778formulario.setCodEstado(ProcesoBatchFormularioConstantes.V_COD_ESTADO_PDT);

					}
				}
			}
		}
	}

	private void seteaAnexasFormularioVirtual(FormularioJsonBean t7778formulario) {
		t7778formulario.getAnexasFormularioList().forEach(anexas -> {
			utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Ingreso al metodo seteaAnexasFormularioVirtual: ",
					Thread.currentThread().getStackTrace());

			anexas.getT7782anexaformPK().setNumOrd(t7778formulario.getNumOrd());
			if (t7778formulario.getNumCargaPdt() != null && anexas.getDesTabanx().length() > 0) {

				String cadenaCodificada = String.valueOf(anexas.getDesTabanx());

				try {
					anexas.setDesTabanx(URLDecoder.decode(cadenaCodificada, "UTF-8"));
				} catch (UnsupportedEncodingException ex) {
					utilLog.imprimirLog(ConstantesUtils.LEVEL_ERROR, ex.getMessage(),
							Thread.currentThread().getStackTrace());
				}
			}
		});
	}

	private List<T7782anexaform> procesarArchivosAnexas(FormularioJsonBean item) {

		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Ingreso al metodo procesarArchivosAnexas: ",
				Thread.currentThread().getStackTrace());

		ArchivoZIPBatch archivoZIPBatch = new ArchivoZIPBatch();
		ArchivaPDTBean pdtBean = new ArchivaPDTBean();
		List<T7782anexaform> lstAnexaForm = new ArrayList<>();
		String rutaNFSUno = configuracion.getConfig(ConfiguracionBatch.class).getFileserver1();
		String rutaNFSDos = configuracion.getConfig(ConfiguracionBatch.class).getFileserver2();

		try {

			pdtBean.setNombreFilePDT(item.getCargaPdt().getDesArchivo());
			pdtBean.setCodFor(pdtBean.getNombreFilePDT().substring(0, 4));
			pdtBean.setRutaTemporal(SUNATPDT_TMP);
			pdtBean.setBarritaTemporal(SUNATPDT_TMP_BARRA);

			boolean check = new File(rutaNFSUno, pdtBean.getNombreFilePDT()).exists();
			if (!check) {
				boolean check2 = new File(rutaNFSDos, pdtBean.getNombreFilePDT()).exists();
				if (!check2) {
					throw new RecursoNoDisponibleException("Problemas con el Repositorio FileServer-NFS");
				} else {
					utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Existe en FileServer2",
							Thread.currentThread().getStackTrace());
					pdtBean.setRutaFinalDescomprimido(rutaNFSDos
							+ pdtBean.getNombreFilePDT().substring(0, pdtBean.getNombreFilePDT().lastIndexOf(".")));
					pdtBean.setRutaFileZIP(rutaNFSDos + pdtBean.getNombreFilePDT());
				}
			} else {
				utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Existe en FileServer1",
						Thread.currentThread().getStackTrace());
				pdtBean.setRutaFinalDescomprimido(rutaNFSUno
						+ pdtBean.getNombreFilePDT().substring(0, pdtBean.getNombreFilePDT().lastIndexOf(".")));
				pdtBean.setRutaFileZIP(rutaNFSUno + pdtBean.getNombreFilePDT());
			}
			pdtBean.setRutaFileEncriptado(pdtBean.getRutaFinalDescomprimido() + ENCRIPTADOS);
			pdtBean.setRutaFileDesencriptado(pdtBean.getRutaFinalDescomprimido() + DESENCRIPTADOS);
			Map<String, Object> mapaAnexasIn = archivoZIPBatch.unZipEntryVarios(pdtBean);

			if (mapaAnexasIn.size() > 0) {
				lstAnexaForm = this.transformarPlanoLista(mapaAnexasIn, item);
				utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Tamanio anexasFormulario : " + lstAnexaForm.size(),
						Thread.currentThread().getStackTrace());
			} else {
				utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
						"No se proceso Mapa Trama Anexas para el archivo Cargado",
						Thread.currentThread().getStackTrace());
			}
		} catch (Exception excepcion) {
			utilLog.imprimirLog(ConstantesUtils.LEVEL_ERROR,
					"Error en Metodo ArchivoZIP.unZipEntryVarios ".concat(excepcion.getMessage()),
					Thread.currentThread().getStackTrace());
		}
		return lstAnexaForm;
	}

	public List<T7782anexaform> obtenerAnexasLegacyFormulario(FormularioJsonBean formulario) {

		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Ingreso al metodo obtenerAnexasLegacyFormulario: ",
				Thread.currentThread().getStackTrace());

		List<T7782anexaform> lstanexasForm = new ArrayList<>();
		T7782anexaform anexasFormComodin = null;
		T7778formulario t7778formulario = new T7778formulario();
		t7778formulario.setCodFor(formulario.getCodFor());
		t7778formulario.setNumOrd(formulario.getNumOrd());
		t7778formulario.setNumRuc(formulario.getNumRuc());
		t7778formulario.setPerTri(formulario.getPerTri());

		try {
			// metrear
			anexasFormComodin = t6630cretperDAO.obtenerAnexasJson(t7778formulario,
					ProcesoBatchFormularioConstantes.VALOR_CAB);

			if (anexasFormComodin != null) {
				anexasFormComodin.setCodUsuregis(formulario.getCodUsuregis());
				lstanexasForm.add(anexasFormComodin);
			} else {
				utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
						" No existe anexas (CABECERA) para el formulario codigo: " + t7778formulario.getCodFor(),
						Thread.currentThread().getStackTrace());

			}
			// metrear
			anexasFormComodin = t6630cretperDAO.obtenerAnexasJson(t7778formulario,
					ProcesoBatchFormularioConstantes.VALOR_DET);

			if (anexasFormComodin != null) {
				anexasFormComodin.setCodUsuregis(formulario.getCodUsuregis());
				lstanexasForm.add(anexasFormComodin);
			} else {
				utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
						" No existe anexas (DETALLE) para el formulario codigo: " + t7778formulario.getCodFor(),
						Thread.currentThread().getStackTrace());

			}
			// metrear
			anexasFormComodin = t6630cretperDAO.obtenerAnexasJson(t7778formulario,
					ProcesoBatchFormularioConstantes.VALOR_ADI);

			if (anexasFormComodin != null) {
				anexasFormComodin.setCodUsuregis(formulario.getCodUsuregis());
				lstanexasForm.add(anexasFormComodin);
			} else {
				utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
						" No existe anexas (ADICIONAL) para el formulario codigo: " + t7778formulario.getCodFor(),
						Thread.currentThread().getStackTrace());

			}
			if (ProcesoBatchFormularioConstantes.VALOR_0697.equals(t7778formulario.getCodFor())
					|| ProcesoBatchFormularioConstantes.VALOR_0633.equals(formulario.getCodFor())) {
				// metrear
				anexasFormComodin = t6630cretperDAO.obtenerAnexasJson(t7778formulario,
						ProcesoBatchFormularioConstantes.VALOR_ADI2);

				if (anexasFormComodin != null) {
					anexasFormComodin.setCodUsuregis(formulario.getCodUsuregis());
					lstanexasForm.add(anexasFormComodin);
				} else {
					utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
							" No existe anexas (ADICIONAL2) para el formulario codigo: " + t7778formulario.getCodFor(),
							Thread.currentThread().getStackTrace());

				}
			}

		} catch (JsonProcessingException excepcion) {
			utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
					"Error en Metodo t6630cretperDAO.obtenerAnexasJson ".concat(excepcion.getMessage()),
					Thread.currentThread().getStackTrace());
		}

		return lstanexasForm;
	}

	public List<Long> obtenerAnexasFormulario() {

		Date hoy = new Date();

		Integer tamanioFileSize = configuracion.getConfig(ConfiguracionBatch.class).getTamanioFileSize();// -1
		Date fechaDesde = procesoBatchFormularioUtil.sumarRestarDiasFecha(hoy);
		Date fechaHasta = procesoBatchFormularioUtil.sumarRestarHorasFecha(hoy);
		// resultados = new ArrayList<Long>();

		return t7778FormulariosDAO.obtenerAnexasFormulario(fechaDesde, fechaHasta, tamanioFileSize);
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

	public T7777bandprespago obtenerBandPresPago(Long idBandeja) {
		return t7777dao.findById(idBandeja);
	}

	public BandejaJsonBean formatearDatos(String jsonFromBandeja) {

		BandejaJsonBean bandejaJsonBean = new BandejaJsonBean();

		try {
			bandejaJsonBean = jsonUtilParser.jsonToEntity(jsonFromBandeja, BandejaJsonBean.class);
		} catch (Exception jpe) {
			utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "  Exception {} " + jpe,
					Thread.currentThread().getStackTrace());
		}
		return bandejaJsonBean;
	}

	public List<T7782anexaform> transformarPlanoLista(final Map<String, Object> mapaAnexasIn,
			final FormularioJsonBean formulario) {
		AtomicInteger contador = new AtomicInteger(0);

		final List<T7782anexaform> listaAnexa = new ArrayList<>();
		mapaAnexasIn.entrySet().stream().parallel().forEachOrdered(valorMapa -> {

			if (valorMapa != null) {
				contador.getAndIncrement();
				final T7782anexaformPK anexaformPK = new T7782anexaformPK();
				final T7782anexaform t7782anexaform = new T7782anexaform();
				final String cadenaCodificada = String.valueOf(valorMapa.getValue());
				anexaformPK.setNumOrd(formulario.getNumOrd());
				anexaformPK.setCodExtanx(valorMapa.getKey().substring(0, 3));
				anexaformPK.setCodNomanx(valorMapa.getKey().substring(4));
				t7782anexaform.setT7782anexaformPK(anexaformPK);
				t7782anexaform.setPerTri(formulario.getPerTri());
				t7782anexaform.setCodUsuregis(formulario.getCodUsuregis());
				t7782anexaform.setDesTabanx(cadenaCodificada);
				listaAnexa.add(t7782anexaform);
			}
		});
		utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Total Anexas-bean: " + listaAnexa.size(),
				Thread.currentThread().getStackTrace());

		if (contador.get() == 0)
			utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG,
					"CargaPDT Nro. " + formulario.getNumCargaPdt() + " no contiene Anexas ",
					Thread.currentThread().getStackTrace());
		return listaAnexa;
	}

	private void registrarProcesoBandeja(Long numBandeja, String codProceso, Character indProceso, String codUsuario,
			EntityManager entityManager) {

		T7794procesoband procesoBandeja = new T7794procesoband();
		T7794procesobandPK procesoBandejaPK = new T7794procesobandPK();
		Calendar fechaActual = Calendar.getInstance();

		Integer cuenta = t7794dao.verificaProcesoBandeja(numBandeja, codProceso);

		procesoBandejaPK.setNumBandeja(numBandeja);
		procesoBandejaPK.setCodProBan(codProceso);
		procesoBandeja.setT7794procesobandPK(procesoBandejaPK);
		procesoBandeja.setIndProBan(indProceso); // IND_PROCESO_BANDEJA_EXITOSO
		procesoBandeja.setNumIntentos(PRIMER_INTENTO);
		procesoBandeja.setCodUsuregis(codUsuario);
		procesoBandeja.setIndDel(VALOR_CERO);
		procesoBandeja.setFecRegis(fechaActual.getTime());
		if (cuenta <= 0) {// no existe
			entityManager.persist(procesoBandeja);
			entityManager.flush();
		} else {
			entityManager.merge(procesoBandeja);
			entityManager.flush();
		}

	}

	private T7794procesoband buscarProcesoBandeja(Long idBandeja, String codProceso) {

		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO, "Ingreso al metodo buscarProcesoBandeja ",
				Thread.currentThread().getStackTrace());

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

	private T7794procesoband registroProcesoBandejaBatchFormulario(Long idBandeja, String codProceso,
			Character indProcesoBandeja, String usuario,T7794procesoband t7794procesoband) {

		utilLog.imprimirLog(ConstantesUtils.LEVEL_INFO,
				"Ingreso al metodo registroProcesoBandejaBatchFormularioReproceso ",
				Thread.currentThread().getStackTrace());
		//T7794procesoband t7794procesoband = buscarProcesoBandeja(idBandeja, codProceso);
		T7794procesobandPK procesoBandActualizar1 = new T7794procesobandPK();
		T7794procesoband procesoBandActualizarEntity1 = new T7794procesoband();
		Calendar fechaActual = null;
		Calendar fechaVacia = null;
		int numIntentos = 0;
		fechaVacia = Calendar.getInstance();
		fechaVacia.set(1, 0, 1, 0, 0, 0);
		fechaActual = Calendar.getInstance();
		//boolean retorno = true;

		if (t7794procesoband != null) { // ya existe
			utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "Se actualiza ProcesoBandeja " + idBandeja,
					Thread.currentThread().getStackTrace());
				
				numIntentos = IND_PROCESO_BANDEJA_EN_PROCESO.equals(t7794procesoband.getIndProBan())? numIntentos + ProcesoBatchFormularioConstantes.PRIMER_INTENTO : ProcesoBatchFormularioConstantes.PRIMER_INTENTO+t7794procesoband.getNumIntentos();
				t7794procesoband.setNumIntentos(numIntentos);
				t7794procesoband.setIndProBan(indProcesoBandeja);
				t7794procesoband.setFecModif(fechaActual.getTime());
				t7794procesoband.setCodUsumodif(usuario);
				t7794procesoband.setIndDel(VALOR_CERO);
				t7794dao.update(t7794procesoband);
		} else { // no existe
			numIntentos = numIntentos + ProcesoBatchFormularioConstantes.PRIMER_INTENTO;
			procesoBandActualizar1.setNumBandeja(idBandeja);
			procesoBandActualizar1.setCodProBan(codProceso);
			procesoBandActualizarEntity1.setT7794procesobandPK(procesoBandActualizar1);
			procesoBandActualizarEntity1.setIndProBan(indProcesoBandeja);
			procesoBandActualizarEntity1.setNumIntentos(numIntentos);
			procesoBandActualizarEntity1.setFecRegis(fechaActual.getTime());
			procesoBandActualizarEntity1.setCodUsuregis(usuario);
			procesoBandActualizarEntity1.setIndDel(VALOR_CERO);
			//procesoBandActualizarEntity1.setFecModif(fechaActual.getTime());
			// if
			// (indicadorProceso.equals(ProcesoBatchFormularioConstantes.IND_PROCESO_BANDEJA_ERRONEO))
			// {

			t7794dao.getEntityManager().persist(procesoBandActualizarEntity1);
			t7794dao.getEntityManager().flush();
			t7794procesoband=procesoBandActualizarEntity1;
		}
		
		return t7794procesoband;
	}

	public List<Long> obtenerAnexaCasillaFormulario(String codProBand) {
		Date hoy = new Date();

		Date fechaDesde = procesoBatchFormularioUtil.sumarRestarDiasFecha(hoy);
		Date fechaHasta = procesoBatchFormularioUtil.sumarRestarHorasFecha(hoy);

		return t7778FormulariosDAO.obtenerAnexaCasilla(fechaDesde, fechaHasta, codProBand);
	}
	
	public List<AnexaJsonBean> obtenerCantidadAnexa(String mes, String dia){
		WSLoggerBean logBean = (WSLoggerBean) request.getAttribute(ConstantesUtils.LOG_BEAN);
		logBean.setNumBandeja(String.valueOf(0));
		request.setAttribute(ConstantesUtils.LOG_INPUT, logBean.getNumBandeja());
		List<AnexaJsonBean> anexaBeanList = new ArrayList<>();
			List<String> listData = t7777dao.createNativeQuery("select arc_data::json from t7777bandprespago where date(fec_ope) = EXTEND(MDY("+mes+","+dia+",2018), YEAR to SECOND) and ind_del = '0' ").getResultList();
			utilLog.imprimirLog(ConstantesUtils.LEVEL_DEBUG, "lista: "+listData.size(), Thread.currentThread().getStackTrace());
			for(String data : listData){
				BandejaJsonBean bandejaBean = this.formatearDatos(data);
				List<FormularioJsonBean> lstT7778formulario = bandejaBean.getListaFormulario();
				if (!lstT7778formulario.isEmpty()) {

					lstT7778formulario.stream().forEachOrdered(formularioAnexas -> {
							if (formularioAnexas != null && formularioAnexas.getCargaPdt() != null
									&& COD_TIP_FOR_PDT.equals(formularioAnexas.getCodTipFor())) {
								Integer tamanio = configuracion.getConfig(ConfiguracionBatch.class)
										.getTamanioFileSize();
	
								if (formularioAnexas.getCargaPdt().getCntTamArch() <= tamanio) {
									List<T7782anexaform> lstanexasForm = formularioAnexas.getAnexasFormularioList();
									if(!lstanexasForm.isEmpty()){
										//codfor|norden|anex|cont \r\n
										for (T7782anexaform anexasForm : lstanexasForm) {
											try {
												anexasForm.setDesTabanx(URLDecoder.decode(anexasForm.getDesTabanx(), "UTF-8"));
											} catch (UnsupportedEncodingException ex) {
												utilLog.imprimirLog(ConstantesUtils.LEVEL_ERROR, ex.getMessage(),
														Thread.currentThread().getStackTrace());
											}
											String[] contador = anexasForm.getDesTabanx().split("\r\n");
											AnexaJsonBean anexaJsonBean = new AnexaJsonBean();
											anexaJsonBean.setCodFor(formularioAnexas.getCodFor());
											anexaJsonBean.setNumOrd(formularioAnexas.getNumOrd());
											anexaJsonBean.setNomAnx(anexasForm.getT7782anexaformPK().getCodNomanx());
											anexaJsonBean.setCont(contador.length);
											anexaBeanList.add(anexaJsonBean);
										}
									}
								}
							
							}
						
					});
				}
		}
		return anexaBeanList;
		
	}
	
	private Date sumarRestarDiasFechaPedido(Date hoy) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hoy);
		calendar.add(Calendar.DAY_OF_YEAR, 1); 
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
	private Date RestarUnDiaFechaPedido(Date hoy) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hoy);
		calendar.add(Calendar.DAY_OF_YEAR, -1); 
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

}
