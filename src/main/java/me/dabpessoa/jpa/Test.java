package me.dabpessoa.jpa;

import me.dabpessoa.service.SpringContextUtils;

import javax.persistence.EntityManager;

/**
 * Created by diego.pessoa on 09/03/2017.
 */
public class Test {

    public static void main(String[] args) {

        EntityManager entityManager = SpringContextUtils.getBean("entityManager", "development");

        String sql = Query.create().select("p")
                .from("saa.tb_prestacao p")
                .where("p.id = 3699").build();

        QueryExecutor.create(entityManager).queryList(sql);

    }

}
