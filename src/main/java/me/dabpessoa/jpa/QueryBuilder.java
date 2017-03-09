package me.dabpessoa.jpa;

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
                where = whereType.getSymbol()+" ";
            }
            where += (name+" "+comparableType.getSymbol()+" "+value);
            where(where);
        }
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();

        if (!query.getSelects().isEmpty()) stringBuilder.append("select ");
        for(int i = 0 ; i < query.getSelects().size() ; i++) {
            if (i + 1 == query.getSelects().size()) stringBuilder.append(query.getSelects().get(i)+" ");
            else stringBuilder.append(query.getSelects().get(i)+", ");
        }

        stringBuilder.append("from ");
        for (int i = 0 ; i < query.getFroms().size() ; i++) {
            if (i + 1 == query.getFroms().size()) stringBuilder.append(query.getFroms().get(i)+" ");
            else stringBuilder.append(query.getFroms().get(i)+", ");
        }

        if (!query.getWheres().isEmpty()) stringBuilder.append("where ");
        for (int i = 0 ; i < query.getWheres().size() ; i++) {
            stringBuilder.append(query.getWheres().get(i)+" ");
        }

        return stringBuilder.toString();
    }

}
