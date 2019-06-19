package unitarias;

import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioConstantes;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ProcesoBatchFormularioUtil;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.ArchivoZIPBatch;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.util.EncriptacionUtil;
import com.google.common.base.Splitter;
import java.util.concurrent.atomic.AtomicInteger;


import pe.gob.sunat.recaudacion3.tributaria.administracion.bean.FormularioJsonBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.bean.ArchivaPDTBean;
import pe.gob.sunat.recaudacion3.tributaria.administracion.model.T7782anexaform;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.BaseException;
import java.io.File;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.RecursoNoDisponibleException;

public class ProcesoBatchAnexasFormularioServiceTest {

	ProcesoBatchFormularioUtil procesoBatchFormularioUtil = new ProcesoBatchFormularioUtil();
	ArchivoZIPBatch archivoZIPBatch = new ArchivoZIPBatch();
	private static final String SUNATPDT_TMP_BARRA= "\\";
	private static final String SUNATPDT_TMP= "\\SUNATPDT\\tmp";

//@Test
public void testObtenerNombrTabla()  throws BaseException{

	ArchivaPDTBean pdtBean=new ArchivaPDTBean();
	EncriptacionUtil encriptacionUtil = new EncriptacionUtil();


	List<T7782anexaform> lstAnexaForm = new ArrayList<>();
	String rutaBackup = "D:\\fileserver\\backup\\";
	String rutaNFSUno= "D:\\fileserver\\fileserver1\\";
	String rutaNFSDos = "D:\\fileserver\\fileserver2\\06012018010KKG3LK06J6605575GKC0G017C3NMB7I19MNICCCCCCCCC\\DESENCRIPTADOS\\";
	pdtBean.setNombreFilePDT("0601201801024117996134342025422545200591246");
	pdtBean.setCodExtension("CON");

	pdtBean.setCodFor(pdtBean.getNombreFilePDT().substring(0,4));
	String rutaFinal=rutaNFSDos+pdtBean.getNombreFilePDT()+"."+pdtBean.getCodExtension();

	String contenidoArchivo=encriptacionUtil.leerFichero(rutaFinal);
	List<String> listaTramas = Splitter.on('|').splitToList(contenidoArchivo);
int count1 = 0;
	System.out.println("rutaFinal "+rutaFinal);
	System.out.println("listaTramas "+listaTramas);

	/*for (int i = 0; i < listaTramas.size(); i++) {
		if ((listaTramas.get(i) != null) && (i % 7 == 0)) {
			System.out.println("llave "+i+ "valor "+listaTramas.get(i));

		}
}*/
String varExtension=".IND";

for (int i = 0; i < listaTramas.size(); i++) {

//	for (String element : listaTramas) {
		count1++;
		//boolean match = listaTrama.stream().anyMatch(varExtension::contains);
		boolean match = listaTramas.get(i).contains(varExtension);

		if(match){
			System.out.println("llave "+listaTramas.get(i));
			System.out.println("tabla "+listaTramas.get(i+1));

		}

}

}










//@Test
public void testDesencriptaAES11() throws BaseException{

		ArchivaPDTBean pdtBean=new ArchivaPDTBean();
		List<T7782anexaform> lstAnexaForm = new ArrayList<>();
		String rutaBackup = "D:\\fileserver\\backup\\";
		String rutaNFSUno= "D:\\fileserver\\fileserver1\\";
    String rutaNFSDos = "D:\\fileserver\\fileserver2\\";
		String ENCRIPTADOS = "\\ENCRIPTADOS";
		String DESENCRIPTADOS = "\\DESENCRIPTADOS";
		pdtBean.setNombreFilePDT("07022015000EJ5MJ81G8B9N31LGK2135B7MCKK9B86L2228CCCCCMG0C.ZIP");
		pdtBean.setCodFor(pdtBean.getNombreFilePDT().substring(0,4));
		pdtBean.setRutaTemporal(SUNATPDT_TMP);
		pdtBean.setBarritaTemporal(SUNATPDT_TMP_BARRA);

		boolean check = new File(rutaNFSUno, pdtBean.getNombreFilePDT()).exists();
		if(!check){
			boolean check2 = new File(rutaNFSDos, pdtBean.getNombreFilePDT()).exists();
			if(!check2){
				throw new RecursoNoDisponibleException("Problemas con el Repositorio FileServer-NFS");
			}else{
				System.out.println("Existe en FileServer2 ");
				pdtBean.setRutaFinalDescomprimido(rutaNFSDos+ pdtBean.getNombreFilePDT().substring(0, pdtBean.getNombreFilePDT().lastIndexOf(".")));
				pdtBean.setRutaFileZIP(rutaNFSDos + pdtBean.getNombreFilePDT());
			}
		}else{
			System.out.println("Existe en FileServer1 ");
			pdtBean.setRutaFinalDescomprimido(rutaNFSUno+ pdtBean.getNombreFilePDT().substring(0, pdtBean.getNombreFilePDT().lastIndexOf(".")));
			pdtBean.setRutaFileZIP(rutaNFSUno + pdtBean.getNombreFilePDT());
		}

		pdtBean.setRutaFileEncriptado(pdtBean.getRutaFinalDescomprimido()+ENCRIPTADOS);
		pdtBean.setRutaFileDesencriptado(pdtBean.getRutaFinalDescomprimido()+DESENCRIPTADOS);

    Map<String, Object> mapaAnexasIn = archivoZIPBatch.unZipEntryVarios(pdtBean);
		System.out.println("mapaAnexasIn "+mapaAnexasIn);

		if (mapaAnexasIn.size() > 0) {
        //metrear
        lstAnexaForm = this.transformarPlanoLista(mapaAnexasIn, new FormularioJsonBean());
	  } else {
      System.out.println("No se proceso Anexas para el archivo Cargado");
    }
	}

	public List<T7782anexaform> transformarPlanoLista(final Map<String, Object> mapaAnexasIn, final FormularioJsonBean formulario) {
			AtomicInteger contador = new AtomicInteger(0);

			final List<T7782anexaform> listaAnexa = new ArrayList<>();

			mapaAnexasIn.entrySet().stream().parallel().forEachOrdered(valorMapa -> {

			//	for (int i = 0; i < listaTramaAnexas.size(); i++) {
		//			if ((listaTramaAnexas.get(i) != null) && (i % 2 == 0) && (llave.equals(listaTramaAnexas.get(i)))) {
						contador.getAndIncrement();
						String cadenaCodificada=String.valueOf(valorMapa.getValue());
						//anexaformPK.setNumOrd(formulario.getNumOrd());
						System.out.println(" extension "+valorMapa.getKey().substring(0, 3));
						System.out.println(" tabla "+valorMapa.getKey().substring(4));
						System.out.println(" trama "+cadenaCodificada);
					//	t7782anexaform.setT7782anexaformPK(anexaformPK);
					//	t7782anexaform.setPerTri(formulario.getPerTri());
					//	t7782anexaform.setCodUsuregis(formulario.getCodUsuregis());
				//	t7782anexaform.setDesTabanx(cadenaCodificada);
					//	listaAnexa.add(t7782anexaform);
				//	}
			//	}
			});
			return listaAnexa;
		}



}
