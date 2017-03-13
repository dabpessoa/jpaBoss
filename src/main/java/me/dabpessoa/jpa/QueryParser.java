package me.dabpessoa.jpa;

import me.dabpessoa.utils.Primitive;
import me.dabpessoa.utils.ReflectionUtils;
import org.apache.taglibs.standard.tag.common.core.ParamSupport;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by diego.pessoa on 10/03/2017.
 */
public class QueryParser {

    public Map<String, String> parseToSQLParams(Map<String, Object> params, boolean wrapValues, boolean removeNulls, boolean deepObjectSearch) {

        if (deepObjectSearch) {

        }


        if (removeNulls) {
            params = removeNullValues(params);
        }

        //TODO FIXME
        return null;
    }

    public Map<String, Object> removeNullValues(Map<String, Object> params) {
        if (params != null) {
            Map<String, Object> notNullParams = new HashMap<>();
            for (String key : params.keySet()) {
                Object value = params.get(key);

                if (value != null && !value.toString().isEmpty()) {
                    notNullParams.put(key, value);
                }
            }
            return notNullParams;
        } return null;
    }

    public Map<String, String> SQLWrap(Map<String, Object> params) {
        if (params != null) {
            Map<String, String> wrapperParams = new HashMap<>();
            for (String key : params.keySet()) {
                Object value = params.get(key);
                String wrapperValue = SQLWrap(value);
                params.put(key, wrapperValue);
            }
            return wrapperParams;
        } return null;
    }

    public List<String> SQLWrap(List<Object> list) {
        if (list != null) {
            List<String> wrappeds = new ArrayList<>();
            for (Object object : list) {
                wrappeds.add(SQLWrap(object));
            }
            return wrappeds;
        } return null;
    }

    public String SQLWrap(Object object) {
        Object wrapedObject = null;
        if (object != null) {
            if (!Primitive.isPrimitiveOrWrapper(object)) {
                if (object instanceof String) {
                    wrapedObject = "'"+wrapedObject.toString()+"'";
                } else if (object instanceof BigDecimal) {
                    wrapedObject = String.valueOf(object);
                } else {
                    wrapedObject = null;
                }
            } else {
                wrapedObject = object;
            }
        } return wrapedObject != null ? wrapedObject.toString() : null;
    }

    // TODO FIXME cosertar esse método.
    public Object deepObjectSearchValue(String key, Object objectValue) {

        // Verifica se é um objeto diferente de primitivos, wrappers e string.
        if (!Primitive.isPrimitiveOrWrapper(objectValue) && !(objectValue instanceof String)) {

            int dotIndex = key.indexOf(".");

            String property;
            if (dotIndex != -1) property = key.substring(0, dotIndex);
            else property = key;

            Object newObjectValue = ReflectionUtils.findFieldValue(objectValue, property);

            String newKey;
            if (property != key) newKey = key.substring(dotIndex);
            else newKey = key;

            deepObjectSearchValue(newKey, newObjectValue);

        }

        return objectValue;

    }

    public static void main(String[] args) {

        String teste = "string teste...";
        Pessoa pessoa = new QueryParser().new Pessoa();
        pessoa.setNome("diego");
        pessoa.setCpf("123456");

        QueryParser qp = new QueryParser();
        Object value = qp.deepObjectSearchValue("nome", pessoa);
        System.out.println(value);

    }

    class Pessoa {
        private String nome;
        private String cpf;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }
    }

}
