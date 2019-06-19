package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util;

import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.BaseException;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.ConstantesUtils;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.UtilLog;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.batch.EncriptacionZIP;
import javax.crypto.Cipher;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.IND_VAL_PDT_CERO;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.COD_FORM_0601;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_CON;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_TXT;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_TXT2;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_CAB;
import static pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes.VALOR_DET;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.ArchivaPDTBean;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.batch.Base24Util;
import java.util.Comparator;
import com.google.common.base.Splitter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Clase ArchivoZIPBatch de ProcesoBatchFormulario.

 *

 * @author Sapia

 * @version 1.0

 * @since 12-10-2016.
 */
public class ArchivoZIPBatch {


	public Map<String, Object> unZipEntryVarios(ArchivaPDTBean pdtBean) throws BaseException {

			final Map<String, Object> mapaAnexasOut = new HashMap<>();
			final String sNomArchivoIni = pdtBean.getNombreFilePDT().substring(0, 11);
			final String sNombreArchivoFin = pdtBean.getNombreFilePDT().substring(11, 56);
			String keyBase24 = "";
			EncriptacionUtil encriptacionUtil = new EncriptacionUtil();

			final Base24Util base24 = Base24Util.getInstance();
			//metrear
			final String rpta = base24.descomprimeNombrePDT(sNombreArchivoFin);
			if (!("0".equals(rpta))) {
				keyBase24 = rpta.substring(0, 32);
			}

			final String nameFinal = sNomArchivoIni + keyBase24;

			final String keyFull = EncriptacionZIP.getSemilla(nameFinal.substring(11, 43));
			File carpetaExtraer =null;
			File carpetaExtraerDecryp=null;
			if(!COD_FORM_0601.equals(pdtBean.getCodFor())){
				carpetaExtraer = new File(pdtBean.getRutaFileEncriptado()+pdtBean.getRutaTemporal());
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG, "carpetaExtraer "+ carpetaExtraer.getAbsolutePath(), Thread.currentThread().getStackTrace());
				carpetaExtraerDecryp = new File(pdtBean.getRutaFileDesencriptado()+pdtBean.getRutaTemporal());
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG, "carpetaExtraerDecryp "+ carpetaExtraerDecryp.getAbsolutePath(), Thread.currentThread().getStackTrace());
			}else{
				carpetaExtraer = new File(pdtBean.getRutaFileEncriptado());
				carpetaExtraerDecryp = new File(pdtBean.getRutaFileDesencriptado());
			}

			try {
				boolean acepto = true;
				//crear carpeta temporal
				this.borrarCrearDirectorio(pdtBean.getRutaFinalDescomprimido(),carpetaExtraer,carpetaExtraerDecryp);
					if(!COD_FORM_0601.equals(pdtBean.getCodFor())){
						encriptacionUtil.desempaquetarArchivoAnt(pdtBean.getRutaFileZIP(), pdtBean.getRutaFileEncriptado(), pdtBean.getBarritaTemporal());
						extraerCarpeta(pdtBean.getRutaFileZIP(), pdtBean.getRutaFileEncriptado()+pdtBean.getRutaTemporal(), pdtBean.getRutaFileDesencriptado()+pdtBean.getRutaTemporal(), keyFull, carpetaExtraer,pdtBean.getBarritaTemporal());
						extraerCarpetaDecryp(pdtBean.getRutaFileDesencriptado()+pdtBean.getRutaTemporal(), mapaAnexasOut, carpetaExtraerDecryp,pdtBean.getBarritaTemporal(),nameFinal);
					}else{
						encriptacionUtil.desempaquetarArchivo(pdtBean.getRutaFileZIP(), pdtBean.getRutaFileEncriptado(), pdtBean.getBarritaTemporal());
						extraerCarpeta(pdtBean.getRutaFileZIP(), pdtBean.getRutaFileEncriptado(), pdtBean.getRutaFileDesencriptado(), keyFull, carpetaExtraer,pdtBean.getBarritaTemporal());
						extraerCarpetaDecryp(pdtBean.getRutaFileDesencriptado(), mapaAnexasOut, carpetaExtraerDecryp,pdtBean.getBarritaTemporal(),nameFinal);
					}

			} catch (Exception e) {
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
			}

			return mapaAnexasOut;
	}


	private void borrarCrearDirectorio(String rutaCarpeta,File carpetaEncriptado, File carpetaDesencriptado) throws IOException {
		File carpetaPDT = new File(rutaCarpeta);
		boolean created =false;

		if(carpetaPDT.exists()){ //existe borrar
			Path pathToBeDeleted  = Paths.get(rutaCarpeta);
			Files.walk(pathToBeDeleted)
			.sorted(Comparator.reverseOrder())
			.map(Path::toFile)
			.forEach(File::delete);
		}
		created = carpetaEncriptado.mkdirs();
		if(created)carpetaDesencriptado.mkdirs();
	}

	private static void extraerCarpeta(final String newNombreFilePDT, final String rutaFileEncryp, final String rutaFileDecryp,  final String keyFull, final File carpetaExtraer,String indBarrita) {

		if (carpetaExtraer.exists()) {
			final File[] ficheros = carpetaExtraer.listFiles();
			new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG, "ficheros "+ficheros, Thread.currentThread().getStackTrace());
			
			Arrays.stream(ficheros).parallel().forEach(file -> {
				if (file.isFile()){
					try {
						if (!(file.getName().equalsIgnoreCase(newNombreFilePDT))) {
							new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG, "FILE "+file.getName(), Thread.currentThread().getStackTrace());
							final String rutaCompletaFile = rutaFileEncryp+indBarrita + file.getName();
							EncriptacionUtil.procesaArchivo(Cipher.DECRYPT_MODE, rutaCompletaFile,rutaCompletaFile+".ZIP", keyFull);
							EncriptacionUtil.desempaquetarArchivo(rutaCompletaFile + ".ZIP", rutaFileDecryp,indBarrita);
						}
					} catch (final BaseException e) {
						new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
					}
				} else {
					new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, "No se proceso el file", Thread.currentThread().getStackTrace());
				}
			});
		} else {
			new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, "No existe el directorio en proceso", Thread.currentThread().getStackTrace());

		}
	}

	private static void extraerCarpetaDecryp(final String rutaFileDecryp, final Map<String, Object> mapaAnexasOut, final File carpetaExtraerDecryp,String indBarrita,String nameFile) {

		List<String> listaTrama = Stream.of(VALOR_TXT, VALOR_CON, VALOR_CAB,VALOR_DET,VALOR_TXT2).collect(Collectors.toList());
		if (carpetaExtraerDecryp.exists()) {

			final File[] archivos = carpetaExtraerDecryp.listFiles();

			String nameFileCon=rutaFileDecryp +indBarrita+nameFile+"."+VALOR_CON;

				final String contenidoArchivo=EncriptacionUtil.leerFichero(nameFileCon);
				final List<String> listaTramas = Splitter.on('|').splitToList(contenidoArchivo);
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG,"contenidofileCON " +listaTramas, Thread.currentThread().getStackTrace());

				Arrays.asList(archivos).forEach(filex -> {

				if(filex.length()>IND_VAL_PDT_CERO){ ///1024

					final String rutaOutputFile = rutaFileDecryp +indBarrita+ filex.getName();


					final String extensionFile = rutaOutputFile.substring(rutaOutputFile.length() - 3, rutaOutputFile.length());

					for (int i = 0; i < listaTramas.size(); i++) {
						final boolean match = listaTramas.get(i).contains("."+extensionFile);

						if(match){ //existe en archivo CON

							final boolean match1 = listaTrama.stream().anyMatch(extensionFile::contains);


							if(!match1){ //no debe existir en DET CAB
								final String tramalinea = EncriptacionUtil.leerFichero(rutaOutputFile);
								if (!"".equals(tramalinea)) {
										mapaAnexasOut.put(extensionFile+"|"+listaTramas.get(i+1), tramalinea);
								}
							}
						}
					}
				}
				});
			}
			new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG,"mapaAnexasOut " +mapaAnexasOut.size(), Thread.currentThread().getStackTrace());

	}
}
