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

}
