package me.dabpessoa.service;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.*;

/**
 *
 * Created by diego.pessoa on 07/03/2017.
 */
@Configuration
@ComponentScan({"me.dabpessoa.*"})
@Profile({"production"})
@Lazy(true)
public class SpringConfigProduction extends SpringConfig {

    @Bean
    @Lazy(false)
    @Scope("singleton")
    public EnvironmentManager environmentManager() {
        return new EnvironmentManager("env.ini");
    }

    @Bean
    public String stringDevelopmentTest() {
        return "production";
    }

}
