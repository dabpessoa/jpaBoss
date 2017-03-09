package me.dabpessoa.teste;

import me.dabpessoa.jpa.JPAHelper;
import me.dabpessoa.service.SpringContextUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by diego.pessoa on 09/03/2017.
 */
@Service
public class Teste {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private JPAHelper hibernateHelper;

    public void teste() {
        System.out.println("EntityManager: "+entityManager);
    }

    public static void main(String[] args) {
        SpringContextUtils.getBean(Teste.class, "development");
    }

}
