package pe.gob.sunat.recaudacion3.tributaria.administracion.formularios.main.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static javax.persistence.SynchronizationType.SYNCHRONIZED;
import static pe.gob.sunat.tecnologia3.arquitectura.framework.microservices.config.EntityManagerInitializer.EMFORMULARIO;

/**
 * Clase que crea Entity Manager Factory FormularioInit.
 *
 * @author Sapia
 * @version 1.0
 * @since 20-12-2016
 */
public class EntityManagerBatchFormularioInit {

	/**
	 * Método que crea el EntityManagerFactory.
	 * 
	 * @return EntityManagerFactory
	 */
	@SuppressWarnings("deprecation")
	@Produces
	@ApplicationScoped
	@Named("EMFORMULARIO")
	public EntityManagerFactory createEntityManagerFactoryFormulario() {
		return createEntityManagerFactory(EMFORMULARIO);
	}
		
	/**
	 * Método que realiza el apagado correctamente.
	 *
	 * @param entityManagerFactory
	 */
	public void closeEntityManagerFactoryFormulario(@Disposes EntityManagerFactory entityManagerFactory) {
		entityManagerFactory.close();
	}

	/**
	 * Método que crea el EntityManager.
	 * 
	 * @param entityManagerFactory
	 * @return EntityManager
	 */
	@SuppressWarnings("deprecation")
	@Produces
	@Named(EMFORMULARIO)
	@RequestScoped
	public EntityManager createEntityManagerFormulario(
			@Named("EMFORMULARIO") EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager(SYNCHRONIZED);
	}

	/**
	 * Este método es importante que se escriba por EntityManager, para cerrarlo
	 * adecuadamente despues de cada uso.
	 *
	 * @param entityManager
	 */
	@SuppressWarnings("deprecation")
	public void closeEntityManagerFormulario(@Disposes @Named(EMFORMULARIO) EntityManager entityManager) {
		entityManager.close();
	}
		
}
