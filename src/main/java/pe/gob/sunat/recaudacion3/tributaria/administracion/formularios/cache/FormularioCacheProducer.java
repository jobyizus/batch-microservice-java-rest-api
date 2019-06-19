package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

public class FormularioCacheProducer {

    @Inject
    private CacheManager cacheManager;

    @Produces
    @Named("jsonAnexaCache")
    @ApplicationScoped
    public Cache<String, String> jsonAnexaCache() {
        return cacheManager.getCache("jsonAnexa");
    }
}
