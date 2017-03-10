package me.dabpessoa.jpa;

import me.dabpessoa.utils.Primitive;
import me.dabpessoa.utils.ReflectionUtils;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by diego.pessoa on 09/03/2017.
 */
public class QueryBuilder {

    private Query query;

    public QueryBuilder(Query query) {
        this.query = query;
    }

    public QueryBuilder() {
        this(new Query());
    }

    public QueryBuilder select(String select) {
        query.addSelect(select);
        return this;
    }

    public QueryBuilder from(String from) {
        query.addFrom(from);
        return this;
    }

    public QueryBuilder join(String join) {
        query.addJoin(join);
        return this;
    }

    public QueryBuilder order(String order) {
        query.addOrder(order);
        return this;
    }

    public QueryBuilder where(String where) {
        query.addWhere(where);
        return this;
    }

    public QueryBuilder appendWhereObjects(Map<String, Object> params) {
        return appendWhereObjects(params, true);
    }

    public QueryBuilder appendWhereObjects(Map<String, Object> params, boolean wrapObjectValues) {
        QueryBuilder queryBuilder = this;
        if (params != null) {
            Map<String, String> mapString = new HashMap<>();
            for (String key : params.keySet()) {
                String keyAux = key;
                Object value = params.get(key);

                if (value != null && !value.toString().isEmpty()) {

                    if (!Primitive.isPrimitiveOrWrapper(value) && !(value instanceof String)) {

                        if (value instanceof List) {
                            if (((List)value).isEmpty()) {
                                value = null;
                            }
                        } else {
                            value = ReflectionUtils.findFirstFieldValueByAnnotation(value, Id.class);
                            if (value != null) {
                                keyAux = key+"."+"id";
                            }
                        }

                    }

                    if (value != null) {

                        if (value instanceof List) {
                            // Criar lista de strings a partir da lista de objetos.

                            List<String> stringValues = new ArrayList<>();
                            List<Object> list = ((List)value);

                            for (Object objectListValue : list) {
                                Class<?> genericClass = objectListValue.getClass();
                                if (genericClass != null && genericClass.equals(String.class)) {
                                    if (wrapObjectValues) {
                                        //objectListValue = wrap(objectListValue);
                                    }
                                    stringValues.add(objectListValue.toString());
                                } else {
                                    if (Primitive.isPrimitiveOrWrapper(objectListValue)) {
                                        stringValues.add(objectListValue.toString());
                                    } else {
                                        Object idValue = ReflectionUtils.findFirstFieldValueByAnnotation(objectListValue, Id.class);
                                        stringValues.add(idValue.toString());
                                    }
                                }
                            }

                            value = stringValues;
                        }

                        if (wrapObjectValues && !(value instanceof List)) {
                            // value = wrap(value);
                        }

                        if (value instanceof List) {
                            queryBuilder = appendWhere(keyAux, (List<String>)value);
                        } else {
                            queryBuilder = appendWhere(keyAux, value.toString());
                        }

                    }

                }

            }
        }

        return queryBuilder;
    }

    public QueryBuilder appendWhere(Map<String, String> params) {
        QueryBuilder queryBuilder = this;
        if (params != null)  {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (value != null && !value.isEmpty()) {
                    queryBuilder = appendWhere(key, params.get(key));
                }
            }
        }
        return queryBuilder;
    }

    public QueryBuilder appendWhere(String name, List<String> values) {
        QueryBuilder queryBuilder = this;
        if (values != null && !values.isEmpty()) {
            StringBuilder stringListValuesBuilder = new StringBuilder();
            for (int i = 0 ; i < values.size() ; i++) {
                if (i+1 == values.size()) stringListValuesBuilder.append(values.get(i));
                else stringListValuesBuilder.append(values.get(i)+SQLCommands.PARAMS_SEPARATOR.sintaxe());
            }
            String value = SQLCommands.PARANTESES_LEFT.sintaxe()+stringListValuesBuilder.toString()+SQLCommands.PARANTESES_RIGHT.sintaxe();
            queryBuilder = appendWhere(name, value, ComparableType.IN, WhereType.AND);
        }
        return queryBuilder;
    }

    public QueryBuilder appendWhere(String name, String value) {
        return appendWhere(name, value, ComparableType.EQUALS, WhereType.AND, true);
    }

    public QueryBuilder appendWhere(String name, String value, WhereType wheretype) {
        return appendWhere(name, value, ComparableType.EQUALS, wheretype, true);
    }

    public QueryBuilder appendWhere(String name, String value, WhereType wheretype, boolean condition) {
        return appendWhere(name, value, ComparableType.EQUALS, wheretype, condition);
    }

    public QueryBuilder appendWhere(String name, String value, boolean condition) {
        return appendWhere(name, value, ComparableType.EQUALS, WhereType.AND, condition);
    }

    public QueryBuilder appendWhere(String name, String value, ComparableType comparableType) {
        return appendWhere(name, value, comparableType, WhereType.AND, true);
    }

    public QueryBuilder appendWhere(String name, String value, ComparableType comparableType, WhereType whereType) {
        return appendWhere(name, value, comparableType, whereType, true);
    }

    public QueryBuilder appendWhere(String name, String value, ComparableType comparableType, WhereType whereType, boolean condition) {
        if (condition) {
            String where = null;
            if (query.getWheres() != null && !query.getWheres().isEmpty()) {
                where = whereType.getSymbol()+SQLCommands.WHITESPACE.sintaxe();
            }
            where += (name+SQLCommands.WHITESPACE.sintaxe()+comparableType.getSymbol()+SQLCommands.WHITESPACE.sintaxe()+value);
            where(where);
        }
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();

        if (!query.getSelects().isEmpty()) stringBuilder.append(SQLCommands.SELECT.sintaxe()+SQLCommands.WHITESPACE.sintaxe());
        for(int i = 0 ; i < query.getSelects().size() ; i++) {
            if (i + 1 == query.getSelects().size()) stringBuilder.append(query.getSelects().get(i)+SQLCommands.WHITESPACE.sintaxe());
            else stringBuilder.append(query.getSelects().get(i)+SQLCommands.PARAMS_SEPARATOR.sintaxe()+SQLCommands.WHITESPACE.sintaxe());
        }

        stringBuilder.append(SQLCommands.FROM.sintaxe()+" ");
        for (int i = 0 ; i < query.getFroms().size() ; i++) {
            if (i + 1 == query.getFroms().size()) stringBuilder.append(query.getFroms().get(i)+SQLCommands.WHITESPACE.sintaxe());
            else stringBuilder.append(query.getFroms().get(i)+SQLCommands.PARAMS_SEPARATOR.sintaxe()+SQLCommands.WHITESPACE.sintaxe());
        }

        if (!query.getWheres().isEmpty()) stringBuilder.append(SQLCommands.WHERE.sintaxe()+SQLCommands.WHITESPACE.sintaxe());
        for (int i = 0 ; i < query.getWheres().size() ; i++) {
            stringBuilder.append(query.getWheres().get(i)+SQLCommands.WHITESPACE.sintaxe());
        }

        return stringBuilder.toString();
    }

}
