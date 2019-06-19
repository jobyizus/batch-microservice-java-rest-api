package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.cache;

import pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.util.config.bean.MensajeBean;

import javax.cache.Cache;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

public class FormularioCache {

    @Inject
    @Named("jsonAnexaCache")
    Cache<String, String> jsonAnexaCache;

    public Optional<MensajeBean> getJsonBandeja(String key) {
        if (jsonAnexaCache.containsKey(key)) {
            String valParam = jsonAnexaCache.get(key);
            return Optional.of(new MensajeBean(key, valParam));
        }
        return Optional.empty();
    }

    public void saveJsonAnexa(String key, String valParam) {
        jsonAnexaCache.put(key, valParam);
    }
}
