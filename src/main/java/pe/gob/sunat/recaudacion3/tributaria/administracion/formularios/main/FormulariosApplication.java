package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.main;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.main.config.ConfiguracionBatch;
import pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.ws.rest.ProcesoBatchFormularioRestService;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.SunatApplication;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.GenericExceptionMapper;
import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.exception.ObjectNotFoundExceptionMapper;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FormulariosApplication extends SunatApplication<ConfiguracionBatch> {

	  @Override
	  public void onRun(ConfiguracionBatch myConfig, Environment environment) throws Exception {
	    environment.jersey().register(ProcesoBatchFormularioRestService.class);
	    environment.jersey().register(ObjectNotFoundExceptionMapper.class);
	    environment.jersey().register(GenericExceptionMapper.class);
	  }

	  public static void main(String... params) throws Exception {
	    new FormulariosApplication().run(params);
	  }

	@Override
	public void onInitialize(Bootstrap<ConfiguracionBatch> bootstrap) {
		// TODO Auto-generated method stub

	}


}
