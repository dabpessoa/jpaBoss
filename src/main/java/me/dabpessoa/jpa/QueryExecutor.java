package me.dabpessoa.jpa;

import me.dabpessoa.utils.Primitive;
import me.dabpessoa.utils.ReflectionUtils;

import javax.persistence.*;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by diego.pessoa on 09/03/2017.
 */
public class QueryExecutor {

    private EntityManager entityManager;

    private QueryExecutor() {}

    public QueryExecutor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static QueryExecutor create(EntityManager entityManager) {
        return new QueryExecutor(entityManager);
    }

    public <T> List<T> queryList(String query) {
        return queryList(query, (Map)null);
    }

    public <T> List<T> queryList(String hql, Map<String,Object> params) {
        javax.persistence.Query query = entityManager.createQuery(hql);

        if (params != null) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    query.setParameter(key, value);
                }
            }
        }
        return query.getResultList();
    }

    public <T> T querySingleResult(String hql, Map<String,Object> params) {
        javax.persistence.Query query = entityManager.createQuery(hql);

        if (params != null) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value != null) {
                    query.setParameter(key, value);
                }
            }
        }
        return (T) query.getSingleResult();
    }

    public <T> List<T> queryList(T entity) {
        Field fields[] = entity.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) return null;
        return queryList(entity, Arrays.asList(fields));
    }

    public <T> List<T> queryListByDefaultJPAAnnotations(T entity) {
        return queryList(entity, Column.class, JoinColumn.class, OneToMany.class, ManyToOne.class, AssociationOverride.class);
    }

    public <T> List<T> queryList(T entity, Class<?>... annotationFieldClasses) {
        List<Field> fields = ReflectionUtils.findFieldsByAnnotations(entity.getClass(), annotationFieldClasses);
        if (fields == null || fields.isEmpty()) return null;
        return queryList(entity, fields);
    }

    public <T> List<T> queryList(T entity, List<Field> fields) {
        Map<String, Object> params = new HashMap<>();
        if (fields != null) {
            for (Field field : fields) {
                String fieldName = field.getName();
                Object fieldValue = ReflectionUtils.findFieldValue(entity, field);
                params.put(fieldName, fieldValue);
            }
        }
        return queryList((Class<T>)entity.getClass(), params);
    }

    public <T> List<T> queryList(T entity, String... fieldsName) {
        Field[] fields = ReflectionUtils.findFieldsByNames(entity.getClass(), fieldsName);
        if (fields == null || fields.length == 0) return null;
        return queryList(entity, Arrays.asList(fields));
    }

    public <T> List<T> queryList(Class<T> clazz, Map<String, Object> params) {
        List<QueryValue> queryValues = new ArrayList<>();
        for (String key : params.keySet()) {
            queryValues.add(new QueryValue(key, params.get(key)));
        }
        return queryList(clazz, queryValues, null, null, null);
    }

    public <T> List<T> queryList(Class<T> clazz, List<QueryValue> queryValues) {
        return queryList(clazz, queryValues, null, null, null);
    }

    public <T> List<T> queryList(Class<T> clazz, List<QueryValue> queryValues, String joins) {
        return queryList(clazz, queryValues, null, joins, null);
    }

    public <T> List<T> queryList(Class<T> clazz, List<QueryValue> queryValues, String entityAlias, String joins, String order) {
        QueryBuilder queryBuilder = me.dabpessoa.jpa.Query.create()
                .select(entityAlias)
                .from(clazz.getName())
                .join(joins)
                .order(order);

        Iterator<QueryValue> it = queryValues.iterator();
        while (it.hasNext()) {
//            queryBuilder.appendWhere();
        }







//        entityAlias = entityAlias != null ? entityAlias : "entity";
//        StringBuffer sb = new StringBuffer("select "+entityAlias+" from "+clazz.getName()+" "+entityAlias+" ");
//        if (joins != null && !joins.isEmpty()) {
//            sb.append(" "+joins+" ");
//        }
//        sb.append(" where 1=1 ");
//
//        List<QueryValue> list = new ArrayList<>();
//        list.addAll(queryValues);
//
//        Iterator<QueryValue> it = list.iterator();
//        while (it.hasNext()) {
//            QueryValue queryValue = it.next();
//            String entityPropertyName = queryValue.getEntityPropertyName().indexOf(".") == -1 ? entityAlias+"."+queryValue.getEntityPropertyName() : queryValue.getEntityPropertyName();
//            String queryParamName = queryValue.getQueryParamName();
//            if (queryParamName == null) queryParamName = queryValue.getEntityPropertyName();
//            Object value = queryValue.getQueryParamValue();
//
//            if (value != null && !value.toString().isEmpty()) {
//
//                if (!Primitive.isPrimitiveOrWrapper(value) && !(value instanceof String)) {
//
//                    Object v = null;
//                    if (value instanceof List) {
//                        if (!((List)value).isEmpty()) {
//                            v = (List)value;
//                        }
//                    } else {
//                        v = ReflectionUtils.findFirstFieldValueByAnnotation(value, Id.class);
//                    }
//
//                    if (v == null) {
//                        it.remove();
//                        continue;
//                    }
//                }
//
//                if (value instanceof String) sb.append(" and lower("+entityPropertyName+") like "+"lower(:"+queryParamName+") ");
//                else if (value instanceof List) sb.append(" and "+entityPropertyName+" in (:"+queryParamName+") ");
//                else sb.append(" and "+entityPropertyName+" = :"+queryParamName+" ");
//
//            } else it.remove();
//        }
//
//        if (order != null && !order.isEmpty()) {
//            sb.append(" "+order+" ");
//        }
//
//        Query q = entityManager.createQuery(sb.toString());
//        for (QueryValue queryValue : list) {
//            String queryParamName = queryValue.getQueryParamName();
//            if (queryParamName == null) queryParamName = queryValue.getEntityPropertyName();
//            Object value = queryValue.getQueryParamValue();
//            if (value instanceof String) q.setParameter(queryParamName, "%"+value.toString()+"%");
//            else q.setParameter(queryParamName, value);
//        }
//        return q.getResultList();

        return null;

    }

    class QueryValue {
        private String entityPropertyName;
        private String queryParamName;
        private Object queryParamValue;
        public QueryValue() {}
        public QueryValue(String entityPropertyName, String queryParamName, Object queryParamValue) {
            this.entityPropertyName = entityPropertyName;
            this.queryParamName = queryParamName;
            this.queryParamValue = queryParamValue;
        }
        public QueryValue(String entityPropertyName, Object queryParamValue) {
            this.entityPropertyName = entityPropertyName;
            this.queryParamValue = queryParamValue;
        }
        public String getEntityPropertyName() {
            return entityPropertyName;
        }
        public void setEntityPropertyName(String entityPropertyName) {
            this.entityPropertyName = entityPropertyName;
        }
        public String getQueryParamName() {
            return queryParamName;
        }
        public void setQueryParamName(String queryParamName) {
            this.queryParamName = queryParamName;
        }
        public Object getQueryParamValue() {
            return queryParamValue;
        }
        public void setQueryParamValue(Object queryParamValue) {
            this.queryParamValue = queryParamValue;
        }
    }

}
