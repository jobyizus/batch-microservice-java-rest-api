package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util;

import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.BaseException;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.ConstantesUtils;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.UtilLog;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.batch.EncriptacionZIP;
import javax.crypto.Cipher;
import javax.enterprise.inject.Vetoed;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Clase EncriptacionUtil de ProcesoBatchFormulario.

 *

 * @author Sapia

 * @version 1.0

 * @since 12-10-2016.
 */
@Vetoed
public class EncriptacionUtil {


	public EncriptacionUtil() {
	}

	private static final int BUFFER = 2048;

	/**
	 * Metodo que devuelve la ruta de archivo de salida.

	 *

	 * @param rutaOutputFile

	 * @return String.
	 */
	public static String leerFichero(final String rutaOutputFile) {
		//String lineaSalida = "";
		StringBuilder lineaSalida=new StringBuilder();
		
		try (
			FileReader fr = new FileReader(rutaOutputFile);
			BufferedReader br = new BufferedReader(fr)) {
			String linea;
			while ((linea = br.readLine()) != null) {
				linea = linea.trim();
				if (linea != null) {
					lineaSalida = lineaSalida.append(linea).append("\r\n");
				}
			}
		} catch (final Exception e) {
			new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, e.getMessage(), Thread.currentThread().getStackTrace());
		}

		return lineaSalida.toString();
	}

	/**
	 * M�todo que procesa archivo.

	 *

	 * @param mode

	 * @param inputFile

	 * @param outputFile

	 * @param password

	 * @throws BaseException
	 */
	public static void procesaArchivo(final int mode, final String inputFile, final String rutaFileOut,final String password) throws BaseException {

		try (
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(inputFile));
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(rutaFileOut))) {
			if (mode == Cipher.ENCRYPT_MODE) {
				EncriptacionZIP.encrypt(is, os, password);
			} else if (mode == Cipher.DECRYPT_MODE) {
				EncriptacionZIP.decrypt(is, os, password);
			}
		} catch (final IOException e) {
			throw new BaseException(e.getMessage(), e);
		}
	}

	public static boolean desempaquetarArchivo(final String sNombreBase, final String rutaFileOut, String indBarrita) throws BaseException {
			FileInputStream fis = null;
			FileOutputStream fos = null;
			ZipInputStream zis = null;
			boolean acepto = true;
			try {
				fis = new FileInputStream(sNombreBase);
				zis = new ZipInputStream(fis);
				ZipEntry salida;
				while ((salida = zis.getNextEntry()) != null) {
					String[] splitNombre = salida.getName().split("/");
					final String sNomFile = splitNombre[splitNombre.length-1];
					new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG, "salida "+rutaFileOut +indBarrita+sNomFile, Thread.currentThread().getStackTrace());
					
					fos = new FileOutputStream(rutaFileOut +indBarrita+ sNomFile,false);
					int leer;
					final byte[] buffer = new byte[BUFFER];
					while (0 < (leer = zis.read(buffer))) {
						fos.write(buffer, 0, leer);
					}

					fos.close();
					zis.closeEntry();
				}
			} catch (final FileNotFoundException e) {
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
			} catch (final IOException e) {
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
				}
			}
			return acepto;
	}
	
	public static boolean desempaquetarArchivoAnt(final String sNombreBase, final String rutaFileOut, String indBarrita) throws BaseException {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		ZipInputStream zis = null;
		boolean acepto = true;
		try {
			fis = new FileInputStream(sNombreBase);
			zis = new ZipInputStream(fis);
			ZipEntry salida;
			while ((salida = zis.getNextEntry()) != null) {
				String[] splitNombre = salida.getName().split("/");
				final String sNomFile = splitNombre[splitNombre.length-1];
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_DEBUG, "salida1 "+rutaFileOut +indBarrita+sNomFile, Thread.currentThread().getStackTrace());
				
				fos = new FileOutputStream(rutaFileOut +"/SUNATPDT/tmp"+indBarrita+ sNomFile,false);
				int leer;
				final byte[] buffer = new byte[BUFFER];
				while (0 < (leer = zis.read(buffer))) {
					fos.write(buffer, 0, leer);
				}

				fos.close();
				zis.closeEntry();
			}
		} catch (final FileNotFoundException e) {
			new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
		} catch (final IOException e) {
			new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				new UtilLog().imprimirLog(ConstantesUtils.LEVEL_WARN, UtilLog.getStackTraceFromException(e), Thread.currentThread().getStackTrace());
			}
		}
		return acepto;
}



}
