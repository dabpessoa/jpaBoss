package me.dabpessoa.jpa;

import me.dabpessoa.utils.Primitive;
import me.dabpessoa.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diego.pessoa on 10/03/2017.
 */
public class QueryParser {

    public Map<String, String> parseToSQLParams(Map<String, Object> params, boolean wrapValues, boolean removeNulls, boolean deepObjectSearch) {

        if (deepObjectSearch) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (key.contains(".")) {
                    params.put(key, deepObjectSearchValue(key, value));
                }
            }
        }

        if (removeNulls) {
            params = removeNullValues(params);
        }

        Map<String, String> stringParams = null;
        if (wrapValues) {
            stringParams = SQLWrap(params);
        } else {

            for (String key : params.keySet()) {
                Object value = params.get(key);
                stringParams.put(key, value != null ? value.toString() : null);
            }

        }

        return stringParams;
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

    public Object deepObjectSearchValue(String key, Object objectValue) {

        if (objectValue == null) return null;

        if (!Primitive.isPrimitiveOrWrapper(objectValue) && !(objectValue instanceof String)) {
            int dotIndex = key.indexOf(".");

            String property;
            if (dotIndex != -1) property = key.substring(0, dotIndex);
            else property = key;

            Object newObjectValue = ReflectionUtils.findFieldValue(objectValue, property);

            String newKey;
            if (property != key) {
                newKey = key.substring(dotIndex+1);
            } else {
                return newObjectValue;
            }

            return deepObjectSearchValue(newKey, newObjectValue);
        }

        return null;

    }

    public Object basicFieldTypeTransform(Object value, Field field) {

        Type fieldType = field.getType();
        String fieldName = field.getName();

        if (value != null) {

            try {

                String myValue = value.toString();
                myValue = myValue.trim();

                if (Integer.class.equals(fieldType)) {
                    value = new Double(myValue).intValue();
                } else if (String.class.equals(fieldType)) {
                    value = myValue;
                } else if (BigDecimal.class.equals(fieldType)) {
                    value = new BigDecimal(myValue);
                } else if (Long.class.equals(fieldType)) {
                    value = Long.parseLong(myValue);
                } else if (Byte.class.equals(fieldType)) {
                    value = Byte.parseByte(myValue);
                } else if (Short.class.equals(fieldType)) {
                    value = Short.parseShort(myValue);
                } else if (Double.class.equals(fieldType)) {
                    value = Double.parseDouble(myValue);
                } else if (Float.class.equals(fieldType)) {
                    value = Float.parseFloat(myValue);
                } else if (BigInteger.class.equals(fieldType)) {
                    value = new BigInteger(myValue);
                }

            } catch (NumberFormatException e) {
                throw new RuntimeException("Não foi possível setar o valor: "+value+", no campo: "+fieldName+", do tipo: "+fieldType+", da classe: "+field.getDeclaringClass()+". Erro de conversão de tipo.", e);
            }

        }

        return value;

    }

}
