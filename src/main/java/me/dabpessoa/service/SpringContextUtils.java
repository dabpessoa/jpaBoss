package me.dabpessoa.service;

import me.dabpessoa.teste.Teste;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public abstract class SpringContextUtils {

	public static final String contextXMLPath = "classpath:applicationContext.xml";

	// Se não houver nenhuma configuração baseada em annotation deixar esse array vazio.
	public static final Class<?>[] contextAnnotationClasses = {SpringConfig.class, SpringConfigDevelopment.class, SpringConfigProduction.class};

	public static final SpringContextLoadType DEFAULT_CONTEXT_LOAD_TYPE = SpringContextLoadType.CONFIGURATION_ANNOTATION;

	public enum SpringContextLoadType {CONFIGURATION_ANNOTATION, XML};

    private static ApplicationContext context;

	// Construtor "private" para impedir instanciação desta classe mesmo internamente.
	private SpringContextUtils() {}

	public static <T> T getBean(String name) {
		return (T) getContext(null).getBean(name);
	}

	public static <T> T getBeanWithConstructorArgs(String name, String... constructorArgs) {
		return (T) getContext(null).getBean(name, constructorArgs);
	}

	public static <T> T getBean(String name, String... activeProfiles) {
		return getConfigurationAnnotationBean(name, activeProfiles);
	}

	public static <T> T getBeanWithConstructorArgs(String name, String[] constructorArgs, String... activeProfiles) {
		return (T) getContext(SpringContextLoadType.CONFIGURATION_ANNOTATION, activeProfiles).getBean(name, constructorArgs);
	}

	public static <T> T getBean(Class<?> clazz) {
		return (T) getContext(null).getBean(clazz);
	}

	public static <T> T getBean(Class<?> clazz, String... activeProfiles) {
		return getConfigurationAnnotationBean(clazz, activeProfiles);
	}

	public static <T> T getXMLBean(String name) {
		return (T) getContext(SpringContextLoadType.XML).getBean(name);
	}

	public static <T> T getXMLBean(Class<?> clazz) {
		return (T) getContext(SpringContextLoadType.XML).getBean(clazz);
	}

	public static <T> T getConfigurationAnnotationBean(String name) {
		return (T) getContext(SpringContextLoadType.CONFIGURATION_ANNOTATION).getBean(name);
	}

	public static <T> T getConfigurationAnnotationBean(Class<?> clazz) {
		return (T) getContext(SpringContextLoadType.CONFIGURATION_ANNOTATION).getBean(clazz);
	}

	public static <T> T getConfigurationAnnotationBean(String name, String... activeProfiles) {
		return (T) getContext(SpringContextLoadType.CONFIGURATION_ANNOTATION, activeProfiles).getBean(name);
	}

	public static <T> T getConfigurationAnnotationBean(Class<?> clazz, String... activeProfiles) {
		return (T) getContext(SpringContextLoadType.CONFIGURATION_ANNOTATION, activeProfiles).getBean(clazz);
	}

	public static void changeProfiles(String... activeProfiles) {
		if (context != null) {
			if (activeProfiles != null && activeProfiles.length != 0) {
				if (context instanceof AnnotationConfigApplicationContext) {
					((AnnotationConfigApplicationContext)context).getEnvironment().setActiveProfiles(activeProfiles);
					((AnnotationConfigApplicationContext) context).refresh();
				}
			}
		}
	}

	public static Resource[] findResources(String locationPattern) throws IOException {
		return new PathMatchingResourcePatternResolver().getResources(locationPattern);
	}

	public static void init(final ApplicationContext contextApp) {
		context = contextApp;
	}

	public static String[] getActiveProfiles() {
		if (context == null) return null;
		else return context.getEnvironment().getActiveProfiles();
	}

	private synchronized static ApplicationContext getContext(SpringContextLoadType springContextLoadType, String... activeProfiles) {
		if (context == null) {
			if (FacesContext.getCurrentInstance() != null) {
				context = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
			}

			if (context == null) {
				context = ApplicationContextProvider.getApplicationContext();
			}

			if (context == null) {
				if (springContextLoadType == null) springContextLoadType = DEFAULT_CONTEXT_LOAD_TYPE;
				switch (springContextLoadType) {
					case CONFIGURATION_ANNOTATION: {
						context = getAnnotationConfigContext(activeProfiles);
					} break;
					case XML: {
						context = new FileSystemXmlApplicationContext(contextXMLPath);
					} break;
					default: throw new RuntimeException("Tipo de contexto de carregamento de beans do spring incorreto. Tipo: "+springContextLoadType);
				}
			}
		} else {
			if (springContextLoadType == SpringContextLoadType.CONFIGURATION_ANNOTATION) {
				// Se os profiles forem diferentes, deve-se atualizar os profiles.
				if (activeProfiles != null && activeProfiles.length != 0 && !Arrays.equals(activeProfiles, context.getEnvironment().getActiveProfiles())) {
					changeProfiles(activeProfiles);
				}
			}
		}
		return context;
	}

	private static ApplicationContext getAnnotationConfigContext(String... activeProfiles) {
		AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext();
		annotationContext.getEnvironment().setActiveProfiles(activeProfiles);
		annotationContext.register(contextAnnotationClasses);
		annotationContext.refresh();
		return annotationContext;
	}

	public static void main(String[] args) {
//		String string = SpringContextUtils.getBean("stringTest");
//		System.out.println(string);

//		Object o = SpringContextUtils.getBean("entityManager");
//		System.out.println(o);

//		EnvironmentManager environmentManager = SpringContextUtils.getBean("environmentManager", EnvironmentManager.EnvironmentSection.DEVELOPMENT.getDescricao());
//		System.out.println(environmentManager);

//		DataSource dataSource = SpringContextUtils.getBean("dataSource", EnvironmentManager.EnvironmentSection.DEVELOPMENT.getDescricao());
//		System.out.println(dataSource);

//		Teste testeService = SpringContextUtils.getBean("teste", EnvironmentManager.EnvironmentSection.DEVELOPMENT.getDescricao());
//		testeService.teste();

		EntityManager em = SpringContextUtils.getBean("entityManager", EnvironmentManager.EnvironmentSection.DEVELOPMENT.getDescricao());
		System.out.println(em);

		BigInteger result = (BigInteger) em.createNativeQuery("select count(*) from saa.tb_prestacao").getSingleResult();
		System.out.println(result);

	}

}