package me.dabpessoa.service;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by diego.pessoa on 08/03/2017.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile(EnvironmentManager.PROFILE_PRODUCTION_SECTION)
public @interface Production {

}
