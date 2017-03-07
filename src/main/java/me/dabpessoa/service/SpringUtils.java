package me.dabpessoa.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class SpringUtils {

	public static final String contextPath = "classpath:applicationContext.xml";
	public static final Class<?>[] contextClasses = {SpringConfigDevelopment.class, SpringConfigProduction.class, SpringConfig.class};

    private static ApplicationContext context;

	private SpringUtils() {}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) getContext().getBean(name);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name, String... activeProfiles) {
		return (T) getAnnotationConfigContext(activeProfiles).getBean(name);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> clazz) { 
		return (T) getContext().getBean(clazz);
	}

	public static <T> T getBean(Class<?> clazz, String... activeProfiles) {
		return (T) getAnnotationConfigContext(activeProfiles).getBean(clazz);
	}
	
	public static void init(final ApplicationContext contextApp) {
		context = contextApp;
	}

	public synchronized static ApplicationContext getContext() {
		if (context == null) {
			if (FacesContext.getCurrentInstance() != null) {
				context = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
			}
			
			if (context == null) {
				context = ApplicationContextProvider.getApplicationContext();
			}
			
			if (context == null) {
				context = new FileSystemXmlApplicationContext(contextPath);
			}

			if (context == null) {
				AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext();
				annotationContext.register(contextClasses);
//				annotationContext.getEnvironment().setActiveProfiles("development");
//				System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
				context = annotationContext;
			}
		}
		return context;
	}

	public synchronized static ApplicationContext getAnnotationConfigContext(String... activeProfiles) {
		if (context == null) {
			AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext();
			annotationContext.getEnvironment().setActiveProfiles(activeProfiles);
//			System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
			annotationContext.register(contextClasses);
			annotationContext.refresh();
			context = annotationContext;
		}
		return context;
	}
	
	public static Resource[] findResources(String locationPattern) throws IOException {
		return new PathMatchingResourcePatternResolver().getResources(locationPattern);
	}

	public static void main(String[] args) {
		String string = SpringUtils.getBean("stringDevelopmentTest", "development");
		System.out.println("Profile: "+string);

	}

}