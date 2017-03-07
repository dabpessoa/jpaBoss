package me.dabpessoa.service;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.*;

import javax.swing.*;

/**
 * Created by diego.pessoa on 07/03/2017.
 */
@Configuration
@ComponentScan({"me.dabpessoa.*"})
@Profile({"development"})
@Lazy(true)
public class SpringConfigDevelopment extends SpringConfig {

    @Bean
    @Lazy(false)
    @Scope("singleton")
    public EnvironmentManager environmentManager() {
        return new EnvironmentManager("env.ini");
    }

    @Bean
    public String stringDevelopmentTest() {
        return "development";
    }

}
