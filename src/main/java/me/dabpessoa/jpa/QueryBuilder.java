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

    public QueryBuilder where(String where) {
        query.addWhere(where);
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();

        if (!query.getSelects().isEmpty()) stringBuilder.append("select ");
        for(int i = 0 ; i < query.getSelects().size() ; i++) {
            if (i + 1 == query.getSelects().size()) stringBuilder.append(query.getSelects().get(i));
            else stringBuilder.append(query.getSelects().get(i)+", ");
        }

        stringBuilder.append("from ");
        for (int i = 0 ; i < query.getFroms().size() ; i++) {
            if (i + 1 == query.getFroms().size()) stringBuilder.append(query.getFroms().get(i));
            else stringBuilder.append(query.getFroms().get(i)+", ");
        }

        if (!query.getWheres().isEmpty()) stringBuilder.append("where ");
        for (int i = 0 ; i < query.getWheres().size() ; i++) {
            if (i + 1 == query.getWheres().size()) stringBuilder.append(query.getWheres().get(i));
            else stringBuilder.append(query.getWheres().get(i)+", ");
        }

        return stringBuilder.toString();
    }

}
